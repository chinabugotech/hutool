package cn.hutool.core.thread.lock;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * 分段锁工具类，支持 Lock、Semaphore 和 ReadWriteLock 的分段实现。
 * <p>
 * 通过将锁分成多个段（segments），不同的操作可以并发使用不同的段，避免所有线程竞争同一把锁。
 * 相等的 key 保证映射到同一段锁（如 key1.equals(key2) 时，get(key1) 和 get(key2) 返回相同对象）。
 * 但不同 key 可能因哈希冲突映射到同一段，段数越少冲突概率越高。
 * <p>
 * 支持两种实现：
 * <ul>
 *     <li>强引用：创建时初始化所有段，内存占用稳定。</li>
 *     <li>弱引用：懒加载，首次使用时创建段，未使用时可被垃圾回收，适合大量段但使用较少的场景。</li>
 * </ul>
 *
 * @param <L> 锁类型
 * @author Guava,dakuo
 * @since 5.8.38
 */
public abstract class SegmentLock<L> {

	/** 当段数大于此阈值时，使用 ConcurrentMap 替代大数组以节省内存（适用于懒加载场景） */
	private static final int LARGE_LAZY_CUTOFF = 1024;

	private SegmentLock() {}

	/**
	 * 根据 key 获取对应的锁段，保证相同 key 返回相同对象。
	 *
	 * @param key 非空 key
	 * @return 对应的锁段
	 */
	public abstract L get(Object key);

	/**
	 * 根据索引获取锁段，索引范围为 [0, size())。
	 *
	 * @param index 索引
	 * @return 指定索引的锁段
	 */
	public abstract L getAt(int index);

	/**
	 * 计算 key 对应的段索引。
	 *
	 * @param key 非空 key
	 * @return 段索引
	 */
	abstract int indexFor(Object key);

	/**
	 * 获取总段数。
	 *
	 * @return 段数
	 */
	public abstract int size();

	/**
	 * 批量获取多个 key 对应的锁段列表，按索引升序排列，避免死锁。
	 *
	 * @param keys 非空 key 集合
	 * @return 锁段列表（可能有重复）
	 */
	public Iterable<L> bulkGet(Iterable<?> keys) {
		@SuppressWarnings("unchecked")
		List<Object> result = (List<Object>) CollUtil.newArrayList(keys);
		if (CollUtil.isEmpty(result)) {
			return Collections.emptyList();
		}
		int[] stripes = new int[result.size()];
		for (int i = 0; i < result.size(); i++) {
			stripes[i] = indexFor(result.get(i));
		}
		Arrays.sort(stripes);
		int previousStripe = stripes[0];
		result.set(0, getAt(previousStripe));
		for (int i = 1; i < result.size(); i++) {
			int currentStripe = stripes[i];
			if (currentStripe == previousStripe) {
				result.set(i, result.get(i - 1));
			} else {
				result.set(i, getAt(currentStripe));
				previousStripe = currentStripe;
			}
		}
		@SuppressWarnings("unchecked")
		List<L> asStripes = (List<L>) result;
		return Collections.unmodifiableList(asStripes);
	}

	// 静态工厂方法

	/**
	 * 创建强引用的分段锁，所有段在创建时初始化。
	 *
	 * @param stripes 段数
	 * @param supplier 锁提供者
	 * @param <L> 锁类型
	 * @return 分段锁实例
	 */
	public static <L> SegmentLock<L> custom(int stripes, Supplier<L> supplier) {
		return new CompactSegmentLock<>(stripes, supplier);
	}

	/**
	 * 创建强引用的可重入锁分段实例。
	 *
	 * @param stripes 段数
	 * @return 分段锁实例
	 */
	public static SegmentLock<Lock> lock(int stripes) {
		return custom(stripes, PaddedLock::new);
	}

	/**
	 * 创建弱引用的可重入锁分段实例，懒加载。
	 *
	 * @param stripes 段数
	 * @return 分段锁实例
	 */
	public static SegmentLock<Lock> lazyWeakLock(int stripes) {
		return lazyWeakCustom(stripes, () -> new ReentrantLock(false));
	}

	/**
	 * 创建弱引用的分段锁，懒加载。
	 *
	 * @param stripes 段数
	 * @param supplier 锁提供者
	 * @param <L> 锁类型
	 * @return 分段锁实例
	 */
	private static <L> SegmentLock<L> lazyWeakCustom(int stripes, Supplier<L> supplier) {
		return stripes < LARGE_LAZY_CUTOFF
			? new SmallLazySegmentLock<>(stripes, supplier)
			: new LargeLazySegmentLock<>(stripes, supplier);
	}

