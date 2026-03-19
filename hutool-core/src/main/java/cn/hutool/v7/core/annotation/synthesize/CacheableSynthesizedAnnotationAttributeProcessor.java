/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.core.annotation.synthesize;

import cn.hutool.v7.core.annotation.Alias;
import cn.hutool.v7.core.annotation.Link;
import cn.hutool.v7.core.annotation.processor.SynthesizedAnnotationAttributeProcessor;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.map.multi.RowKeyTable;
import cn.hutool.v7.core.map.multi.Table;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

/**
 * <p>带缓存功能的{@link SynthesizedAnnotationAttributeProcessor}实现，
 * 构建时需要传入比较器，获取属性值时将根据比较器对合成注解进行排序，
 * 然后选择具有所需属性的，排序最靠前的注解用于获取属性值
 *
 * <p>通过该处理器获取合成注解属性值时会出现隐式别名，
 * 即子注解和元注解中同时存在类型和名称皆相同的属性时，元注解中属性总是会被该属性覆盖，
 * 并且该覆盖关系并不会通过{@link Alias}或{@link Link}被传递到关联的属性中。
 *
 * @author huangchengxing
 */
public class CacheableSynthesizedAnnotationAttributeProcessor implements SynthesizedAnnotationAttributeProcessor {

	private final Table<String, Class<?>, Object> valueCaches = new RowKeyTable<>();
	private final Comparator<Hierarchical> annotationComparator;

	/**
	 * 创建一个带缓存的注解值选择器
	 *
	 * @param annotationComparator 注解比较器，排序更靠前的注解将被优先用于获取值
	 */
	public CacheableSynthesizedAnnotationAttributeProcessor(final Comparator<Hierarchical> annotationComparator) {
		Assert.notNull(annotationComparator, "annotationComparator must not null");
		this.annotationComparator = annotationComparator;
	}

	/**
	 * 创建一个带缓存的注解值选择器，
	 * 默认按{@link SynthesizedAnnotation#getVerticalDistance()}和{@link SynthesizedAnnotation#getHorizontalDistance()}排序，
	 * 越靠前的越优先被取值。
	 */
	public CacheableSynthesizedAnnotationAttributeProcessor() {
		this(Hierarchical.DEFAULT_HIERARCHICAL_COMPARATOR);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttributeValue(final String attributeName, final Class<T> attributeType, final Collection<? extends SynthesizedAnnotation> synthesizedAnnotations) {
		Object value = valueCaches.get(attributeName, attributeType);
		if (Objects.isNull(value)) {
			synchronized (valueCaches) {
				value = valueCaches.get(attributeName, attributeType);
				if (Objects.isNull(value)) {
					value = synthesizedAnnotations.stream()
						.filter(ma -> ma.hasAttribute(attributeName, attributeType))
						.min(annotationComparator)
						.map(ma -> ma.getAttributeValue(attributeName))
						.orElse(null);
					valueCaches.put(attributeName, attributeType, value);
				}
			}
		}
		return (T)value;
	}
}
