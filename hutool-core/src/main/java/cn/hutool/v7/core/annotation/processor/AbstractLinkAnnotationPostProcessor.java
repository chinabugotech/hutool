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

package cn.hutool.v7.core.annotation.processor;

import cn.hutool.v7.core.annotation.AnnotationUtil;
import cn.hutool.v7.core.annotation.Link;
import cn.hutool.v7.core.annotation.RelationType;
import cn.hutool.v7.core.annotation.attribute.AnnotationAttribute;
import cn.hutool.v7.core.annotation.synthesize.AnnotationSynthesizer;
import cn.hutool.v7.core.annotation.synthesize.SynthesizedAggregateAnnotation;
import cn.hutool.v7.core.annotation.synthesize.SynthesizedAnnotation;
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.lang.Opt;
import cn.hutool.v7.core.util.ObjUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link SynthesizedAnnotationPostProcessor}的基本实现，
 * 用于处理注解中带有{@link Link}注解的属性。
 *
 * @author huangchengxing
 * @see MirrorLinkAnnotationPostProcessor
 * @see AliasLinkAnnotationPostProcessor
 */
public abstract class AbstractLinkAnnotationPostProcessor implements SynthesizedAnnotationPostProcessor {

	/**
	 * 若一个注解属性上存在{@link Link}注解，注解的{@link Link#type()}返回值在{@link #processTypes()}中存在，
	 * 且此{@link Link}指定的注解对象在当前的{@link SynthesizedAggregateAnnotation}中存在，
	 * 则从聚合器中获取类型对应的合成注解对象，与该对象中的指定属性，然后将全部关联数据交给
	 * {@link #processLinkedAttribute}处理。
	 *
	 * @param synthesizedAnnotation 合成的注解
	 * @param synthesizer           合成注解聚合器
	 */
	@Override
	public void process(final SynthesizedAnnotation synthesizedAnnotation, final AnnotationSynthesizer synthesizer) {
		final Map<String, AnnotationAttribute> attributeMap = new HashMap<>(synthesizedAnnotation.getAttributes());
		attributeMap.forEach((originalAttributeName, originalAttribute) -> {
			// 获取注解
			final Link link = getLinkAnnotation(originalAttribute, processTypes());
			if (ObjUtil.isNull(link)) {
				return;
			}
			// 获取注解属性
			final SynthesizedAnnotation linkedAnnotation = getLinkedAnnotation(link, synthesizer, synthesizedAnnotation.annotationType());
			if (ObjUtil.isNull(linkedAnnotation)) {
				return;
			}
			final AnnotationAttribute linkedAttribute = linkedAnnotation.getAttributes().get(link.attribute());
			// 处理
			processLinkedAttribute(
					synthesizer, link,
					synthesizedAnnotation, synthesizedAnnotation.getAttributes().get(originalAttributeName),
					linkedAnnotation, linkedAttribute
			);
		});
	}

	// =========================== 抽象方法 ===========================

	/**
	 * 当属性上存在{@link Link}注解时，仅当{@link Link#type()}在本方法返回值内存在时才进行处理
	 *
	 * @return 支持处理的{@link RelationType}类型
	 */
	protected abstract RelationType[] processTypes();

	/**
	 * 对关联的合成注解对象及其关联属性的处理
	 *
	 * @param synthesizer        注解合成器
	 * @param annotation         {@code originalAttribute}上的{@link Link}注解对象
	 * @param originalAnnotation 当前正在处理的{@link SynthesizedAnnotation}对象
	 * @param originalAttribute  {@code originalAnnotation}上的待处理的属性
	 * @param linkedAnnotation   {@link Link}指向的关联注解对象
	 * @param linkedAttribute    {@link Link}指向的{@code originalAnnotation}中的关联属性，该参数可能为空
	 */
	protected abstract void processLinkedAttribute(
			AnnotationSynthesizer synthesizer, Link annotation,
			SynthesizedAnnotation originalAnnotation, AnnotationAttribute originalAttribute,
			SynthesizedAnnotation linkedAnnotation, AnnotationAttribute linkedAttribute
	);

	// =========================== @Link注解的处理 ===========================

	/**
	 * 从注解属性上获取指定类型的{@link Link}注解
	 *
	 * @param attribute     注解属性
	 * @param relationTypes 类型
	 * @return 注解
	 */
	protected Link getLinkAnnotation(final AnnotationAttribute attribute, final RelationType... relationTypes) {
		return Opt.ofNullable(attribute)
				.map(t -> AnnotationUtil.getSynthesizedAnnotation(attribute.getAttribute(), Link.class))
				.filter(a -> ArrayUtil.contains(relationTypes, a.type()))
				.getOrNull();
	}

	/**
	 * 从合成注解中获取{@link Link#type()}指定的注解对象
	 *
	 * @param annotation  {@link Link}注解
	 * @param synthesizer 注解合成器
	 * @param defaultType 默认类型
	 * @return {@link SynthesizedAnnotation}
	 */
	protected SynthesizedAnnotation getLinkedAnnotation(final Link annotation, final AnnotationSynthesizer synthesizer, final Class<? extends Annotation> defaultType) {
		final Class<?> targetAnnotationType = getLinkedAnnotationType(annotation, defaultType);
		return synthesizer.getSynthesizedAnnotation(targetAnnotationType);
	}

	/**
	 * 若{@link Link#annotation()}获取的类型{@code Annotation#getClass()}，则返回{@code defaultType}，
	 * 否则返回{@link Link#annotation()}指定的类型
	 *
	 * @param annotation  {@link Link}注解
	 * @param defaultType 默认注解类型
	 * @return 注解类型
	 */
	protected Class<?> getLinkedAnnotationType(final Link annotation, final Class<?> defaultType) {
		return ObjUtil.equals(annotation.annotation(), Annotation.class) ?
				defaultType : annotation.annotation();
	}

	// =========================== 注解属性的校验 ===========================

	/**
	 * 校验两个注解属性的返回值类型是否一致
	 *
	 * @param original 原属性
	 * @param alias    别名属性
	 */
	protected void checkAttributeType(final AnnotationAttribute original, final AnnotationAttribute alias) {
		Assert.equals(
				original.getAttributeType(), alias.getAttributeType(),
				"return type of the linked attribute [{}] is inconsistent with the original [{}]",
				original.getAttribute(), alias.getAttribute()
		);
	}

	/**
	 * 检查{@link Link}指向的注解属性是否就是本身
	 *
	 * @param original {@link Link}注解的属性
	 * @param linked   {@link Link}指向的注解属性
	 */
	protected void checkLinkedSelf(final AnnotationAttribute original, final AnnotationAttribute linked) {
		final boolean linkSelf = (original == linked) || ObjUtil.equals(original.getAttribute(), linked.getAttribute());
		Assert.isFalse(linkSelf, "cannot link self [{}]", original.getAttribute());
	}

	/**
	 * 检查{@link Link}指向的注解属性是否存在
	 *
	 * @param original        {@link Link}注解的属性
	 * @param linkedAttribute {@link Link}指向的注解属性
	 * @param annotation      {@link Link}注解
	 */
	protected void checkLinkedAttributeNotNull(final AnnotationAttribute original, final AnnotationAttribute linkedAttribute, final Link annotation) {
		Assert.notNull(linkedAttribute, "cannot find linked attribute [{}] of original [{}] in [{}]",
				original.getAttribute(), annotation.attribute(),
				getLinkedAnnotationType(annotation, original.getAnnotationType())
		);
	}

}
