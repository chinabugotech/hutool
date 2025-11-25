package cn.hutool.core.map;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.getter.OptNullBasicTypeFromObjectGetter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Map代理，提供各种getXXX方法，并提供默认值支持
 *
 * @author looly
 * @since 3.2.0
 */
public class MapProxy implements Map<Object, Object>, OptNullBasicTypeFromObjectGetter<Object>, InvocationHandler, Serializable {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	Map map;

	/**
	 * 创建代理Map<br>
	 * 此类对Map做一次包装，提供各种getXXX方法
	 *
	 * @param map 被代理的Map
	 * @return {@link MapProxy}
	 */
	public static MapProxy create(Map<?, ?> map) {
		return (map instanceof MapProxy) ? (MapProxy) map : new MapProxy(map);
	}

	/**
	 * 构造
	 *
	 * @param map 被代理的Map
	 */
	public MapProxy(Map<?, ?> map) {
		this.map = map;
	}

	@Override
	public Object getObj(Object key, Object defaultValue) {
		final Object value = map.get(key);
		return null != value ? value : defaultValue;
	}

	/**
	 * 获取代理Map中的元素数量
	 *
	 * @return Map中键值对的数量
	 */
	@Override
	public int size() {
		return map.size();
	}

	/**
	 * 判断代理Map是否为空
	 *
	 * @return 如果Map不包含任何键值对则返回{@code true}
	 */
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * 判断代理Map是否包含指定的键
	 *
	 * @param key 要判断的键
	 * @return 如果Map包含指定键的映射关系则返回{@code true}
	 */
	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	/**
	 * 判断代理Map是否包含指定的值
	 *
	 * @param value 要判断的值
	 * @return 如果Map中存在一个或多个键映射到指定值则返回{@code true}
	 */
	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	/**
	 * 从代理Map中获取指定键对应的值
	 *
	 * @param key 要获取其关联值的键
	 * @return 指定键映射的值,如果Map不包含该键的映射关系则返回{@code null}
	 */
	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	/**
	 * 向代理Map中添加指定的键值对
	 * <p>
	 * 如果Map中已存在该键,则旧值将被替换
	 *
	 * @param key 要存放在Map中的键
	 * @param value 要与键关联的值
	 * @return 与键关联的旧值,如果键没有映射关系则返回{@code null}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	/**
	 * 从代理Map中移除指定键的映射关系
	 *
	 * @param key 要从Map中移除其映射关系的键
	 * @return 与键关联的旧值,如果键没有映射关系则返回{@code null}
	 */
	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	/**
	 * 将指定Map中的所有映射关系复制到代理Map中
	 * <p>
	 * 对于指定Map中的每个键值对,如果代理Map中已存在该键,则旧值将被替换
	 *
	 * @param m 要存储在代理Map中的映射关系
	 */
	@SuppressWarnings({"unchecked", "NullableProblems"})
	@Override
	public void putAll(Map<?, ?> m) {
		map.putAll(m);
	}

	/**
	 * 从代理Map中移除所有映射关系
	 * <p>
	 * 此操作完成后,Map将为空
	 */
	@Override
	public void clear() {
		map.clear();
	}

	/**
	 * 返回代理Map中所有键的Set视图
	 * <p>
	 * 返回的Set由Map支持,因此对Map的更改会反映在Set中,反之亦然
	 *
	 * @return 代理Map中所有键的Set视图
	 */
	@SuppressWarnings({"unchecked", "NullableProblems"})
	@Override
	public Set<Object> keySet() {
		return map.keySet();
	}

	/**
	 * 返回代理Map中所有值的Collection视图
	 * <p>
	 * 返回的Collection由Map支持,因此对Map的更改会反映在Collection中,反之亦然
	 *
	 * @return 代理Map中所有值的Collection视图
	 */
	@SuppressWarnings({"unchecked", "NullableProblems"})
	@Override
	public Collection<Object> values() {
		return map.values();
	}

	/**
	 * 返回代理Map中所有键值对映射关系的Set视图
	 * <p>
	 * 返回的Set由Map支持,因此对Map的更改会反映在Set中,反之亦然
	 *
	 * @return 代理Map中所有键值对映射关系的Set视图
	 */
	@SuppressWarnings({"unchecked", "NullableProblems"})
	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return map.entrySet();
	}

	/**
	 * 实现InvocationHandler接口的调用处理方法
	 * <p>
	 * 此方法用于动态代理,支持以下功能:
	 * <ul>
	 *     <li>将getXXX方法调用转换为从Map中获取对应key的值</li>
	 *     <li>将isXXX方法调用(针对boolean类型)转换为从Map中获取对应key的值</li>
	 *     <li>将setXXX方法调用转换为向Map中设置对应key的值</li>
	 *     <li>支持驼峰命名和下划线命名的自动转换</li>
	 *     <li>自动进行类型转换,将Map中的值转换为方法返回类型</li>
	 * </ul>
	 *
	 * @param proxy 代理实例
	 * @param method 被调用的方法
	 * @param args 方法参数
	 * @return 方法调用的结果
	 * @throws UnsupportedOperationException 如果方法不支持代理
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) {
		final Class<?>[] parameterTypes = method.getParameterTypes();
		if (ArrayUtil.isEmpty(parameterTypes)) {
			final Class<?> returnType = method.getReturnType();
			if (void.class != returnType) {
				// 匹配Getter
				final String methodName = method.getName();
				String fieldName = null;
				if (methodName.startsWith("get")) {
					// 匹配getXXX
					fieldName = StrUtil.removePreAndLowerFirst(methodName, 3);
				} else if (BooleanUtil.isBoolean(returnType) && methodName.startsWith("is")) {
					// 匹配isXXX
					fieldName = StrUtil.removePreAndLowerFirst(methodName, 2);
				}else if ("hashCode".equals(methodName)) {
					return this.hashCode();
				} else if ("toString".equals(methodName)) {
					return this.toString();
				}

				if (StrUtil.isNotBlank(fieldName)) {
					if (false == this.containsKey(fieldName)) {
						// 驼峰不存在转下划线尝试
						fieldName = StrUtil.toUnderlineCase(fieldName);
					}
					return Convert.convert(method.getGenericReturnType(), this.get(fieldName));
				}
			}

		} else if (1 == parameterTypes.length) {
			// 匹配Setter
			final String methodName = method.getName();
			if (methodName.startsWith("set")) {
				final String fieldName = StrUtil.removePreAndLowerFirst(methodName, 3);
				if (StrUtil.isNotBlank(fieldName)) {
					this.put(fieldName, args[0]);
					final Class<?> returnType = method.getReturnType();
					if(returnType.isInstance(proxy)){
						return proxy;
					}
				}
			} else if ("equals".equals(methodName)) {
				return this.equals(args[0]);
			}
		}

		throw new UnsupportedOperationException(method.toGenericString());
	}

	/**
	 * 将Map代理为指定接口的动态代理对象
	 *
	 * @param <T> 代理的Bean类型
	 * @param interfaceClass 接口
	 * @return 代理对象
	 * @since 4.5.2
	 */
	@SuppressWarnings("unchecked")
	public <T> T toProxyBean(Class<T> interfaceClass) {
		return (T) Proxy.newProxyInstance(ClassLoaderUtil.getClassLoader(), new Class<?>[]{interfaceClass}, this);
	}
}
