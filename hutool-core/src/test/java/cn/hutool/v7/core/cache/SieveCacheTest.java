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

import cn.hutool.v7.core.cache.impl.SieveCache;
import cn.hutool.v7.core.thread.ThreadUtil;
import cn.hutool.v7.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SIEVE 缓存算法单元测试
 */
public class SieveCacheTest {

	@Test
	public void evictionLogicTest() {
		SieveCache<String, String> cache = new SieveCache<>(3);

		cache.put("A", "A");
		cache.put("B", "B");
		cache.put("C", "C");

		cache.get("A");

		cache.put("D", "D");

		Assertions.assertEquals("A", cache.get("A"));
		Assertions.assertEquals("C", cache.get("C"));
		Assertions.assertEquals("D", cache.get("D"));

		Assertions.assertNull(cache.get("B"), "B 应该被淘汰，因为它是未访问过的节点");
	}


	@Test
	public void expiryTest() {
		SieveCache<String, String> cache = new SieveCache<>(3);
		cache.put("k1", "v1", 100);
		cache.put("k2", "v2", 10000);

		ThreadUtil.sleep(200);

		Assertions.assertNull(cache.get("k1"), "k1 应该过期");
		Assertions.assertEquals("v2", cache.get("k2"), "k2 应该存在");
		Assertions.assertEquals(1, cache.size(), "size 应该为 1");
	}

	@Test
	public void listenerTest() {
		final AtomicInteger removeCount = new AtomicInteger();
		SieveCache<Integer, Integer> cache = new SieveCache<>(2);

		cache.setListener((key, value) -> {
			removeCount.incrementAndGet();
		});

		cache.put(1, 1);
		cache.put(2, 2);
		cache.put(3, 3);

		Assertions.assertEquals(1, removeCount.get());
	}

	@Test
	public void concurrencyPressureTest() throws InterruptedException {
		int threadCount = 20;
		int loopCount = 2000;
		int capacity = 100;

		final SieveCache<String, String> cache = new SieveCache<>(capacity);
		final CountDownLatch latch = new CountDownLatch(threadCount);
		final AtomicInteger errorCount = new AtomicInteger(0);

		for (int i = 0; i < threadCount; i++) {
			new Thread(() -> {
				try {
					for (int j = 0; j < loopCount; j++) {
						String key = String.valueOf(RandomUtil.randomInt(0, 1000));
						if (RandomUtil.randomBoolean()) {
							cache.put(key, "val-" + key);
						} else {
							cache.get(key);
						}
					}
				} catch (Exception e) {
					errorCount.incrementAndGet();
				} finally {
					latch.countDown();
				}
			}).start();
		}

		latch.await();

		Assertions.assertEquals(0, errorCount.get(), "并发执行不应出现异常");
		Assertions.assertTrue(cache.size() <= capacity, "缓存大小不应超过容量");

		int iteratorCount = 0;
		for (String ignored : cache) {
			iteratorCount++;
		}
		Assertions.assertEquals(cache.size(), iteratorCount, "迭代器数量与 size() 应一致");
	}

	/**
	 * 抗扫描能力测试
	 * 如果扫描数据量过大（如 50% 容量）且热点数据无访问，热点数据的保护位会被耗尽，因此这里仅模拟少量数据的扫描攻击。
	 */
	@Test
	public void scanResistanceTest() {
		int capacity = 10;
		SieveCache<Integer, Integer> cache = new SieveCache<>(capacity);

		// 填满热点数据
		for (int i = 0; i < capacity; i++) {
			cache.put(i, i);
		}

		// 模拟热点访问
		for (int i = 0; i < capacity; i++) {
			cache.get(i);
		}

		// 插入 1 个冷数据
		cache.put(10, 10);

		int retainedHotItems = 0;
		for (int i = 0; i < capacity; i++) {
			if (cache.get(i) != null) {
				retainedHotItems++;
			}
		}

		Assertions.assertNull(cache.get(10), "冷数据 (10) 应该被淘汰");
		Assertions.assertEquals(capacity, retainedHotItems, "所有热点数据 (0-9) 应该被保留");
	}
}
