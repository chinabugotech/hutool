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

/**
 * 表示一个被指定了强制别名的注解属性。
 * 当调用{@link #getValue()}时，总是返回{@link #linked}的值
 *
 * @author huangchengxing
 * @see cn.hutool.v7.core.annotation.processor.AliasAnnotationPostProcessor
 * @see cn.hutool.v7.core.annotation.processor.AliasLinkAnnotationPostProcessor
 * @see cn.hutool.v7.core.annotation.RelationType#ALIAS_FOR
 * @see cn.hutool.v7.core.annotation.RelationType#FORCE_ALIAS_FOR
 */
public class ForceAliasedAnnotationAttribute extends AbstractWrappedAnnotationAttribute {

	/**
	 * 构造
	 *
	 * @param origin 原始属性
	 * @param linked 被强制别名的属性
	 */
	public ForceAliasedAnnotationAttribute(AnnotationAttribute origin, AnnotationAttribute linked) {
		super(origin, linked);
	}

	/**
	 * 总是返回{@link #linked}的{@link AnnotationAttribute#getValue()}的返回值
	 *
	 * @return {@link #linked}的{@link AnnotationAttribute#getValue()}的返回值
	 */
	@Override
	public Object getValue() {
		return linked.getValue();
	}

	/**
	 * 总是返回{@link #linked}的{@link AnnotationAttribute#isValueEquivalentToDefaultValue()}的返回值
	 *
	 * @return {@link #linked}的{@link AnnotationAttribute#isValueEquivalentToDefaultValue()}的返回值
	 */
	@Override
	public boolean isValueEquivalentToDefaultValue() {
		return linked.isValueEquivalentToDefaultValue();
	}

	/**
	 * 总是返回{@link #linked}的{@link AnnotationAttribute#getAttributeType()}的返回值
	 *
	 * @return {@link #linked}的{@link AnnotationAttribute#getAttributeType()}的返回值
	 */
	@Override
	public Class<?> getAttributeType() {
		return linked.getAttributeType();
	}

}
