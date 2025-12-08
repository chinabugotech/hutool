package cn.hutool.v7.core.cache;

import cn.hutool.v7.core.cache.impl.SmartCacheImpl;
import cn.hutool.v7.core.text.StrUtil;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * 智能缓存构建器
 *
 * @author Nic
 */
public class SmartCacheBuilder<K, V> {

	// 必需参数
	private final Cache<K, V> cache;

	// 可选参数
	private String name = "SmartCache";
	private boolean enableStats = true;
	private boolean enableAsyncRefresh = false;
	private int warmUpBatchSize = 100;
	private Duration refreshTimeout = Duration.ofSeconds(30);
	private ExecutorService refreshExecutor;
	private Function<K, V> cacheLoader;

	/**
	 * 私有构造器
	 */
	private SmartCacheBuilder(Cache<K, V> cache) {
		this.cache = cache;
	}

	/**
	 * 创建构建器
	 */
	public static <K, V> SmartCacheBuilder<K, V> of(Cache<K, V> cache) {
		return new SmartCacheBuilder<>(cache);
	}

	/**
	 * 设置缓存名称
	 */
	public SmartCacheBuilder<K, V> name(String name) {
		this.name = StrUtil.defaultIfBlank(name, "SmartCache");
		return this;
	}

	/**
	 * 启用统计
	 */
	public SmartCacheBuilder<K, V> enableStats(boolean enableStats) {
		this.enableStats = enableStats;
		return this;
	}

	/**
	 * 启用异步刷新
	 */
	public SmartCacheBuilder<K, V> enableAsyncRefresh(boolean enableAsyncRefresh) {
		this.enableAsyncRefresh = enableAsyncRefresh;
		return this;
	}

	/**
	 * 设置预热批次大小
	 */
	public SmartCacheBuilder<K, V> warmUpBatchSize(int warmUpBatchSize) {
		this.warmUpBatchSize = Math.max(1, warmUpBatchSize);
		return this;
	}

	/**
	 * 设置刷新超时时间
	 */
	public SmartCacheBuilder<K, V> refreshTimeout(Duration refreshTimeout) {
		this.refreshTimeout = refreshTimeout;
		return this;
	}

	/**
	 * 设置刷新线程池
	 */
	public SmartCacheBuilder<K, V> refreshExecutor(ExecutorService refreshExecutor) {
		this.refreshExecutor = refreshExecutor;
		return this;
	}

	/**
	 * 设置缓存加载器
	 */
	public SmartCacheBuilder<K, V> cacheLoader(Function<K, V> cacheLoader) {
		this.cacheLoader = cacheLoader;
		return this;
	}

	/**
	 * 构建智能缓存
	 */
	public SmartCache<K, V> build() {
		// 确保有刷新线程池（如果需要异步刷新）
		if (enableAsyncRefresh && refreshExecutor == null) {
			refreshExecutor = Executors.newFixedThreadPool(
				Math.max(2, Runtime.getRuntime().availableProcessors() / 2)
			);
		}

		return new SmartCacheImpl<>(
			cache,
			name,
			enableStats,
			enableAsyncRefresh,
			warmUpBatchSize,
			refreshTimeout,
			refreshExecutor,
			cacheLoader
		);
	}
}