	/**
	 * 创建强引用的信号量分段实例。
	 *
	 * @param stripes 段数
	 * @param permits 每个信号量的许可数
	 * @return 分段信号量实例
	 */
	public static SegmentLock<Semaphore> semaphore(int stripes, int permits) {
		return custom(stripes, () -> new PaddedSemaphore(permits));
	}

	/**
	 * 创建弱引用的信号量分段实例，懒加载。
	 *
	 * @param stripes 段数
	 * @param permits 每个信号量的许可数
	 * @return 分段信号量实例
	 */
	public static SegmentLock<Semaphore> lazyWeakSemaphore(int stripes, int permits) {
		return lazyWeakCustom(stripes, () -> new Semaphore(permits, false));
	}

	/**
	 * 创建强引用的读写锁分段实例。
	 *
	 * @param stripes 段数
	 * @return 分段读写锁实例
	 */
	public static SegmentLock<ReadWriteLock> readWriteLock(int stripes) {
		return custom(stripes, ReentrantReadWriteLock::new);
	}

	/**
	 * 创建弱引用的读写锁分段实例，懒加载。
	 *
	 * @param stripes 段数
	 * @return 分段读写锁实例
	 */
	public static SegmentLock<ReadWriteLock> lazyWeakReadWriteLock(int stripes) {
		return lazyWeakCustom(stripes, WeakSafeReadWriteLock::new);
	}

	// 内部实现类

	/**
	 * 弱引用安全的读写锁实现，确保读锁和写锁持有对自身的强引用。
	 */
	private static final class WeakSafeReadWriteLock implements ReadWriteLock {
		private final ReadWriteLock delegate;

		WeakSafeReadWriteLock() {
			this.delegate = new ReentrantReadWriteLock();
		}

		@Override
		public Lock readLock() {
			return new WeakSafeLock(delegate.readLock(), this);
		}

		@Override
		public Lock writeLock() {
			return new WeakSafeLock(delegate.writeLock(), this);
		}
	}

	/**
	 * 弱引用安全的锁包装类，确保持有强引用。
	 */
	private static final class WeakSafeLock implements Lock {
		private final Lock delegate;
		private final WeakSafeReadWriteLock strongReference;

		WeakSafeLock(Lock delegate, WeakSafeReadWriteLock strongReference) {
			this.delegate = delegate;
			this.strongReference = strongReference;
		}

		@Override
		public void lock() {
			delegate.lock();
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			delegate.lockInterruptibly();
		}

		@Override
		public boolean tryLock() {
			return delegate.tryLock();
		}

		@Override
		public boolean tryLock(long time, java.util.concurrent.TimeUnit unit) throws InterruptedException {
			return delegate.tryLock(time, unit);
		}

		@Override
		public void unlock() {
			delegate.unlock();
		}

		@Override
		public Condition newCondition() {
			return new WeakSafeCondition(delegate.newCondition(), strongReference);
		}
	}

	/**
	 * 弱引用安全的条件包装类。
	 */
	@SuppressWarnings("FieldCanBeLocal")
	private static final class WeakSafeCondition implements Condition {
		private final Condition delegate;

		/** 防止垃圾回收 */
		private final WeakSafeReadWriteLock strongReference;

		WeakSafeCondition(Condition delegate, WeakSafeReadWriteLock strongReference) {
			this.delegate = delegate;
			this.strongReference = strongReference;
		}

		@Override
		public void await() throws InterruptedException {
			delegate.await();
		}

		@Override
		public void awaitUninterruptibly() {
			delegate.awaitUninterruptibly();
		}

		@Override
		public long awaitNanos(long nanosTimeout) throws InterruptedException {
			return delegate.awaitNanos(nanosTimeout);
		}

		@Override
		public boolean await(long time, TimeUnit unit) throws InterruptedException {
			return delegate.await(time, unit);
		}

		@Override
		public boolean awaitUntil(Date deadline) throws InterruptedException {
			return delegate.awaitUntil(deadline);
		}

		@Override
		public void signal() {
			delegate.signal();
		}

		@Override
		public void signalAll() {
			delegate.signalAll();
		}
	}

	/**
	 * 抽象基类，确保段数为 2 的幂。
	 */
	private abstract static class PowerOfTwoSegmentLock<L> extends SegmentLock<L> {
		final int mask;

		PowerOfTwoSegmentLock(int stripes) {
			Assert.isTrue(stripes > 0, "Segment count must be positive");
			this.mask = stripes > Integer.MAX_VALUE / 2 ? ALL_SET : ceilToPowerOfTwo(stripes) - 1;
		}

		@Override
		final int indexFor(Object key) {
			int hash = smear(key.hashCode());
			return hash & mask;
		}

