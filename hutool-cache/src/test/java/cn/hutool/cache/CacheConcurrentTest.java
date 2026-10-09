package cn.hutool.cache;

import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.cache.impl.WeakCache;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 缓存单元测试
 *
 * @author looly
 *
 */
public class CacheConcurrentTest {

	@Test
	@Disabled
	public void fifoCacheTest() {
		int threadCount = 4000;
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
		int threadCount = 40000;
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

	private void show(Cache<String, String> cache) {

		for (Object tt : cache) {
			Console.log(tt);
		}
	}

	@Test
	public void reentrantCacheShouldReturnValueAfterDoubleCheck() throws Exception {
		final String key = "key";
		final CountDownLatch initialMiss = new CountDownLatch(1);
		final CountDownLatch continueDoubleCheck = new CountDownLatch(1);
		final AtomicInteger factoryCount = new AtomicInteger();
		final AtomicReference<Thread> delayedThread = new AtomicReference<>();
		final AtomicReference<String> delayedValue = new AtomicReference<>();
		final LRUCache<String, String> cache = new LRUCache<String, String>(2) {
			@Override
			public String get(String key, boolean isUpdateLastAccess) {
				final String value = super.get(key, isUpdateLastAccess);
				if (Thread.currentThread() == delayedThread.get() && null == value) {
					initialMiss.countDown();
					try {
						continueDoubleCheck.await(5, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						throw new AssertionError(e);
					}
				}
				return value;
			}
		};
		final Thread delayed = new Thread(() -> delayedValue.set(cache.get(key, false, () -> "delayed")));
		delayedThread.set(delayed);
		delayed.start();

		assertTrue(initialMiss.await(5, TimeUnit.SECONDS));

		final Thread winner = new Thread(() -> cache.get(key, false, () -> {
			factoryCount.incrementAndGet();
			return "winner";
		}));
		winner.start();
		winner.join(5000);
		assertFalse(winner.isAlive());

		continueDoubleCheck.countDown();
		delayed.join(5000);
		assertFalse(delayed.isAlive());
		assertEquals("winner", delayedValue.get());
		assertEquals(1, factoryCount.get());
	}

	@Test
	@Disabled
	public void effectiveTest() {
		// 模拟耗时操作消耗时间
		int delay = 2000;
		AtomicInteger ai = new AtomicInteger(0);
		WeakCache<Integer, Integer> weakCache = new WeakCache<>(60 * 1000);
		ConcurrencyTester concurrencyTester = ThreadUtil.concurrencyTest(32, () -> {
			int i = ai.incrementAndGet() % 4;
			weakCache.get(i, () -> {
				ThreadUtil.sleep(delay);
				return i;
			});
		});
		long interval = concurrencyTester.getInterval();
		// 总耗时应与单次操作耗时在同一个数量级
		assertTrue(interval < delay * 2);
	}
}
