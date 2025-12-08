package cn.hutool.v7.core.cache;

import cn.hutool.v7.core.cache.smart.SmartCache;
import cn.hutool.v7.core.cache.smart.SmartCacheBuilder;
import cn.hutool.v7.core.cache.smart.SmartCacheUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 智能缓存基础功能测试
 */
@DisplayName("智能缓存基础功能测试")
public class SmartCacheBasicTest {

	private SmartCache<Object, Object> cache;
	private AtomicInteger loadCounter;

	@BeforeEach
	void setUp() {
		loadCounter = new AtomicInteger(0);

		cache = SmartCacheBuilder.of(CacheUtil.newLRUCache(10))
			.name("TestCache")
			.enableStats(true)
			.cacheLoader(key -> {
				loadCounter.incrementAndGet();
				// 模拟加载耗时
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				return "value_" + key;
			})
			.build();
	}

	@Test
	@DisplayName("测试基本put和get操作")
	void testBasicPutAndGet() {
		cache.put("key1", "value1");

		assertEquals("value1", cache.get("key1"));
	}

	@Test
	@DisplayName("测试缓存加载器")
	void testCacheLoader() {
		// 第一次获取，应该触发加载
		assertEquals("value_key2", cache.get("key2"));
		assertEquals(1, loadCounter.get());

		// 第二次获取，应该使用缓存，不会触发加载
		assertEquals("value_key2", cache.get("key2"));
		assertEquals(1, loadCounter.get()); // 计数器不变

		// 获取另一个键，应该再次触发加载
		assertEquals("value_key3", cache.get("key3"));
		assertEquals(2, loadCounter.get());
	}

	@Test
	@DisplayName("测试批量操作")
	void testBatchOperations() {
		// 批量放入
		Map<String, String> data = new HashMap<>();
		data.put("batch1", "value1");
		data.put("batch2", "value2");
		data.put("batch3", "value3");

		cache.putAll(data);

		// 批量获取
		Map<Object, Object> result = cache.getAll(Arrays.asList("batch1", "batch2", "batch3", "non_existent"));

		assertEquals(4, result.size());
		assertEquals("value1", result.get("batch1"));
		assertEquals("value2", result.get("batch2"));
		assertEquals("value3", result.get("batch3"));
		assertTrue(result.containsKey("non_existent"));
	}

	@Test
	@DisplayName("测试computeIfAbsent")
	void testComputeIfAbsent() {
		AtomicInteger computeCounter = new AtomicInteger(0);

		// 第一次计算
		String result1 = (String) cache.computeIfAbsent("compute1", key -> {
			computeCounter.incrementAndGet();
			return "computed_" + key;
		});

		assertEquals("computed_compute1", result1);
		assertEquals(1, computeCounter.get());

		// 第二次获取，应该使用缓存
		String result2 = (String) cache.computeIfAbsent("compute1", key -> {
			computeCounter.incrementAndGet();
			return "should_not_be_called";
		});

		assertEquals("computed_compute1", result2);
		assertEquals(1, computeCounter.get()); // 计数器不变

		// 测试不存在的键
		assertNull(cache.computeIfAbsent("nullKey", key -> null));
	}

	@Test
	@DisplayName("测试缓存预热")
	void testWarmUp() {
		// 清除初始状态
		cache.clear();

		// 预热
		int warmed = cache.warmUp(Arrays.asList("warm1", "warm2", "warm3"));

		assertEquals(3, warmed);
		assertEquals(3, cache.size());

		// 验证预热的内容
		assertEquals("value_warm1", cache.get("warm1"));
		assertEquals("value_warm2", cache.get("warm2"));
		assertEquals("value_warm3", cache.get("warm3"));

		// 预热已存在的键，应该不会重复加载
		int alreadyWarmed = cache.warmUp(Arrays.asList("warm1", "warm4"));
		assertEquals(1, alreadyWarmed); // 只有warm4是新加载的
	}

	@Test
	@DisplayName("测试缓存容量和大小")
	void testCapacityAndSize() {
		SmartCache<String, String> smallCache = SmartCacheUtil.newLRUSmartCache(3);

		smallCache.put("1", "a");
		smallCache.put("2", "b");
		smallCache.put("3", "c");

		assertEquals(3, smallCache.size());
		assertEquals(3, smallCache.capacity());

		// 超过容量，应该触发淘汰
		smallCache.put("4", "d");

		// 由于是LRU，第一个元素可能被淘汰
		assertTrue(smallCache.size() <= 3);
	}
}
