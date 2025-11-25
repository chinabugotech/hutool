package cn.hutool.core.map;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 双向Map<br>
 * 互换键值对不检查值是否有重复，如果有则后加入的元素替换先加入的元素<br>
 * 值的顺序在HashMap中不确定，所以谁覆盖谁也不确定，在有序的Map中按照先后顺序覆盖，保留最后的值<br>
 * 它与TableMap的区别是，BiMap维护两个Map实现高效的正向和反向查找
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 5.2.6
 */
public class BiMap<K, V> extends MapWrapper<K, V> {
	private static final long serialVersionUID = 1L;

	private Map<V, K> inverse;

	/**
	 * 构造
	 *
	 * @param raw 被包装的Map
	 */
	public BiMap(Map<K, V> raw) {
		super(raw);
	}

	/**
	 * 将指定的键值对放入Map中,同时同步更新反向Map
	 * <p>
	 * 如果key已存在但value不同,会在反向Map中移除旧的value到key的映射,并建立新的value到key的映射
	 *
	 * @param key 键
	 * @param value 值
	 * @return 与key关联的旧值,如果key不存在则返回null
	 */
	@Override
	public V put(K key, V value) {
		final V oldValue = super.put(key, value);
		if (null != this.inverse) {
			if(null != oldValue){
				// issue#I88R5M
				// 如果put的key相同，value不同，需要在inverse中移除旧的关联
				this.inverse.remove(oldValue);
			}
			this.inverse.put(value, key);
		}
		return oldValue;
	}

	/**
	 * 将指定Map中的所有映射关系复制到此Map中,同时同步更新反向Map
	 *
	 * @param m 要存储在此Map中的映射关系
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		super.putAll(m);
		if (null != this.inverse) {
			m.forEach((key, value) -> this.inverse.put(value, key));
		}
	}

	/**
	 * 从Map中移除指定键的映射关系,同时同步从反向Map中移除对应的反向映射
	 *
	 * @param key 要从Map中移除其映射关系的键
	 * @return 与key关联的旧值,如果key不存在则返回null
	 */
	@Override
	public V remove(Object key) {
		final V v = super.remove(key);
		if (null != this.inverse && null != v) {
			this.inverse.remove(v);
		}
		return v;
	}

	/**
	 * 仅当指定键当前映射到指定值时才移除该键的映射关系,同时同步从反向Map中移除对应的反向映射
	 *
	 * @param key 与指定值关联的键
	 * @param value 与指定键关联的值
	 * @return 如果删除成功则返回true
	 */
	@Override
	public boolean remove(Object key, Object value) {
		return super.remove(key, value) && null != this.inverse && this.inverse.remove(value, key);
	}

	/**
	 * 从Map中移除所有映射关系,同时清空反向Map
	 */
	@Override
	public void clear() {
		super.clear();
		this.inverse = null;
	}

	/**
	 * 获取反向Map
	 *
	 * @return 反向Map
	 */
	public Map<V, K> getInverse() {
		if (null == this.inverse) {
			inverse = MapUtil.inverse(getRaw());
		}
		return this.inverse;
	}

	/**
	 * 根据值获得键
	 *
	 * @param value 值
	 * @return 键
	 */
	public K getKey(V value) {
		return getInverse().get(value);
	}

	/**
	 * 如果指定的键尚未与值关联(或映射到null),则将其与给定值关联,同时同步更新反向Map
	 * <p>
	 * 只有当键成功添加到主Map时(即键之前不存在),才会同步更新反向Map,确保双向映射的一致性
	 *
	 * @param key 与指定值关联的键
	 * @param value 与指定键关联的值
	 * @return 与指定键关联的当前值,如果没有映射则返回null
	 */
	@Override
	public V putIfAbsent(K key, V value) {
		final V oldValue = super.putIfAbsent(key, value);
		// 只有当oldValue为null时(即key之前不存在),才更新反向Map
		if (null == oldValue && null != this.inverse) {
			this.inverse.put(value, key);
		}
		return oldValue;
	}

	/**
	 * 如果指定的键尚未与值关联(或映射到null),则使用给定的映射函数计算其值并放入此Map中
	 * <p>
	 * 由于此操作可能会修改Map的映射关系,因此在操作完成后会重置反向Map,下次访问时会重新构建
	 *
	 * @param key 与计算值关联的键
	 * @param mappingFunction 用于计算值的映射函数
	 * @return 与指定键关联的当前(现有的或计算的)值,如果计算的值为null则返回null
	 */
	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		final V result = super.computeIfAbsent(key, mappingFunction);
		resetInverseMap();
		return result;
	}

	/**
	 * 如果指定键的值存在且非null,则使用给定的重新映射函数计算新值
	 * <p>
	 * 如果重新映射函数返回null,则移除该映射关系。由于此操作会修改Map的映射关系,
	 * 因此在操作完成后会重置反向Map,下次访问时会重新构建
	 *
	 * @param key 与指定值关联的键
	 * @param remappingFunction 用于计算值的重新映射函数
	 * @return 与指定键关联的新值,如果不存在则返回null
	 */
	@Override
	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		final V result = super.computeIfPresent(key, remappingFunction);
		resetInverseMap();
		return result;
	}

	/**
	 * 尝试计算指定键及其当前映射值的映射关系(如果当前映射不存在,则为null)
	 * <p>
	 * 此方法用于为指定的键计算新的值。如果重新映射函数返回null,则移除该映射关系(如果存在)。
	 * 由于此操作会修改Map的映射关系,因此在操作完成后会重置反向Map,下次访问时会重新构建
	 *
	 * @param key 与指定值关联的键
	 * @param remappingFunction 用于计算值的重新映射函数
	 * @return 与指定键关联的新值,如果不存在则返回null
	 */
	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		final V result = super.compute(key, remappingFunction);
		resetInverseMap();
		return result;
	}

	/**
	 * 如果指定的键尚未与值关联或关联的值为null,则将其与给定的非null值关联
	 * <p>
	 * 否则,使用给定的重新映射函数的结果替换关联的值,如果结果为null则移除该映射关系。
	 * 由于此操作会修改Map的映射关系,因此在操作完成后会重置反向Map,下次访问时会重新构建
	 *
	 * @param key 与指定值关联的键
	 * @param value 要与键关联的非null值
	 * @param remappingFunction 用于重新计算值的重新映射函数
	 * @return 与指定键关联的新值,如果不存在则返回null
	 */
	@Override
	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		final V result = super.merge(key, value, remappingFunction);
		resetInverseMap();
		return result;
	}

	/**
	 * 重置反转的Map，如果反转map为空，则不操作。
	 */
	private void resetInverseMap() {
		if (null != this.inverse) {
			inverse = null;
		}
	}
}
