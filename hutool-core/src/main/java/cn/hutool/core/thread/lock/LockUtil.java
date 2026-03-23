package cn.hutool.core.thread.lock;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * 锁相关工具
 *
 * @author looly
 * @since 5.2.5
 */
public class LockUtil {

	private static final NoLock NO_LOCK = new NoLock();

	/**
	 * 创建{@link StampedLock}锁
	 *
	 * @return {@link StampedLock}锁
	 */
	public static StampedLock createStampLock() {
		return new StampedLock();
	}

	/**
	 * 创建{@link ReentrantReadWriteLock}锁
	 *
	 * @param fair 是否公平锁
	 * @return {@link ReentrantReadWriteLock}锁
	 */
	public static ReentrantReadWriteLock createReadWriteLock(boolean fair) {
		return new ReentrantReadWriteLock(fair);
	}

	/**
	 * 获取单例的无锁对象
	 *
	 * @return {@link NoLock}
	 */
	public static NoLock getNoLock(){
		return NO_LOCK;
	}

	/**
	 * 创建分段锁（强引用），使用 ReentrantLock
	 *
	 * @param segments 分段数量，必须大于 0
	 * @return 分段锁实例
	 */
	public static SegmentLock<Lock> createSegmentLock(int segments) {
		return SegmentLock.lock(segments);
	}

	/**
	 * 创建分段读写锁（强引用），使用 ReentrantReadWriteLock
	 *
	 * @param segments 分段数量，必须大于 0
	 * @return 分段读写锁实例
	 */
	public static SegmentLock<ReadWriteLock> createSegmentReadWriteLock(int segments) {
		return SegmentLock.readWriteLock(segments);
	}

	/**
	 * 创建分段信号量（强引用）
	 *
	 * @param segments 分段数量，必须大于 0
	 * @param permits 每个信号量的许可数
	 * @return 分段信号量实例
	 */
	public static SegmentLock<Semaphore> createSegmentSemaphore(int segments, int permits) {
		return SegmentLock.semaphore(segments, permits);
	}

	/**
	 * 创建弱引用分段锁，使用 ReentrantLock，懒加载
	 *
	 * @param segments 分段数量，必须大于 0
	 * @return 弱引用分段锁实例
	 */
	public static SegmentLock<Lock> createLazySegmentLock(int segments) {
		return SegmentLock.lazyWeakLock(segments);
	}

	/**
	 * 根据 key 获取分段锁（强引用）
	 *
	 * @param segments 分段数量，必须大于 0
	 * @param key 用于映射分段的 key
	 * @return 对应的 Lock 实例
	 */
	public static Lock getSegmentLock(int segments, Object key) {
		return SegmentLock.lock(segments).get(key);
	}

	/**
	 * 根据 key 获取分段读锁（强引用）
	 *
	 * @param segments 分段数量，必须大于 0
	 * @param key 用于映射分段的 key
	 * @return 对应的读锁实例
	 */
	public static Lock getSegmentReadLock(int segments, Object key) {
		return SegmentLock.readWriteLock(segments).get(key).readLock();
	}

	/**
	 * 根据 key 获取分段写锁（强引用）
	 *
	 * @param segments 分段数量，必须大于 0
	 * @param key 用于映射分段的 key
	 * @return 对应的写锁实例
	 */
	public static Lock getSegmentWriteLock(int segments, Object key) {
		return SegmentLock.readWriteLock(segments).get(key).writeLock();
	}

	/**
	 * 根据 key 获取分段信号量（强引用）
	 *
	 * @param segments 分段数量，必须大于 0
	 * @param permits 每个信号量的许可数
	 * @param key 用于映射分段的 key
	 * @return 对应的 Semaphore 实例
	 */
	public static Semaphore getSegmentSemaphore(int segments, int permits, Object key) {
		return SegmentLock.semaphore(segments, permits).get(key);
	}

	/**
	 * 根据 key 获取弱引用分段锁，懒加载
	 *
	 * @param segments 分段数量，必须大于 0
	 * @param key 用于映射分段的 key
	 * @return 对应的 Lock 实例
	 */
	public static Lock getLazySegmentLock(int segments, Object key) {
		return SegmentLock.lazyWeakLock(segments).get(key);
	}
}
