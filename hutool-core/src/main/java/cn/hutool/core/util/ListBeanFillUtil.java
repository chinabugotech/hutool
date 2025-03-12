package cn.hutool.core.util;

import cn.hutool.core.collection.CollectionUtil;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 辅助List中Bean属性填充工具类 (通过另一个List或Map)
 *
 * @author fancg
 **/
public class ListBeanFillUtil {


	/**
	 * 把有相同标识符的Map中对象属性填充到另一个集合中的对象属性中<br>
	 *
	 * @param sourceList      源集合
	 * @param sourceKeyMapper 获取源对象标识符的方法
	 * @param targetMap       目标Map
	 * @param consumer        属性填充方法
	 * @param <T>             源对象类型
	 * @param <R>             目标对象类型
	 * @param <K>             标识符类型
	 */
	public static <T, R, K> void fillByMap(List<T> sourceList, Function<T, K> sourceKeyMapper, Map<K, R> targetMap, BiConsumer<T, R> consumer) {
		if (CollectionUtil.isEmpty(sourceList) || CollectionUtil.isEmpty(targetMap)) {
			return;
		}
		for (T source : sourceList) {
			R target = targetMap.get(sourceKeyMapper.apply(source));
			consumer.accept(source, target);
		}
	}

	/**
	 * 把有相同标识符的集合中对象属性填充到另一个集合中的对象属性中<br>
	 *
	 * @param sourceList      源集合
	 * @param sourceKeyMapper 获取源对象标识符的方法
	 * @param targetList      目标集合
	 * @param targetKeyMapper 获取目标对象标识符的方法
	 * @param consumer        属性填充方法
	 * @param <T>             源对象类型
	 * @param <R>             目标对象类型
	 * @param <K>             标识符类型
	 */
	public static <T, R, K> void fill(List<T> sourceList, Function<T, K> sourceKeyMapper, List<R> targetList, Function<R, K> targetKeyMapper, BiConsumer<T, R> consumer) {
		if (CollectionUtil.isEmpty(sourceList) || CollectionUtil.isEmpty(targetList)) {
			return;
		}
		Map<K, R> targetMap = targetList.stream().collect(Collectors.toMap(targetKeyMapper, Function.identity()));
		fillByMap(sourceList, sourceKeyMapper, targetMap, consumer);
	}

	/**
	 * 把有相同标识符的集合中对象属性填充到另一个集合中的对象属性中<br>
	 *
	 * @param sourceList      源集合
	 * @param targetGetFunc   获取目标集合的方法,接收标识符集合,返回List
	 * @param sourceKeyMapper 获取源对象标识符的方法
	 * @param targetKeyMapper 获取目标对象标识符的方法
	 * @param consumer        属性填充方法
	 * @param <T>             源对象类型
	 * @param <R>             目标对象类型
	 * @param <K>             标识符类型
	 */
	public static <T, R, K> void fill(List<T> sourceList, Function<T, K> sourceKeyMapper, Function<List<K>, List<R>> targetGetFunc, Function<R, K> targetKeyMapper, BiConsumer<T, R> consumer) {
		if (CollectionUtil.isEmpty(sourceList)) {
			return;
		}
		List<K> keyList = sourceList.stream().map(sourceKeyMapper).distinct().collect(Collectors.toList());
		List<R> targetList = targetGetFunc.apply(keyList);
		fill(sourceList, sourceKeyMapper, targetList, targetKeyMapper, consumer);
	}

	/**
	 * 把有相同标识符的集合中对象属性填充到另一个集合中的对象属性中<br>
	 *
	 * @param sourceList      源集合
	 * @param targetGetFunc   获取目标集合的方法,接收标识符集合,返回Map
	 * @param sourceKeyMapper 获取源对象标识符的方法
	 * @param consumer        属性填充方法
	 * @param <T>             源对象类型
	 * @param <R>             目标对象类型
	 * @param <K>             标识符类型
	 */
	public static <T, R, K> void fillByMap(List<T> sourceList, Function<T, K> sourceKeyMapper, Function<List<K>, Map<K, R>> targetGetFunc, BiConsumer<T, R> consumer) {
		if (CollectionUtil.isEmpty(sourceList)) {
			return;
		}
		List<K> keyList = sourceList.stream().map(sourceKeyMapper).distinct().collect(Collectors.toList());
		Map<K, R> targetMap = targetGetFunc.apply(keyList);
		fillByMap(sourceList, sourceKeyMapper, targetMap, consumer);
	}


}
