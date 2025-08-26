/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.core.cache.impl;

import cn.hutool.v7.core.collection.iter.CopiedIter;
import cn.hutool.v7.core.collection.set.SetUtil;
import cn.hutool.v7.core.func.SerSupplier;
import cn.hutool.v7.core.lang.mutable.Mutable;

import java.io.Serial;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用{@link Lock}保护的缓存，读写都使用悲观锁完成，主要避免某些Map无法使用读写锁的问题<br>
 * 例如使用了LinkedHashMap的缓存，由于get方法也会改变Map的结构，因此读写必须加互斥锁
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly
 */
public abstract class LockedCache<K, V> extends AbstractCache<K, V> {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 一些特殊缓存，例如使用了LinkedHashMap的缓存，由于get方法也会改变Map的结构，导致无法使用读写锁
	 */
	protected Lock lock = new ReentrantLock();

	@Override
	public void put(final K key, final V object, final long timeout) {
		lock.lock();
		try {
			putWithoutLock(key, object, timeout);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean containsKey(final K key) {
		return null != getOrRemoveExpired(key, false, false);
	}

	@Override
	public V get(final K key, final boolean isUpdateLastAccess) {
		return getOrRemoveExpired(key, isUpdateLastAccess, true);
	}

	@Override
	public V get(final K key, final boolean isUpdateLastAccess, final long timeout, final SerSupplier<V> valueFactory) {
		V v = get(key, isUpdateLastAccess);

		// 对象不存在，则加锁创建
		if (null == v && null != valueFactory) {
			// 按照pr#1385提议，使用key锁可以避免对象创建等待问题，但是会带来循环锁问题，见：issue#4022
			// 因此此处依旧采用全局锁，在对象创建过程中，全局等待，避免循环锁依赖
			// 这样避免了循环锁，但是会存在一个缺点，即对象创建过程中，其它线程无法获得锁，从而无法使用缓存，因此需要考虑对象创建的耗时问题
			lock.lock();
			try {
				// 双重检查锁，防止在竞争锁的过程中已经有其它线程写入
				final CacheObj<K, V> co = getOrRemoveExpiredWithoutLock(key);
				if (null == co) {
					// supplier的创建是一个耗时过程，此处创建与全局锁无关，而与key锁相关，这样就保证每个key只创建一个value，且互斥
					v = valueFactory.get();
					putWithoutLock(key, v, timeout);
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
	public void remove(final K key) {
		CacheObj<K, V> co;
		lock.lock();
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
			final Set<Mutable<K>> keys = SetUtil.of(cacheMap.keySet());
			CacheObj<K, V> co;
			for (final Mutable<K> key : keys) {
				co = removeWithoutLock(key.get());
				if (null != co) {
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
	 *
	 * @param key                键
	 * @param isUpdateLastAccess 是否更新最后访问时间
	 * @param isUpdateCount      是否更新计数器
	 * @return 值或null
	 */
	private V getOrRemoveExpired(final K key, final boolean isUpdateLastAccess, final boolean isUpdateCount) {
		CacheObj<K, V> co;
		lock.lock();
		try {
			co = getOrRemoveExpiredWithoutLock(key);
		} finally {
			lock.unlock();
		}

		// 未命中
		if (null == co) {
			if (isUpdateCount) {
				missCount.increment();
			}
			return null;
		}

		if (isUpdateCount) {
			hitCount.increment();
		}
		return co.get(isUpdateLastAccess);
	}
}
