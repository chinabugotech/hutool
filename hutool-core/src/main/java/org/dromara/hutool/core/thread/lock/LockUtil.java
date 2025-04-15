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

package org.dromara.hutool.core.thread.lock;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * 锁相关工具
 *
 * @author Looly
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
	public static ReentrantReadWriteLock createReadWriteLock(final boolean fair) {
		return new ReentrantReadWriteLock(fair);
	}

	// region ----- SegmentLock
	/**
	 * 创建分段锁（强引用），使用 ReentrantLock
	 *
	 * @param segments 分段数量，必须大于 0
	 * @return 分段锁实例
	 */
	public static SegmentLock<Lock> createSegmentLock(final int segments) {
		return SegmentLock.lock(segments);
	}

	/**
	 * 创建分段读写锁（强引用），使用 ReentrantReadWriteLock
	 *
	 * @param segments 分段数量，必须大于 0
	 * @return 分段读写锁实例
	 */
	public static SegmentLock<ReadWriteLock> createSegmentReadWriteLock(final int segments) {
		return SegmentLock.readWriteLock(segments);
	}

	/**
	 * 创建分段信号量（强引用）
	 *
	 * @param segments 分段数量，必须大于 0
	 * @param permits 每个信号量的许可数
	 * @return 分段信号量实例
	 */
	public static SegmentLock<Semaphore> createSegmentSemaphore(final int segments, final int permits) {
		return SegmentLock.semaphore(segments, permits);
	}

	/**
	 * 创建弱引用分段锁，使用 ReentrantLock，懒加载
	 *
	 * @param segments 分段数量，必须大于 0
	 * @return 弱引用分段锁实例
	 */
	public static SegmentLock<Lock> createLazySegmentLock(final int segments) {
		return SegmentLock.lazyWeakLock(segments);
	}

	/**
	 * 根据 key 获取分段锁（强引用）
	 *
	 * @param segments 分段数量，必须大于 0
	 * @param key 用于映射分段的 key
	 * @return 对应的 Lock 实例
	 */
	public static Lock getSegmentLock(final int segments, final Object key) {
		return SegmentLock.lock(segments).get(key);
	}

	/**
	 * 根据 key 获取分段读锁（强引用）
	 *
	 * @param segments 分段数量，必须大于 0
	 * @param key 用于映射分段的 key
	 * @return 对应的读锁实例
	 */
	public static Lock getSegmentReadLock(final int segments, final Object key) {
		return SegmentLock.readWriteLock(segments).get(key).readLock();
	}

	/**
	 * 根据 key 获取分段写锁（强引用）
	 *
	 * @param segments 分段数量，必须大于 0
	 * @param key 用于映射分段的 key
	 * @return 对应的写锁实例
	 */
	public static Lock getSegmentWriteLock(final int segments, final Object key) {
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
	public static Semaphore getSegmentSemaphore(final int segments, final int permits, final Object key) {
		return SegmentLock.semaphore(segments, permits).get(key);
	}

	/**
	 * 根据 key 获取弱引用分段锁，懒加载
	 *
	 * @param segments 分段数量，必须大于 0
	 * @param key 用于映射分段的 key
	 * @return 对应的 Lock 实例
	 */
	public static Lock getLazySegmentLock(final int segments, final Object key) {
		return SegmentLock.lazyWeakLock(segments).get(key);
	}
	// endregion

	/**
	 * 获取单例的无锁对象
	 *
	 * @return {@link NoLock}
	 */
	public static NoLock getNoLock(){
		return NO_LOCK;
	}
}