		@Override
		public final L get(Object key) {
			return getAt(indexFor(key));
		}
	}

	/**
	 * 强引用实现，使用固定数组存储段。
	 */
	private static class CompactSegmentLock<L> extends PowerOfTwoSegmentLock<L> {
		private final Object[] array;

		CompactSegmentLock(int stripes, Supplier<L> supplier) {
			super(stripes);
			Assert.isTrue(stripes <= Integer.MAX_VALUE / 2, "Segment count must be <= 2^30");
			this.array = new Object[mask + 1];
			for (int i = 0; i < array.length; i++) {
				array[i] = supplier.get();
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public L getAt(int index) {
			if (index < 0 || index >= array.length) {
				throw new IllegalArgumentException("Index " + index + " out of bounds for size " + array.length);
			}
			return (L) array[index];
		}

		@Override
		public int size() {
			return array.length;
		}
	}

	/**
	 * 小规模弱引用实现，使用 AtomicReferenceArray 存储段。
	 */
	private static class SmallLazySegmentLock<L> extends PowerOfTwoSegmentLock<L> {
		final AtomicReferenceArray<ArrayReference<? extends L>> locks;
		final Supplier<L> supplier;
		final int size;
		final ReferenceQueue<L> queue = new ReferenceQueue<>();

		SmallLazySegmentLock(int stripes, Supplier<L> supplier) {
			super(stripes);
			this.size = (mask == ALL_SET) ? Integer.MAX_VALUE : mask + 1;
			this.locks = new AtomicReferenceArray<>(size);
			this.supplier = supplier;
		}

		@Override
		public L getAt(int index) {
			if (size != Integer.MAX_VALUE) {
				Assert.isTrue(index >= 0 && index < size, "Index out of bounds");
			}
			ArrayReference<? extends L> existingRef = locks.get(index);
			L existing = existingRef == null ? null : existingRef.get();
			if (existing != null) {
				return existing;
			}
			L created = supplier.get();
			ArrayReference<L> newRef = new ArrayReference<>(created, index, queue);
			while (!locks.compareAndSet(index, existingRef, newRef)) {
				existingRef = locks.get(index);
				existing = existingRef == null ? null : existingRef.get();
				if (existing != null) {
					return existing;
				}
			}
			drainQueue();
			return created;
		}

		private void drainQueue() {
			Reference<? extends L> ref;
			while ((ref = queue.poll()) != null) {
				ArrayReference<? extends L> arrayRef = (ArrayReference<? extends L>) ref;
				locks.compareAndSet(arrayRef.index, arrayRef, null);
			}
		}

		@Override
		public int size() {
			return size;
		}

		private static final class ArrayReference<L> extends WeakReference<L> {
			final int index;

			ArrayReference(L referent, int index, ReferenceQueue<L> queue) {
				super(referent, queue);
				this.index = index;
			}
		}
	}

	/**
	 * 大规模弱引用实现，使用 ConcurrentMap 存储段。
	 */
	private static class LargeLazySegmentLock<L> extends PowerOfTwoSegmentLock<L> {
		final ConcurrentMap<Integer, L> locks;
		final Supplier<L> supplier;
		final int size;

		LargeLazySegmentLock(int stripes, Supplier<L> supplier) {
			super(stripes);
			this.size = (mask == ALL_SET) ? Integer.MAX_VALUE : mask + 1;
			this.locks = new ConcurrentHashMap<>();
			this.supplier = supplier;
		}

		@Override
		public L getAt(int index) {
			if (size != Integer.MAX_VALUE) {
				Assert.isTrue(index >= 0 && index < size, "Index out of bounds");
			}
			L existing = locks.get(index);
			if (existing != null) {
				return existing;
			}
			L created = supplier.get();
			existing = locks.putIfAbsent(index, created);
			return existing != null ? existing : created;
		}

		@Override
		public int size() {
			return size;
		}
	}

	private static final int ALL_SET = ~0;

	private static int ceilToPowerOfTwo(int x) {
		return 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(x - 1));
	}

	private static int smear(int hashCode) {
		hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
		return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);
	}

	/**
	 * 填充锁，避免缓存行干扰。
	 */
	private static class PaddedLock extends ReentrantLock {
		private static final long serialVersionUID = 1L;

		long unused1;
		long unused2;
		long unused3;

		PaddedLock() {
			super(false);
		}
	}

	/**
	 * 填充信号量，避免缓存行干扰。
	 */
	private static class PaddedSemaphore extends Semaphore {
		private static final long serialVersionUID = 1L;

		long unused1;
		long unused2;
		long unused3;

		PaddedSemaphore(int permits) {
			super(permits, false);
		}
	}

}
