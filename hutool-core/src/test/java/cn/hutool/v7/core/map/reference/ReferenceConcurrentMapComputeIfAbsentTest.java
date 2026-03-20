/*
 * Copyright (c) 2013-2026 Hutool Team.
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

package cn.hutool.v7.core.map.reference;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ReferenceConcurrentMapComputeIfAbsentTest {

	@Test
	public void testComputeIfAbsentConcurrency() throws InterruptedException {
		final WeakConcurrentMap<String, String> map = new WeakConcurrentMap<>();
		final int threadCount = 10000;
		final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		final CountDownLatch latch = new CountDownLatch(threadCount);
		final AtomicInteger computeCount = new AtomicInteger(0);

		// 多个线程同时调用 computeIfAbsent，使用相同的 key
		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					final String result = map.computeIfAbsent("sharedKey", key -> {
						computeCount.incrementAndGet();
						try {
							Thread.sleep(10);
						} catch (final InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						return "computedValue";
					});
					assertNotNull(result);
					assertEquals("computedValue", result);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(50, TimeUnit.SECONDS);
		executor.shutdown();
		assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));

		// 验证只有一个线程执行了计算函数
		assertEquals(1, computeCount.get());
		assertEquals("computedValue", map.get("sharedKey"));
	}

	@Test
	public void testComputeIfAbsentDifferentKeys() throws InterruptedException {
		final WeakConcurrentMap<Integer, String> map = new WeakConcurrentMap<>();
		final int threadCount = 20;
		final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		final CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			final int key = i;
			executor.submit(() -> {
				try {
					final String result = map.computeIfAbsent(key, k -> "value" + k);
					assertNotNull(result);
					assertEquals("value" + key, result);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(5, TimeUnit.SECONDS);
		executor.shutdown();
		assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));

		assertEquals(threadCount, map.size());
	}

	@Test
	public void testComputeIfAbsentWithNullValue() throws InterruptedException {
		final WeakConcurrentMap<String, String> map = new WeakConcurrentMap<>();
		final int threadCount = 5;
		final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		final CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					final String result = map.computeIfAbsent("nullKey", key -> null);
					assertNull(result);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(5, TimeUnit.SECONDS);
		executor.shutdown();
		assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));

		assertTrue(map.containsKey("nullKey"));
	}

	@Test
	public void testComputeIfAbsentMixedOperations() throws InterruptedException {
		final WeakConcurrentMap<String, Integer> map = new WeakConcurrentMap<>();
		final int threadCount = 10;
		final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		final CountDownLatch latch = new CountDownLatch(threadCount * 2);
		final AtomicInteger counter = new AtomicInteger(0);

		for (int i = 0; i < threadCount; i++) {
			final int threadId = i;

			executor.submit(() -> {
				try {
					final String key = "key" + (threadId % 5);
					final Integer result = map.computeIfAbsent(key, k -> counter.incrementAndGet());
					assertNotNull(result);
				} finally {
					latch.countDown();
				}
			});

			executor.submit(() -> {
				try {
					final String key = "key" + threadId;
					final Integer result = map.computeIfAbsent(key, k -> counter.incrementAndGet());
					assertNotNull(result);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(5, TimeUnit.SECONDS);
		executor.shutdown();
		assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));

		assertTrue(map.size() <= 10);
		assertTrue(counter.get() <= 10);
	}

	@Test
	public void testComputeIfAbsentHighContention() throws InterruptedException {
		final WeakConcurrentMap<String, Long> map = new WeakConcurrentMap<>();
		final int threadCount = 50;
		final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		final CountDownLatch latch = new CountDownLatch(threadCount);
		final AtomicInteger computeCalls = new AtomicInteger(0);

		final long startTime = System.currentTimeMillis();

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					final Long result = map.computeIfAbsent("highContentionKey", key -> {
						computeCalls.incrementAndGet();
						try {
							Thread.sleep(5);
						} catch (final InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						return System.nanoTime();
					});
					assertNotNull(result);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(10, TimeUnit.SECONDS);
		final long endTime = System.currentTimeMillis();

		executor.shutdown();
		assertTrue(executor.awaitTermination(2, TimeUnit.SECONDS));

		assertEquals(1, computeCalls.get());
		assertNotNull(map.get("highContentionKey"));
		assertTrue(endTime - startTime < 1000);
	}

	@Test
	public void testComputeIfAbsentExistingKey() throws InterruptedException {
		final WeakConcurrentMap<String, String> map = new WeakConcurrentMap<>();
		map.put("existingKey", "existingValue");

		final int threadCount = 10;
		final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		final CountDownLatch latch = new CountDownLatch(threadCount);
		final AtomicInteger computeCalls = new AtomicInteger(0);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					final String result = map.computeIfAbsent("existingKey", key -> {
						computeCalls.incrementAndGet();
						return "newValue";
					});
					assertEquals("existingValue", result);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(5, TimeUnit.SECONDS);
		executor.shutdown();
		assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));

		assertEquals(0, computeCalls.get());
		assertEquals("existingValue", map.get("existingKey"));
	}

	@Test
	public void testComputeIfAbsentExceptionHandling() throws InterruptedException {
		final WeakConcurrentMap<String, String> map = new WeakConcurrentMap<>();
		final int threadCount = 5;
		final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		final CountDownLatch latch = new CountDownLatch(threadCount);
		final AtomicInteger exceptionCount = new AtomicInteger(0);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					assertThrows(RuntimeException.class, () ->
						map.computeIfAbsent("errorKey", key -> {
							throw new RuntimeException("Test exception");
						})
					);
					exceptionCount.incrementAndGet();
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(5, TimeUnit.SECONDS);
		executor.shutdown();
		assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));

		assertEquals(threadCount, exceptionCount.get());
		assertFalse(map.containsKey("errorKey"));
	}
}
