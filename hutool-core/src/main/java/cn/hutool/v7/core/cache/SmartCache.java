package cn.hutool.v7.core.cache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * 智能缓存接口 - 扩展Hutool标准缓存功能
 *
 * <p>提供多级缓存、异步刷新、缓存预热等高级功能</p>
 *
 * @author Nic
 */
public interface SmartCache<K, V> extends Cache<K, V> {

	/**
	 * 批量获取缓存项
	 *
	 * @param keys 键集合
	 * @return 键值对映射
	 */
	Map<K, V> getAll(Collection<K> keys);

	/**
	 * 批量放入缓存项
	 *
	 * @param map 键值对映射
	 */
	void putAll(Map<? extends K, ? extends V> map);

	/**
	 * 异步刷新缓存项
	 *
	 * @param key 缓存键
	 * @return CompletableFuture包装的缓存值
	 */
	CompletableFuture<V> refreshAsync(K key);

	/**
	 * 缓存预热
	 *
	 * @param keys 需要预热的键集合
	 * @return 预热成功的数量
	 */
	int warmUp(Collection<K> keys);

	/**
	 * 原子操作：如果不存在则计算并放入
	 *
	 * @param key 缓存键
	 * @param mappingFunction 映射函数
	 * @return 缓存值
	 */
	V computeIfAbsent(K key, Function<K, V> mappingFunction);

	/**
	 * 原子操作：如果存在则重新计算
	 *
	 * @param key 缓存键
	 * @param remappingFunction 重新映射函数
	 * @return 新的缓存值
	 */
	V computeIfPresent(K key, Function<K, V> remappingFunction);

	/**
	 * 获取缓存统计信息
	 *
	 * @return 缓存统计
	 */
	CacheStats getStats();

	/**
	 * 清除所有统计信息
	 */
	void clearStats();

	/**
	 * 获取缓存名称
	 *
	 * @return 缓存名称
	 */
	String getName();

	/**
	 * 设置缓存名称
	 *
	 * @param name 缓存名称
	 */
	void setName(String name);
}
