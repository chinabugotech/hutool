package cn.hutool.v7.core.cache;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 缓存统计信息
 *
 * @author Nic
 */
public class CacheStats implements Serializable {
	private static final long serialVersionUID = 1L;

	private final LongAdder hitCount;
	private final LongAdder missCount;
	private final LongAdder loadSuccessCount;
	private final LongAdder loadFailureCount;
	private final LongAdder evictionCount;
	private final LongAdder totalLoadTime; // 纳秒
	private final AtomicLong cacheSize;
	private final long startTime;

	/**
	 * 构建器模式
	 */
	public static class Builder {
		private final CacheStats stats;

		public Builder() {
			this.stats = new CacheStats();
		}

		public Builder hitCount(long hitCount) {
			stats.hitCount.add(hitCount);
			return this;
		}

		public Builder missCount(long missCount) {
			stats.missCount.add(missCount);
			return this;
		}

		public Builder loadSuccessCount(long loadSuccessCount) {
			stats.loadSuccessCount.add(loadSuccessCount);
			return this;
		}

		public Builder loadFailureCount(long loadFailureCount) {
			stats.loadFailureCount.add(loadFailureCount);
			return this;
		}

		public Builder evictionCount(long evictionCount) {
			stats.evictionCount.add(evictionCount);
			return this;
		}

		public Builder totalLoadTime(long totalLoadTime) {
			stats.totalLoadTime.add(totalLoadTime);
			return this;
		}

		public Builder cacheSize(long cacheSize) {
			stats.cacheSize.set(cacheSize);
			return this;
		}

		public CacheStats build() {
			return stats;
		}
	}

	public CacheStats() {
		this.hitCount = new LongAdder();
		this.missCount = new LongAdder();
		this.loadSuccessCount = new LongAdder();
		this.loadFailureCount = new LongAdder();
		this.evictionCount = new LongAdder();
		this.totalLoadTime = new LongAdder();
		this.cacheSize = new AtomicLong(0);
		this.startTime = System.currentTimeMillis();
	}

	// ========== 统计计算方法 ==========

	/**
	 * 获取命中率
	 */
	public double getHitRate() {
		long requestCount = hitCount.longValue() + missCount.longValue();
		return requestCount == 0 ? 1.0 : (double) hitCount.longValue() / requestCount;
	}

	/**
	 * 获取未命中率
	 */
	public double getMissRate() {
		long requestCount = hitCount.longValue() + missCount.longValue();
		return requestCount == 0 ? 0.0 : (double) missCount.longValue() / requestCount;
	}

	/**
	 * 获取平均加载时间（毫秒）
	 */
	public double getAverageLoadTime() {
		long total = totalLoadTime.longValue();
		long success = loadSuccessCount.longValue();
		return success == 0 ? 0.0 : (total / 1_000_000.0) / success;
	}

	/**
	 * 获取加载失败率
	 */
	public double getLoadFailureRate() {
		long totalLoads = loadSuccessCount.longValue() + loadFailureCount.longValue();
		return totalLoads == 0 ? 0.0 : (double) loadFailureCount.longValue() / totalLoads;
	}

	/**
	 * 获取缓存运行时间（秒）
	 */
	public long getRuntimeSeconds() {
		return (System.currentTimeMillis() - startTime) / 1000;
	}

	// ========== Getter方法 ==========

	public long getHitCount() {
		return hitCount.longValue();
	}

	public long getMissCount() {
		return missCount.longValue();
	}

	public long getLoadSuccessCount() {
		return loadSuccessCount.longValue();
	}

	public long getLoadFailureCount() {
		return loadFailureCount.longValue();
	}

	public long getEvictionCount() {
		return evictionCount.longValue();
	}

	public long getTotalLoadTime() {
		return totalLoadTime.longValue();
	}

	public long getCacheSize() {
		return cacheSize.get();
	}

	public long getStartTime() {
		return startTime;
	}

	/**
	 * 记录一次命中
	 */
	public void recordHit() {
		hitCount.increment();
	}

	/**
	 * 记录一次未命中
	 */
	public void recordMiss() {
		missCount.increment();
	}

	/**
	 * 记录一次成功的加载
	 */
	public void recordLoadSuccess(long loadTime) {
		loadSuccessCount.increment();
		totalLoadTime.add(loadTime);
	}

	/**
	 * 记录一次失败的加载
	 */
	public void recordLoadFailure() {
		loadFailureCount.increment();
	}

	/**
	 * 记录一次驱逐
	 */
	public void recordEviction() {
		evictionCount.increment();
	}

	/**
	 * 更新缓存大小
	 */
	public void setCacheSize(long size) {
		cacheSize.set(size);
	}

	@Override
	public String toString() {
		return String.format(
			"CacheStats{hitRate=%.2f%%, hits=%d, misses=%d, loadSuccess=%d, loadFailure=%d, " +
				"evictions=%d, avgLoadTime=%.2fms, size=%d, runtime=%ds}",
			getHitRate() * 100, getHitCount(), getMissCount(), getLoadSuccessCount(),
			getLoadFailureCount(), getEvictionCount(), getAverageLoadTime(),
			getCacheSize(), getRuntimeSeconds()
		);
	}
}
