package cn.hutool.v7.core.cache.impl;

import cn.hutool.v7.core.cache.Cache;
import cn.hutool.v7.core.cache.CacheStats;
import cn.hutool.v7.core.cache.SmartCache;
import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.collection.iter.CopiedIter;
import cn.hutool.v7.core.collection.partition.Partition;
import cn.hutool.v7.core.func.SerSupplier;
import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.core.text.StrUtil;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * 智能缓存实现
 *
 * @author Nic
 */
public class SmartCacheImpl<K, V> implements SmartCache<K, V> {

	// 底层缓存
	private final Cache<K, V> delegate;

	// 配置参数
	private String name;
	private final boolean enableStats;
	private final boolean enableAsyncRefresh;
	private final int warmUpBatchSize;
	private final Duration refreshTimeout;
	private final ExecutorService refreshExecutor;
	private final Function<K, V> cacheLoader;

	// 统计信息
	private final CacheStats stats;

	// 锁机制
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final Map<K, CompletableFuture<V>> pendingRefreshes = new ConcurrentHashMap<>();

	/**
	 * 构造器
	 */
	public SmartCacheImpl(
		Cache<K, V> delegate,
		String name,
		boolean enableStats,
		boolean enableAsyncRefresh,
		int warmUpBatchSize,
		Duration refreshTimeout,
		ExecutorService refreshExecutor,
		Function<K, V> cacheLoader) {

		this.delegate = delegate;
		this.name = name;
		this.enableStats = enableStats;
		this.enableAsyncRefresh = enableAsyncRefresh;
		this.warmUpBatchSize = Math.max(1, warmUpBatchSize);
		this.refreshTimeout = refreshTimeout != null ? refreshTimeout : Duration.ofSeconds(30);
		this.refreshExecutor = refreshExecutor;
		this.cacheLoader = cacheLoader;
		this.stats = enableStats ? new CacheStats() : null;
	}

	// ========== 实现Cache接口方法 ==========

