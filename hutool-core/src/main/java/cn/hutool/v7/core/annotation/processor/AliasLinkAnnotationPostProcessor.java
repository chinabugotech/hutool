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

import cn.hutool.v7.core.annotation.Link;
import cn.hutool.v7.core.annotation.RelationType;
import cn.hutool.v7.core.annotation.attribute.AbstractWrappedAnnotationAttribute;
import cn.hutool.v7.core.annotation.attribute.AliasedAnnotationAttribute;
import cn.hutool.v7.core.annotation.attribute.AnnotationAttribute;
import cn.hutool.v7.core.annotation.attribute.ForceAliasedAnnotationAttribute;
import cn.hutool.v7.core.annotation.synthesize.AnnotationSynthesizer;
import cn.hutool.v7.core.annotation.synthesize.SynthesizedAnnotation;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.lang.Opt;
import cn.hutool.v7.core.util.ObjUtil;

import java.util.function.BinaryOperator;

/**
 * <p>用于处理注解对象中带有{@link Link}注解，且{@link Link#type()}为
 * {@link RelationType#ALIAS_FOR}或{@link RelationType#FORCE_ALIAS_FOR}的属性。<br>
 * 当该处理器执行完毕后，{@link Link}注解指向的目标注解的属性将会被包装并替换为
 * {@link AliasedAnnotationAttribute}或{@link ForceAliasedAnnotationAttribute}。
 *
 * @author huangchengxing
 * @see RelationType#ALIAS_FOR
 * @see AliasedAnnotationAttribute
 * @see RelationType#FORCE_ALIAS_FOR
 * @see ForceAliasedAnnotationAttribute
 */
public class AliasLinkAnnotationPostProcessor extends AbstractLinkAnnotationPostProcessor {

	private static final RelationType[] PROCESSED_RELATION_TYPES = new RelationType[]{RelationType.ALIAS_FOR, RelationType.FORCE_ALIAS_FOR};

	@Override
	public int order() {
		return Integer.MIN_VALUE + 2;
	}

	/**
	 * 该处理器只处理{@link Link#type()}类型为{@link RelationType#ALIAS_FOR}和{@link RelationType#FORCE_ALIAS_FOR}的注解属性
	 *
	 * @return 含有{@link RelationType#ALIAS_FOR}和{@link RelationType#FORCE_ALIAS_FOR}的数组
	 */
	@Override
	protected RelationType[] processTypes() {
		return PROCESSED_RELATION_TYPES;
	}

	/**
	 * 获取{@link Link}指向的目标注解属性，并根据{@link Link#type()}的类型是
	 * {@link RelationType#ALIAS_FOR}或{@link RelationType#FORCE_ALIAS_FOR}
	 * 将目标注解属性包装为{@link AliasedAnnotationAttribute}或{@link ForceAliasedAnnotationAttribute}，
	 * 然后用包装后注解属性在对应的合成注解中替换原始的目标注解属性
	 *
	 * @param synthesizer        注解合成器
	 * @param annotation         {@code originalAttribute}上的{@link Link}注解对象
	 * @param originalAnnotation 当前正在处理的{@link SynthesizedAnnotation}对象
	 * @param originalAttribute  {@code originalAnnotation}上的待处理的属性
	 * @param linkedAnnotation   {@link Link}指向的关联注解对象
	 * @param linkedAttribute    {@link Link}指向的{@code originalAnnotation}中的关联属性，该参数可能为空
	 */
	@Override
	protected void processLinkedAttribute(
		final AnnotationSynthesizer synthesizer, final Link annotation,
		final SynthesizedAnnotation originalAnnotation, final AnnotationAttribute originalAttribute,
		final SynthesizedAnnotation linkedAnnotation, final AnnotationAttribute linkedAttribute) {
		// 校验别名关系
		checkAliasRelation(annotation, originalAttribute, linkedAttribute);
		// 处理aliasFor类型的关系
		if (RelationType.ALIAS_FOR.equals(annotation.type())) {
			wrappingLinkedAttribute(synthesizer, originalAttribute, linkedAttribute, AliasedAnnotationAttribute::new);
			return;
		}
		// 处理forceAliasFor类型的关系
		wrappingLinkedAttribute(synthesizer, originalAttribute, linkedAttribute, ForceAliasedAnnotationAttribute::new);
	}

	/**
	 * 对指定注解属性进行包装，若该属性已被包装过，则递归以其为根节点的树结构，对树上全部的叶子节点进行包装
	 */
	private void wrappingLinkedAttribute(
		final AnnotationSynthesizer synthesizer, final AnnotationAttribute originalAttribute, final AnnotationAttribute aliasAttribute, final BinaryOperator<AnnotationAttribute> wrapping) {
		// 不是包装属性
		if (!aliasAttribute.isWrapped()) {
			processAttribute(synthesizer, originalAttribute, aliasAttribute, wrapping);
			return;
		}
		// 是包装属性
		final AbstractWrappedAnnotationAttribute wrapper = (AbstractWrappedAnnotationAttribute) aliasAttribute;
		wrapper.getAllLinkedNonWrappedAttributes().forEach(
			t -> processAttribute(synthesizer, originalAttribute, t, wrapping)
		);
	}

	/**
	 * 获取指定注解属性，然后将其再进行一层包装
	 */
	private void processAttribute(
		final AnnotationSynthesizer synthesizer, final AnnotationAttribute originalAttribute,
		final AnnotationAttribute target, final BinaryOperator<AnnotationAttribute> wrapping) {
		Opt.ofNullable(target.getAnnotationType())
			.map(synthesizer::getSynthesizedAnnotation)
			.ifPresent(t -> t.replaceAttribute(target.getAttributeName(), old -> wrapping.apply(old, originalAttribute)));
	}

	/**
	 * 基本校验
	 */
	private void checkAliasRelation(final Link annotation, final AnnotationAttribute originalAttribute, final AnnotationAttribute linkedAttribute) {
		checkLinkedAttributeNotNull(originalAttribute, linkedAttribute, annotation);
		checkAttributeType(originalAttribute, linkedAttribute);
		checkCircularDependency(originalAttribute, linkedAttribute);
	}

	/**
	 * 检查两个属性是否互为别名
	 */
	private void checkCircularDependency(final AnnotationAttribute original, final AnnotationAttribute alias) {
		checkLinkedSelf(original, alias);
		final Link annotation = getLinkAnnotation(alias, RelationType.ALIAS_FOR, RelationType.FORCE_ALIAS_FOR);
		if (ObjUtil.isNull(annotation)) {
			return;
		}
		final Class<?> aliasAnnotationType = getLinkedAnnotationType(annotation, alias.getAnnotationType());
		if (ObjUtil.notEquals(aliasAnnotationType, original.getAnnotationType())) {
			return;
		}
		Assert.notEquals(
			annotation.attribute(), original.getAttributeName(),
			"circular reference between the alias attribute [{}] and the original attribute [{}]",
			alias.getAttribute(), original.getAttribute()
		);
	}

}
