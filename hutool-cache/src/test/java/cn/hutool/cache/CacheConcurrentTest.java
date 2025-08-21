package cn.hutool.cache;

import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.cache.impl.WeakCache;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import static org.junit.jupiter.api.Assertions.*;
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
	/**
	 * 测试嵌套get调用可能导致的死锁：https://github.com/chinabugotech/hutool/issues/4022
	 */
	@Test
	public  void nestedGetDeadlockTest() throws Exception {
		final Cache<String, String> cache = new LRUCache<>(100);

		final String key1 = "key1";
		final String key2 = "key2";

		ExecutorService executor = Executors.newFixedThreadPool(2);
		CountDownLatch latch = new CountDownLatch(2);
		// 线程1：key1 -> key2
		executor.submit(() -> {
			try {
				String result = cache.get(key1, new Func0<String>() {
					@Override
					public String call() throws Exception {
						Thread.sleep(100);
						return cache.get(key2, () -> "value2") + "-nested";
					}
				});
			} catch (Exception e) {
				System.err.println("线程1异常: " + e);
			}
			finally {
				latch.countDown();
			}
		});

		// 线程2：key2 -> key1
		executor.submit(() -> {
			try {
				String result = cache.get(key2, new Func0<String>() {
					@Override
					public String call() throws Exception {
						Thread.sleep(100);
						return cache.get(key1, () -> "value1") + "-nested";
					}
				});
			} catch (Exception e) {
				System.err.println("线程2异常: " + e);
			} finally {
				latch.countDown();
			}
		});

		// 设置超时检测死锁
		if (!latch.await(5, TimeUnit.SECONDS)) {
			System.out.println("检测到可能的死锁!");
		}
		executor.shutdown();

	}
}
