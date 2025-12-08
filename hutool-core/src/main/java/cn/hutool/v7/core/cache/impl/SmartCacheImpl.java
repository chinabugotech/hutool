package cn.hutool.v7.core.cache.impl;

import cn.hutool.v7.core.cache.Cache;
import cn.hutool.v7.core.cache.smart.CacheStats;
import cn.hutool.v7.core.cache.smart.SmartCache;
import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.collection.iter.CopiedIter;
import cn.hutool.v7.core.collection.partition.Partition;
import cn.hutool.v7.core.exception.HutoolException;
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
 * @param <K> 缓存键类型
 * @param <V> 缓存值类型
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
	 * 构造函数
	 *
	 * @param delegate           底层缓存实现
	 * @param name               缓存名称
	 * @param enableStats        是否启用统计信息
	 * @param enableAsyncRefresh 是否启用异步刷新
	 * @param warmUpBatchSize    warmUpBatchSize
	 * @param refreshTimeout     刷新超时时间
	 * @param refreshExecutor    刷新执行器
	 * @param cacheLoader        缓存加载器
	 */
	public SmartCacheImpl(
		final Cache<K, V> delegate,
		final String name,
		final boolean enableStats,
		final boolean enableAsyncRefresh,
		final int warmUpBatchSize,
		final Duration refreshTimeout,
		final ExecutorService refreshExecutor,
		final Function<K, V> cacheLoader) {

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
	public void put(final K key, final V object, final long timeout) {
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
	public void put(final K key, final V object) {
		put(key, object, 0);
	}

	@Override
	public V get(final K key, final boolean isUpdateLastAccess) {
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
						final long startTime = System.nanoTime();
						try {
							value = cacheLoader.apply(key);
							if (value != null) {
								delegate.put(key, value);
								stats.recordLoadSuccess(System.nanoTime() - startTime);
							}
						} catch (final Exception e) {
							stats.recordLoadFailure();
							throw new HutoolException("Failed to load cache value for key: " + key, e);
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
	public V get(final K key) {
		return get(key, false);
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public Iterator<V> iterator() {
		return delegate.iterator();
	}

	@Override
	public int prune() {
		lock.writeLock().lock();
		try {
			final int pruned = delegate.prune();
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
	public void remove(final K key) {
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
	public boolean containsKey(final K key) {
		lock.readLock().lock();
		try {
			return delegate.containsKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	// ========== 实现SmartCache接口方法 ==========

	@Override
	public Map<K, V> getAll(final Collection<K> keys) {
		if (CollUtil.isEmpty(keys)) {
			return Collections.emptyMap();
		}

		lock.readLock().lock();
		try {
			final Map<K, V> result = new HashMap<>(keys.size());

			for (final K key : keys) {
				final V value = get(key);
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
	public void putAll(final Map<? extends K, ? extends V> map) {
		if (MapUtil.isEmpty(map)) {
			return;
		}

		lock.writeLock().lock();
		try {
			for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
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
	public CompletableFuture<V> refreshAsync(final K key) {
		if (!enableAsyncRefresh) {
			throw new UnsupportedOperationException("Async refresh is not enabled");
		}

		if (cacheLoader == null) {
			throw new IllegalStateException("Cache loader is required for async refresh");
		}

		// 检查是否已经有正在进行的刷新
		final CompletableFuture<V> pending = pendingRefreshes.get(key);
		if (pending != null) {
			return pending;
		}

		CompletableFuture<V> future = CompletableFuture.supplyAsync(() -> {
			try {
				final long startTime = System.nanoTime();
				final V newValue = cacheLoader.apply(key);

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
			} catch (final Exception e) {
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
	public int warmUp(final Collection<K> keys) {
		if (cacheLoader == null || CollUtil.isEmpty(keys)) {
			return 0;
		}

		int warmedUp = 0;
		final Collection<List<K>> batches = new Partition<>(new ArrayList<>(keys), warmUpBatchSize);

		for (final List<K> batch : batches) {
			lock.writeLock().lock();
			try {
				for (final K key : batch) {
					if (!delegate.containsKey(key)) {
						try {
							final V value = cacheLoader.apply(key);
							if (value != null) {
								delegate.put(key, value);
								warmedUp++;
							}
						} catch (final Exception e) {
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
	public V computeIfAbsent(final K key, final Function<K, V> mappingFunction) {
		lock.writeLock().lock();
		try {
			V value = delegate.get(key);
			if (value == null && mappingFunction != null) {
				final long startTime = System.nanoTime();
				try {
					value = mappingFunction.apply(key);
					if (value != null) {
						delegate.put(key, value);

						if (enableStats) {
							stats.recordLoadSuccess(System.nanoTime() - startTime);
							stats.setCacheSize(delegate.size());
						}
					}
				} catch (final Exception e) {
					if (enableStats) {
						stats.recordLoadFailure();
					}
					throw new HutoolException("Failed to compute value for key: " + key, e);
				}
			}

			return value;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public V computeIfPresent(final K key, final Function<K, V> remappingFunction) {
		lock.writeLock().lock();
		try {
			if (delegate.containsKey(key) && remappingFunction != null) {
				final long startTime = System.nanoTime();
				try {
					final V newValue = remappingFunction.apply(key);
					if (newValue != null) {
						delegate.put(key, newValue);

						if (enableStats) {
							stats.recordLoadSuccess(System.nanoTime() - startTime);
						}
					}
					return newValue;
				} catch (final Exception e) {
					if (enableStats) {
						stats.recordLoadFailure();
					}
					throw new HutoolException("Failed to compute value for key: " + key, e);
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
			final long currentSize = stats.getCacheSize();
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
	public void setName(final String name) {
		this.name = StrUtil.defaultIfBlank(name, "SmartCache");
	}

	/**
	 * 获取底层缓存
	 *
	 * @return 底层缓存实例
	 */
	public Cache<K, V> getDelegate() {
		return delegate;
	}

	@Override
	public V get(final K key, final boolean isUpdateLastAccess, final long timeout, final SerSupplier<V> valueFactory) {
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
					final long loadStartTime = System.nanoTime();
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
					} catch (final Exception e) {
						if (enableStats) {
							stats.recordLoadFailure();
						}
						// 可以根据需要决定是抛出异常，还是返回null。
						// 为了保持接口的健壮性，这里将异常包装后抛出。
						throw new HutoolException("Failed to load value for key: " + key, e);
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
}
