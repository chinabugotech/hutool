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

package cn.hutool.v7.core.cache;

import cn.hutool.v7.core.cache.impl.FIFOCache;
import cn.hutool.v7.core.cache.impl.LRUCache;
import cn.hutool.v7.core.cache.impl.WeakCache;
import cn.hutool.v7.core.exception.HutoolException;
import cn.hutool.v7.core.func.SerSupplier;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.thread.ConcurrencyTester;
import cn.hutool.v7.core.thread.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 缓存单元测试
 *
 * @author Looly
 *
 */
public class CacheConcurrentTest {

	@Test
	@Disabled
	public void fifoCacheTest() {
		final int threadCount = 4000;
		final Cache<String, String> cache = new FIFOCache<>(3);

		// 由于缓存容量只有3，当加入第四个元素的时候，根据FIFO规则，最先放入的对象将被移除

		for (int i = 0; i < threadCount; i++) {
			ThreadUtil.execute(() -> {
				cache.put("key1", "value1", System.currentTimeMillis() * 3);
				cache.put("key2", "value2", System.currentTimeMillis() * 3);
				cache.put("key3", "value3", System.currentTimeMillis() * 3);
				cache.put("key4", "value4", System.currentTimeMillis() * 3);
				ThreadUtil.sleep(1000);
				cache.put("key5", "value5", System.currentTimeMillis() * 3);
				cache.put("key6", "value6", System.currentTimeMillis() * 3);
				cache.put("key7", "value7", System.currentTimeMillis() * 3);
				cache.put("key8", "value8", System.currentTimeMillis() * 3);
				Console.log("put all");
			});
		}

		for (int i = 0; i < threadCount; i++) {
			ThreadUtil.execute(() -> show(cache));
		}

		System.out.println("==============================");
		ThreadUtil.sleep(10000);
	}

	@Test
	@Disabled
	public void lruCacheTest() {
		final int threadCount = 40000;
		final Cache<String, String> cache = new LRUCache<>(1000);

		for (int i = 0; i < threadCount; i++) {
			final int index = i;
			ThreadUtil.execute(() -> {
				cache.put("key1"+ index, "value1");
				cache.put("key2"+ index, "value2", System.currentTimeMillis() * 3);

				int size = cache.size();
				int capacity = cache.capacity();
				if(size > capacity) {
					Console.log("{} {}", size, capacity);
				}
				ThreadUtil.sleep(1000);
				size = cache.size();
				capacity = cache.capacity();
				if(size > capacity) {
					Console.log("## {} {}", size, capacity);
				}
			});
		}

		ThreadUtil.sleep(5000);
	}

	private void show(final Cache<String, String> cache) {

		for (final Object tt : cache) {
			Console.log(tt);
		}
	}

	@SuppressWarnings("resource")
	@Test
	public void effectiveTest() {
		// 模拟耗时操作消耗时间
		final int delay = 2000;
		final AtomicInteger ai = new AtomicInteger(0);
		final WeakCache<Integer, Integer> weakCache = new WeakCache<>(60 * 1000);
		final ConcurrencyTester concurrencyTester = ThreadUtil.concurrencyTest(32, () -> {
			final int i = ai.incrementAndGet() % 4;
			weakCache.get(i, () -> {
				ThreadUtil.sleep(delay);
				return i;
			});
		});
		final long interval = concurrencyTester.getInterval();
		// 总耗时应与单次操作耗时在同一个数量级
		Assertions.assertTrue(interval < delay * 2);
	}

	@Test
	void issue4022Test() throws InterruptedException {
		final Cache<String, String> cache = new LRUCache<>(100);

		final String key1 = "key1";
		final String key2 = "key2";

		final CountDownLatch latch = new CountDownLatch(2);
		// 线程1：key1 -> key2
		ThreadUtil.execute(() -> {
			try {
				final String result = cache.get(key1, () -> {
					Thread.sleep(100);
					return cache.get(key2, () -> "value2") + "-nested";
				});
			}
			finally {
				latch.countDown();
			}
		});

		// 线程2：key2 -> key1
		ThreadUtil.execute(() -> {
			try {
				final String result = cache.get(key2, () -> {
					Thread.sleep(100);
					return cache.get(key1, () -> "value1") + "-nested";
				});
			} finally {
				latch.countDown();
			}
		});

		// 设置超时检测死锁
		if (!latch.await(5, TimeUnit.SECONDS)) {
			throw new HutoolException("检测到可能的死锁!");
		}
	}
}
