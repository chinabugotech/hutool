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

package cn.hutool.v7.core.annotation.attribute;


import cn.hutool.v7.core.lang.Assert;

/**
 * 表示存在对应镜像属性的注解属性，当获取值时将根据{@link cn.hutool.v7.core.annotation.RelationType#MIRROR_FOR}的规则进行处理
 *
 * @author huangchengxing
 * @see cn.hutool.v7.core.annotation.processor.MirrorLinkAnnotationPostProcessor
 * @see cn.hutool.v7.core.annotation.RelationType#MIRROR_FOR
 */
public class MirroredAnnotationAttribute extends AbstractWrappedAnnotationAttribute {

	/**
	 * 创建一个镜像注解属性
	 *
	 * @param origin 原始属性
	 * @param linked 镜像属性
	 */
	public MirroredAnnotationAttribute(final AnnotationAttribute origin, final AnnotationAttribute linked) {
		super(origin, linked);
	}

	@Override
	public Object getValue() {
		final boolean originIsDefault = original.isValueEquivalentToDefaultValue();
		final boolean targetIsDefault = linked.isValueEquivalentToDefaultValue();
		final Object originValue = original.getValue();
		final Object targetValue = linked.getValue();

		// 都为默认值，或都为非默认值时，两方法的返回值必须相等
		if (originIsDefault == targetIsDefault) {
			Assert.equals(
				originValue, targetValue,
				"the values of attributes [{}] and [{}] that mirror each other are different: [{}] <==> [{}]",
				original.getAttribute(), linked.getAttribute(), originValue, targetValue
			);
			return originValue;
		}

		// 两者有一者不为默认值时，优先返回非默认值
		return originIsDefault ? targetValue : originValue;
	}

	/**
	 * 当{@link #original}与{@link #linked}都为默认值时返回{@code true}
	 *
	 * @return 是否
	 */
	@Override
	public boolean isValueEquivalentToDefaultValue() {
		return original.isValueEquivalentToDefaultValue() && linked.isValueEquivalentToDefaultValue();
	}
}
