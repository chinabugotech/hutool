package cn.hutool.cache.impl;

import cn.hutool.core.collection.CopiedIter;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.lang.mutable.Mutable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用{@link ReentrantLock}保护的缓存，读写都使用悲观锁完成，主要避免某些Map无法使用读写锁的问题<br>
 * 例如使用了LinkedHashMap的缓存，由于get方法也会改变Map的结构，因此读写必须加互斥锁
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 5.7.15
 */
public abstract class ReentrantCache<K, V> extends AbstractCache<K, V> {
	private static final long serialVersionUID = 1L;

	// 一些特殊缓存，例如使用了LinkedHashMap的缓存，由于get方法也会改变Map的结构，导致无法使用读写锁
	// TODO 最优的解决方案是使用Guava的ConcurrentLinkedHashMap，此处使用简化的互斥锁
	protected final ReentrantLock lock = new ReentrantLock();

	private volatile boolean useKeyLock = true;
	private final AtomicBoolean useKeyLockSet = new AtomicBoolean(false);
	/**
	 * 设置是否使用key锁，默认为true，仅限在刚new创建cache对象时候调用一次
	 *
	 * @param useKeyLock 是否使用key锁
	 * @return this
	 * @since 5.8.40
	 */
	public ReentrantCache<K, V> setUseKeyLock(boolean useKeyLock) {
		if (!useKeyLockSet.compareAndSet(false, true)) {//cas操作允许useKeyLock只被赋值一次
			throw new IllegalStateException("useKeyLock has already been set!");
		}
		this.useKeyLock = useKeyLock;
		return this;
	}

	@Override
	public void put(K key, V object, long timeout) {
		lock.lock();
		try {
			putWithoutLock(key, object, timeout);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean containsKey(K key) {
		return null != getOrRemoveExpired(key, false, false);
	}

	@Override
	public V get(K key, boolean isUpdateLastAccess) {
		return getOrRemoveExpired(key, isUpdateLastAccess, true);
	}

	@Override
	public V get(final K key, final boolean isUpdateLastAccess, final long timeout, final Func0<V> valueFactory) {
		if(useKeyLock){
			// 用户如果允许，则使用key锁，可以减少valueFactory对象创建造成的
			return super.get(key, isUpdateLastAccess, timeout, valueFactory);
		}

		V v = get(key, isUpdateLastAccess);

		// 对象不存在，则加锁创建
		if (null == v && null != valueFactory) {
			// 按照pr#1385提议，使用key锁可以避免对象创建等待问题，但是会带来循环锁问题，见：issue#4022
			// 因此此处依旧采用全局锁，在对象创建过程中，全局等待，避免循环锁依赖
			// 这样避免了循环锁，但是会存在一个缺点，即对象创建过程中，其它线程无法获得锁，从而无法使用缓存，因此需要考虑对象创建的耗时问题
			lock.lock();
			try {
				// 双重检查锁，防止在竞争锁的过程中已经有其它线程写入
				final CacheObj<K, V> co = getWithoutLock(key);
				if (null == co) {
					// supplier的创建是一个耗时过程，此处创建与全局锁无关，而与key锁相关，这样就保证每个key只创建一个value，且互斥
					v = valueFactory.callWithRuntimeException();
					put(key, v, timeout);
				}
			} finally {
				lock.unlock();
			}
		}
		return v;
	}

	@Override
	public Iterator<CacheObj<K, V>> cacheObjIterator() {
		CopiedIter<CacheObj<K, V>> copiedIterator;
		lock.lock();
		try {
			copiedIterator = CopiedIter.copyOf(cacheObjIter());
		} finally {
			lock.unlock();
		}
		return new CacheObjIterator<>(copiedIterator);
	}

	@Override
	public final int prune() {
		lock.lock();
		try {
			return pruneCache();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void remove(K key) {
		lock.lock();
		CacheObj<K, V> co;
		try {
			co = removeWithoutLock(key);
		} finally {
			lock.unlock();
		}
		if (null != co) {
			onRemove(co.key, co.obj);
		}
	}

	@Override
	public void clear() {
		lock.lock();
		try {
			// 获取所有键的副本
			Set<Mutable<K>> keys = new HashSet<>(cacheMap.keySet());
			for (Mutable<K> key : keys) {
				CacheObj<K, V> co = removeWithoutLock(key.get());
				if (co != null) {
					onRemove(co.key, co.obj); // 触发资源释放
				}
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public String toString() {
		lock.lock();
		try {
			return super.toString();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获得值或清除过期值
	 * @param key 键
	 * @param isUpdateLastAccess 是否更新最后访问时间
	 * @param isUpdateCount 是否更新计数器
	 * @return 值或null
	 */
	private V getOrRemoveExpired(final K key, final boolean isUpdateLastAccess, final boolean isUpdateCount) {
		CacheObj<K, V> co;
		lock.lock();
		try {
			co = getWithoutLock(key);
			if(null != co && co.isExpired()){
				//过期移除
				removeWithoutLock(key);
				onRemove(co.key, co.obj);
				co = null;
			}
		} finally {
			lock.unlock();
		}

		// 未命中
		if (null == co) {
			if(isUpdateCount){
				missCount.increment();
			}
			return null;
		}

		if(isUpdateCount){
			hitCount.increment();
		}
		return co.get(isUpdateLastAccess);
	}
}
