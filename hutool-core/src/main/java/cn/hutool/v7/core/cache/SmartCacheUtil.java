package cn.hutool.v7.core.cache;

import cn.hutool.v7.core.cache.impl.LRUCache;

/**
 * 智能缓存工具类
 *
 * @author Nic
 */
public class SmartCacheUtil {

	private SmartCacheUtil() {
		// 工具类，禁止实例化
	}

	/**
	 * 创建LRU智能缓存
	 */
	public static <K, V> SmartCache<K, V> newLRUSmartCache(int capacity) {
		return (SmartCache<K, V>) SmartCacheBuilder.of(CacheUtil.newLRUCache(capacity))
			.name("LRU-SmartCache")
			.build();
	}

	/**
	 * 创建LFU智能缓存
	 */
	public static <K, V> SmartCache<K, V> newLFUSmartCache(int capacity) {
		return (SmartCache<K, V>) SmartCacheBuilder.of(CacheUtil.newLFUCache(capacity))
			.name("LFU-SmartCache")
			.build();
	}

	/**
	 * 创建FIFO智能缓存
	 */
	public static <K, V> SmartCache<K, V> newFIFOSmartCache(int capacity) {
		return (SmartCache<K, V>) SmartCacheBuilder.of(CacheUtil.newFIFOCache(capacity))
			.name("FIFO-SmartCache")
			.build();
	}

	/**
	 * 创建带加载器的智能缓存
	 */
	public static <K, V> SmartCache<K, V> newSmartCache(
		Cache<K, V> cache,
		java.util.function.Function<K, V> loader) {

		return SmartCacheBuilder.of(cache)
			.cacheLoader(loader)
			.enableAsyncRefresh(true)
			.enableStats(true)
			.build();
	}

	/**
	 * 创建定时过期的智能缓存
	 */
	public static <K, V> SmartCache<K, V> newTimedSmartCache(
		int capacity,
		long timeout,
		java.util.function.Function<K, V> loader) {

		Cache<K, V> cache = new LRUCache<>(capacity, timeout) {
			@Override
			public boolean isFull() {
				return this.cacheMap.size() >= capacity;
			}
		};

		return SmartCacheBuilder.of(cache)
			.name("Timed-SmartCache")
			.cacheLoader(loader)
			.enableStats(true)
			.build();
	}

	/**
	 * 获取缓存的详细统计信息
	 */
	public static String getDetailedStats(SmartCache<?, ?> cache) {
		if (cache == null) {
			return "Cache is null";
		}

		try {
			CacheStats stats = cache.getStats();
			return String.format(
				"Cache: %s\n" +
					"  Size: %d / %d\n" +
					"  Hit Rate: %.2f%%\n" +
					"  Hits: %d\n" +
					"  Misses: %d\n" +
					"  Load Success: %d\n" +
					"  Load Failure: %d\n" +
					"  Avg Load Time: %.2fms\n" +
					"  Evictions: %d\n" +
					"  Runtime: %ds",
				cache.getName(),
				cache.size(),
				cache.capacity(),
				stats.getHitRate() * 100,
				stats.getHitCount(),
				stats.getMissCount(),
				stats.getLoadSuccessCount(),
				stats.getLoadFailureCount(),
				stats.getAverageLoadTime(),
				stats.getEvictionCount(),
				stats.getRuntimeSeconds()
			);
		} catch (Exception e) {
			return "Unable to get stats: " + e.getMessage();
		}
	}
}
