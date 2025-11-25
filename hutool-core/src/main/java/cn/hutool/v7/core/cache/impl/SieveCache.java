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

import cn.hutool.v7.core.lang.mutable.Mutable;
import cn.hutool.v7.core.lang.mutable.MutableObj;

import java.io.Serial;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SIEVE 缓存算法实现<br>
 * <p>
 * SIEVE 是一种比 LRU 更简单且通常更高效的缓存算法。<br>
 * 核心特性：<br>
 * 缓存命中时，仅将节点的 {@code visited} 标记设为 true，不移动节点位置。<br>
 * 淘汰时，使用 {@code hand} 指针从尾部扫描，淘汰 {@code visited=false} 的节点。<br>
 * 新加入节点 {@code visited = false} 且置于头部，Hand 指针扫描时会优先淘汰它，提供抗扫描能力。<br>
 * </p>
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Lettuceleaves
 */
public class SieveCache<K, V> extends LockedCache<K, V> {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 双向链表头节点
	 */
	private SieveCacheObj<K, V> head;
	/**
	 * 双向链表尾节点
	 */
	private SieveCacheObj<K, V> tail;
	/**
	 * 下一次扫描的起始位置
	 */
	private SieveCacheObj<K, V> hand;

	/**
	 * 构造<br>
	 * 默认无超时
	 *
	 * @param capacity 容量
	 */
	public SieveCache(int capacity) {
		this(capacity, 0);
	}

	/**
	 * 构造
	 *
	 * @param capacity 容量
	 * @param timeout  默认超时时间，单位：毫秒
	 */
	public SieveCache(int capacity, long timeout) {
		if (Integer.MAX_VALUE == capacity) {
			capacity -= 1;
		}

		this.capacity = capacity;
		this.timeout = timeout;

		// 这里的设置 capacity + 1, 1.0f 避免触发扩容
		this.cacheMap = new HashMap<>(capacity + 1, 1.0f);
		this.lock = new ReentrantLock();
	}

	@Override
	protected void putWithoutLock(K key, V object, long timeout) {
		final Mutable<K> keyObj = MutableObj.of(key);
		SieveCacheObj<K, V> co = (SieveCacheObj<K, V>) cacheMap.get(keyObj);

		if (co != null) {
			final SieveCacheObj<K, V> newCo = new SieveCacheObj<>(key, object, timeout);

			// 新加入的节点，默认刚访问过，防止立刻被淘汰
			newCo.visited = true;

			// 替换旧节点
			replaceNode(co, newCo);
			cacheMap.put(keyObj, newCo);
		} else {
			co = new SieveCacheObj<>(key, object, timeout);
			cacheMap.put(keyObj, co);
			addToHead(co);
			co.visited = false;

			if (cacheMap.size() > capacity) {
				pruneCache();
			}
		}
	}

	/**
	 * 在双向链表中用 newNode 替换 oldNode，保持链表结构不变
	 *
	 * @param oldNode 旧节点
	 * @param newNode 新节点
	 */
	private void replaceNode(SieveCacheObj<K, V> oldNode, SieveCacheObj<K, V> newNode) {
		newNode.prev = oldNode.prev;
		newNode.next = oldNode.next;

		// 更新前向指针
		if (oldNode.prev != null) {
			oldNode.prev.next = newNode;
		} else {
			head = newNode;
		}

		// 更新后向指针
		if (oldNode.next != null) {
			oldNode.next.prev = newNode;
		} else {
			tail = newNode;
		}

		// 将hand转移至新节点，防止扫描时淘汰热点数据
		if (hand == oldNode) {
			hand = newNode;
		}

		oldNode.prev = null;
		oldNode.next = null;
	}

	@Override
	protected CacheObj<K, V> getOrRemoveExpiredWithoutLock(K key) {
		final Mutable<K> keyObj = MutableObj.of(key);
		final SieveCacheObj<K, V> co = (SieveCacheObj<K, V>) cacheMap.get(keyObj);

		if (null != co) {
			if (co.isExpired()) {
				removeWithoutLock(key);
				return null;
			}
			co.visited = true;
			co.lastAccess = System.currentTimeMillis();
		}
		return co;
	}

	@Override
	protected CacheObj<K, V> removeWithoutLock(K key) {
		final Mutable<K> keyObj = MutableObj.of(key);
		final SieveCacheObj<K, V> co = (SieveCacheObj<K, V>) cacheMap.remove(keyObj);
		if (co != null) {
			removeNode(co);
		}
		return co;
	}

	/**
	 * 优先清理过期对象，如果容量仍溢出，反向扫描visited为false的节点，设置true节点为false
	 */
	@Override
	protected int pruneCache() {
		int count = 0;

		if (isPruneExpiredActive()) {
			final Iterator<CacheObj<K, V>> values = cacheObjIter();
			CacheObj<K, V> co;
			while (values.hasNext()) {
				co = values.next();
				if (co.isExpired()) {
					values.remove();
					removeNode((SieveCacheObj<K, V>) co);
					onRemove(co.key, co.obj);
					count++;
				}
			}
		}

		if (cacheMap.size() > capacity) {
			if (hand == null) {
				hand = tail;
			}

			while (cacheMap.size() > capacity) {
				if (hand == null) {
					hand = tail;
				}

				if (!hand.visited) {
					final SieveCacheObj<K, V> victim = hand;
					hand = hand.prev;

					final Mutable<K> keyObj = MutableObj.of(victim.key);
					cacheMap.remove(keyObj);
					removeNode(victim);
					onRemove(victim.key, victim.obj);
					count++;
				} else {
					hand.visited = false;
					hand = hand.prev;
				}
			}
		}
		return count;
	}

	/**
	 * 将节点加入链表头部
	 *
	 * @param node 节点
	 */
	private void addToHead(SieveCacheObj<K, V> node) {
		node.next = head;
		node.prev = null;
		if (head != null) {
			head.prev = node;
		}
		head = node;
		if (tail == null) {
			tail = node;
		}
	}

	/**
	 * 从链表中移除节点
	 *
	 * @param node 节点
	 */
	private void removeNode(SieveCacheObj<K, V> node) {
		if (node == hand) {
			hand = node.prev;
		}

		if (node.prev != null) {
			node.prev.next = node.next;
		} else {
			head = node.next;
		}

		if (node.next != null) {
			node.next.prev = node.prev;
		} else {
			tail = node.prev;
		}

		node.next = null;
		node.prev = null;
	}

	/**
	 * 给节点添加visited属性，用于Sieve缓存淘汰策略
	 */
	private static class SieveCacheObj<K, V> extends CacheObj<K, V> {
		@Serial
		private static final long serialVersionUID = 1L;
		/**
		 * 是否被访问过
		 */
		boolean visited = false;
		/**
		 * 前向节点
		 */
		SieveCacheObj<K, V> prev;
		/**
		 * 后向节点
		 */
		SieveCacheObj<K, V> next;

		protected SieveCacheObj(final K key, final V obj, final long ttl) {
			super(key, obj, ttl);
		}
	}
}
