package cn.hutool.core.comparator;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.lang.loader.LazyFunLoader;
import cn.hutool.core.util.StrUtil;

import java.util.function.Function;

/**
 * Bean属性排序器<br>
 * 支持读取Bean多层次下的属性
 *
 * @param <T> 被比较的Bean
 * @author Looly
 */
public class PropertyComparator<T> extends FuncComparator<T> {
	private static final long serialVersionUID = 9157326766723846313L;

	/**
	 * 构造
	 *
	 * @param property 属性名
	 */
	public PropertyComparator(String property) {
		this(property, true);
	}

	/**
	 * 构造
	 *
	 * @param property      属性名
	 * @param isNullGreater null值是否排在后（从小到大排序）
	 */
	public PropertyComparator(String property, boolean isNullGreater) {
		this(property, true, isNullGreater);
	}

	/**
	 * 构造
	 *
	 * @param property      属性名
	 * @param compareSelf   在字段值相同情况下，是否比较对象本身。
	 *                      如果此项为{@code false}，字段值比较后为0会导致对象被认为相同，可能导致被去重。
	 * @param isNullGreater null值是否排在后（从小到大排序）
	 * @since 5.8.28
	 */
	public PropertyComparator(String property, final boolean compareSelf, boolean isNullGreater) {
		super(isNullGreater, compareSelf, propertyReader(property));
	}

	/** 创建读取排序属性的函数，首次使用时解析路径。 */
	private static <T> Function<T, Comparable<?>> propertyReader(final String property) {
		if (StrUtil.isBlank(property)) {
			return bean -> null;
		}
		final LazyFunLoader<BeanPath> path = LazyFunLoader.on(() -> BeanPath.create(property));
		return bean -> (Comparable<?>) path.get().get(bean);
	}
}