	@Override
	public void put(K key, V object, long timeout) {
		lock.writeLock().lock();
		try {
			delegate.put(key, object, timeout);
			if (enableStats) {
				stats.setCacheSize(delegate.size());
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void put(K key, V object) {
		put(key, object, 0);
	}

	@Override
	public V get(K key, boolean isUpdateLastAccess) {
		lock.readLock().lock();
		try {
			V value = delegate.get(key, isUpdateLastAccess);

			if (enableStats) {
				if (value != null) {
					stats.recordHit();
				} else {
					stats.recordMiss();

					// 如果有缓存加载器，尝试加载
					if (cacheLoader != null) {
						long startTime = System.nanoTime();
						try {
							value = cacheLoader.apply(key);
							if (value != null) {
								delegate.put(key, value);
								stats.recordLoadSuccess(System.nanoTime() - startTime);
							}
						} catch (Exception e) {
							stats.recordLoadFailure();
							throw new CacheException("Failed to load cache value for key: " + key, e);
						}
					}
				}
			}

			return value;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V get(K key) {
		return get(key, false);
	}

	@Override
	public Iterator<V> iterator() {
		return delegate.iterator();
	}

	@Override
	public int prune() {
		lock.writeLock().lock();
		try {
			int pruned = delegate.prune();
			if (enableStats && pruned > 0) {
				for (int i = 0; i < pruned; i++) {
					stats.recordEviction();
				}
				stats.setCacheSize(delegate.size());
			}
			return pruned;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public boolean isFull() {
		lock.readLock().lock();
		try {
			return delegate.isFull();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void remove(K key) {
		lock.writeLock().lock();
		try {
			delegate.remove(key);
			if (enableStats) {
				stats.setCacheSize(delegate.size());
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void clear() {
		lock.writeLock().lock();
		try {
			delegate.clear();
			if (enableStats) {
				stats.setCacheSize(0);
			}
			pendingRefreshes.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public int capacity() {
		lock.readLock().lock();
		try {
			return delegate.capacity();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public long timeout() {
		lock.readLock().lock();
		try {
			return delegate.timeout();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		lock.readLock().lock();
		try {
			return delegate.isEmpty();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public int size() {
		lock.readLock().lock();
		try {
			return delegate.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsKey(K key) {
		lock.readLock().lock();
		try {
			return delegate.containsKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	// ========== 实现SmartCache接口方法 ==========

	@Override
	public Map<K, V> getAll(Collection<K> keys) {
		if (CollUtil.isEmpty(keys)) {
			return Collections.emptyMap();
		}

		lock.readLock().lock();
		try {
			Map<K, V> result = new HashMap<>(keys.size());

			for (K key : keys) {
				V value = get(key);
				if (value != null) {
					result.put(key, value);
				}
			}

			return result;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		if (MapUtil.isEmpty(map)) {
			return;
		}

		lock.writeLock().lock();
		try {
			for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
				delegate.put(entry.getKey(), entry.getValue());
			}

			if (enableStats) {
				stats.setCacheSize(delegate.size());
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public CompletableFuture<V> refreshAsync(K key) {
		if (!enableAsyncRefresh) {
			throw new UnsupportedOperationException("Async refresh is not enabled");
		}

		if (cacheLoader == null) {
			throw new IllegalStateException("Cache loader is required for async refresh");
		}

		// 检查是否已经有正在进行的刷新
		CompletableFuture<V> pending = pendingRefreshes.get(key);
		if (pending != null) {
			return pending;
		}

		CompletableFuture<V> future = CompletableFuture.supplyAsync(() -> {
			try {
				long startTime = System.nanoTime();
				V newValue = cacheLoader.apply(key);

				if (newValue != null) {
					lock.writeLock().lock();
					try {
						delegate.put(key, newValue);
						if (enableStats) {
							stats.recordLoadSuccess(System.nanoTime() - startTime);
						}
					} finally {
						lock.writeLock().unlock();
					}
				}

				return newValue;
			} catch (Exception e) {
				if (enableStats) {
					stats.recordLoadFailure();
				}
				throw new CompletionException(e);
			} finally {
				pendingRefreshes.remove(key);
			}
		}, refreshExecutor);

		// 设置超时
		future = future.orTimeout(refreshTimeout.toMillis(), TimeUnit.MILLISECONDS)
			.exceptionally(ex -> {
				pendingRefreshes.remove(key);
				return null;
			});

		pendingRefreshes.put(key, future);
		return future;
	}

	@Override
	public int warmUp(Collection<K> keys) {
		if (cacheLoader == null || CollUtil.isEmpty(keys)) {
			return 0;
		}

		int warmedUp = 0;
		Collection<List<K>> batches = new Partition<>(new ArrayList<>(keys), warmUpBatchSize);

		for (List<K> batch : batches) {
			lock.writeLock().lock();
			try {
				for (K key : batch) {
					if (!delegate.containsKey(key)) {
						try {
							V value = cacheLoader.apply(key);
							if (value != null) {
								delegate.put(key, value);
								warmedUp++;
							}
						} catch (Exception e) {
							// 忽略单个键的加载失败，继续处理其他键
						}
					}
				}
			} finally {
				lock.writeLock().unlock();
			}
		}

		if (enableStats) {
			stats.setCacheSize(delegate.size());
		}

		return warmedUp;
	}

	@Override
	public V computeIfAbsent(K key, Function<K, V> mappingFunction) {
		lock.writeLock().lock();
		try {
			V value = delegate.get(key);
			if (value == null && mappingFunction != null) {
				long startTime = System.nanoTime();
				try {
					value = mappingFunction.apply(key);
					if (value != null) {
						delegate.put(key, value);

						if (enableStats) {
							stats.recordLoadSuccess(System.nanoTime() - startTime);
							stats.setCacheSize(delegate.size());
						}
					}
				} catch (Exception e) {
					if (enableStats) {
						stats.recordLoadFailure();
					}
					throw new CacheException("Failed to compute value for key: " + key, e);
				}
			}

			return value;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public V computeIfPresent(K key, Function<K, V> remappingFunction) {
		lock.writeLock().lock();
		try {
			if (delegate.containsKey(key) && remappingFunction != null) {
				long startTime = System.nanoTime();
				try {
					V newValue = remappingFunction.apply(key);
					if (newValue != null) {
						delegate.put(key, newValue);

						if (enableStats) {
							stats.recordLoadSuccess(System.nanoTime() - startTime);
						}
					}
					return newValue;
				} catch (Exception e) {
					if (enableStats) {
						stats.recordLoadFailure();
					}
					throw new CacheException("Failed to compute value for key: " + key, e);
				}
			}
			return null;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public CacheStats getStats() {
		if (!enableStats) {
			throw new UnsupportedOperationException("Statistics are not enabled");
		}

		lock.readLock().lock();
		try {
			stats.setCacheSize(delegate.size());
			return stats;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void clearStats() {
		if (!enableStats) {
			throw new UnsupportedOperationException("Statistics are not enabled");
		}

		lock.writeLock().lock();
		try {
			// 创建新的统计实例，保留缓存大小
			long currentSize = stats.getCacheSize();
			stats.setCacheSize(currentSize);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = StrUtil.defaultIfBlank(name, "SmartCache");
	}

	/**
	 * 获取底层缓存
	 */
	public Cache<K, V> getDelegate() {
		return delegate;
	}

	@Override
	public V get(K key, boolean isUpdateLastAccess, long timeout, SerSupplier<V> valueFactory) {
		if (key == null) {
			throw new NullPointerException("Key must not be null");
		}

		lock.readLock().lock();
		V value = null;
		try {
			// 1. 优先尝试从底层缓存获取
			value = delegate.get(key, isUpdateLastAccess);
		} finally {
			lock.readLock().unlock();
		}

		// 2. 如果缓存未命中，则使用工厂方法创建、缓存并返回新值
		if (value == null && valueFactory != null) {
			lock.writeLock().lock();
			try {
				// 双重检查锁定模式，防止在获取写锁期间，其他线程已经创建并插入了值
				value = delegate.get(key, isUpdateLastAccess);
				if (value == null) {
					// 记录加载开始时间，用于统计
					long loadStartTime = System.nanoTime();
					try {
						// 调用工厂方法创建新值
						value = valueFactory.get();
						// 如果工厂成功创建了值，则将其放入缓存
						if (value != null) {
							if (timeout > 0) {
								// 使用传入的自定义超时时间
								delegate.put(key, value, timeout);
							} else {
								// 使用缓存的默认超时策略
								delegate.put(key, value);
							}

							// 记录加载成功（如果开启了统计）
							if (enableStats) {
								stats.recordLoadSuccess(System.nanoTime() - loadStartTime);
							}
						} else {
							// 工厂方法返回了null，记录加载失败（可选逻辑）
							if (enableStats) {
								stats.recordLoadFailure();
							}
							// 注意：此时并未将null值存入缓存，下次请求仍会触发加载
						}
					} catch (Exception e) {
						if (enableStats) {
							stats.recordLoadFailure();
						}
						// 可以根据需要决定是抛出异常，还是返回null。
						// 为了保持接口的健壮性，这里将异常包装后抛出。
						throw new CacheException("Failed to load value for key: " + key, e);
					}
				}
				// 无论新值是否由当前线程创建，写锁块结束时，value变量中已经有了最终结果。
			} finally {
				lock.writeLock().unlock();
			}
		}
		// 返回最终结果
		return value;
	}


	@Override
	public Iterator<CacheObj<K, V>> cacheObjIterator() {
		CopiedIter<CacheObj<K, V>> copiedIterator;
		lock.readLock().lock();
		try {
			copiedIterator = CopiedIter.copyOf(this.delegate.cacheObjIterator());
		} finally {
			lock.readLock().unlock();
		}
		return new CacheObjIterator<>(copiedIterator);
	}

	/**
	 * 自定义缓存异常
	 */
	public static class CacheException extends RuntimeException {
		public CacheException(String message) {
			super(message);
		}

		public CacheException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
