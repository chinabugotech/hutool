/*
 * Copyright (c) 2013-2026 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.core.comparator;

import cn.hutool.v7.core.bean.path.BeanPath;
import cn.hutool.v7.core.lang.loader.LazyFunLoader;
import cn.hutool.v7.core.text.StrUtil;

import java.io.Serial;
import java.util.Map;
import java.util.function.Function;

/**
 * Bean属性排序器<br>
 * 支持读取Bean多层次下的属性
 *
 * @author Looly
 *
 * @param <T> 被比较的Bean
 */
public class PropertyComparator<T> extends FuncComparator<T> {
	@Serial
	private static final long serialVersionUID = 9157326766723846313L;

	/**
	 * 构造，默认{@code null}排在后（从小到大排序）
	 *
	 * @param property 属性名
	 */
	public PropertyComparator(final String property) {
		this(property, true);
	}

	/**
	 * 构造
	 *
	 * @param property      属性名
	 * @param isNullGreater null值是否排在后（从小到大排序）
	 */
	public PropertyComparator(final String property, final boolean isNullGreater) {
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
	public PropertyComparator(final String property, final boolean compareSelf, final boolean isNullGreater) {
		super(isNullGreater, compareSelf, propertyReader(property));
	}

	/** 创建读取排序属性的函数，首次使用时解析路径。 */
	private static <T> Function<T, Comparable<?>> propertyReader(final String property) {
		if (StrUtil.isBlank(property)) {
			return bean -> null;
		}
		final LazyFunLoader<BeanPath<Object>> path = LazyFunLoader.of(() -> BeanPath.of(property));
		return bean -> {
			// 含点号的完整 Map 键优先于嵌套路径。
			if (bean instanceof Map<?, ?> map && map.containsKey(property)) {
				return (Comparable<?>) map.get(property);
			}
			return (Comparable<?>) path.get().getValue(bean);
		};
	}
}
