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
import cn.hutool.v7.core.annotation.attribute.AnnotationAttribute;
import cn.hutool.v7.core.annotation.attribute.MirroredAnnotationAttribute;
import cn.hutool.v7.core.annotation.synthesize.AnnotationSynthesizer;
import cn.hutool.v7.core.annotation.synthesize.SynthesizedAnnotation;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.text.CharSequenceUtil;
import cn.hutool.v7.core.util.ObjUtil;

/**
 * <p>用于处理注解对象中带有{@link Link}注解，且{@link Link#type()}为{@link RelationType#MIRROR_FOR}的属性。<br>
 * 当该处理器执行完毕后，原始合成注解中被{@link Link}注解的属性与{@link Link}注解指向的目标注解的属性，
 * 都将会被被包装并替换为{@link MirroredAnnotationAttribute}。
 *
 * @author huangchengxing
 * @see RelationType#MIRROR_FOR
 * @see MirroredAnnotationAttribute
 */
public class MirrorLinkAnnotationPostProcessor extends AbstractLinkAnnotationPostProcessor {

	private static final RelationType[] PROCESSED_RELATION_TYPES = new RelationType[]{ RelationType.MIRROR_FOR };

	@Override
	public int order() {
		return Integer.MIN_VALUE + 1;
	}

	/**
	 * 该处理器只处理{@link Link#type()}类型为{@link RelationType#MIRROR_FOR}的注解属性
	 *
	 * @return 仅有{@link RelationType#MIRROR_FOR}数组
	 */
	@Override
	protected RelationType[] processTypes() {
		return PROCESSED_RELATION_TYPES;
	}

	/**
	 * 将存在镜像关系的合成注解属性分别包装为{@link MirroredAnnotationAttribute}对象，
	 * 并使用包装后{@link MirroredAnnotationAttribute}替换在它们对应合成注解实例中的{@link AnnotationAttribute}
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

		// 镜像属性必然成对出现，因此此处必定存在三种情况：
		// 1.两属性都不为镜像属性，此时继续进行后续处理；
		// 2.两属性都为镜像属性，并且指向对方，此时无需后续处理；
		// 3.两属性仅有任意一属性为镜像属性，此时镜像属性必然未指向当前原始属性，此时应该抛出异常；
		if (originalAttribute instanceof MirroredAnnotationAttribute
			|| linkedAttribute instanceof MirroredAnnotationAttribute) {
			checkMirrored(originalAttribute, linkedAttribute);
			return;
		}

		// 校验镜像关系
		checkMirrorRelation(annotation, originalAttribute, linkedAttribute);
		// 包装这一对镜像属性，并替换原注解中的对应属性
		final AnnotationAttribute mirroredOriginalAttribute = new MirroredAnnotationAttribute(originalAttribute, linkedAttribute);
		originalAnnotation.setAttribute(originalAttribute.getAttributeName(), mirroredOriginalAttribute);
		final AnnotationAttribute mirroredTargetAttribute = new MirroredAnnotationAttribute(linkedAttribute, originalAttribute);
		linkedAnnotation.setAttribute(annotation.attribute(), mirroredTargetAttribute);
	}

	/**
	 * 检查映射关系是否正确
	 */
	@SuppressWarnings("DataFlowIssue")
	private void checkMirrored(final AnnotationAttribute original, final AnnotationAttribute mirror) {
		final boolean originalAttributeMirrored = original instanceof MirroredAnnotationAttribute;
		final boolean mirrorAttributeMirrored = mirror instanceof MirroredAnnotationAttribute;

		// 校验通过
		final boolean passed = originalAttributeMirrored && mirrorAttributeMirrored
			&& ObjUtil.equals(((MirroredAnnotationAttribute)original).getLinked(), ((MirroredAnnotationAttribute)mirror).getOriginal());
		if (passed) {
			return;
		}

		// 校验失败，拼装异常信息用于抛出异常
		final String errorMsg;
		// 原始字段已经跟其他字段形成镜像
		if (originalAttributeMirrored && !mirrorAttributeMirrored) {
			errorMsg = CharSequenceUtil.format(
				"attribute [{}] cannot mirror for [{}], because it's already mirrored for [{}]",
				original.getAttribute(), mirror.getAttribute(), ((MirroredAnnotationAttribute)original).getLinked()
			);
		}
		// 镜像字段已经跟其他字段形成镜像
		else if (!originalAttributeMirrored && mirrorAttributeMirrored) {
			errorMsg = CharSequenceUtil.format(
				"attribute [{}] cannot mirror for [{}], because it's already mirrored for [{}]",
				mirror.getAttribute(), original.getAttribute(), ((MirroredAnnotationAttribute)mirror).getLinked()
			);
		}
		// 两者都形成了镜像，但是都未指向对方，理论上不会存在该情况
		else {
			errorMsg = CharSequenceUtil.format(
				"attribute [{}] cannot mirror for [{}], because [{}] already mirrored for [{}] and  [{}] already mirrored for [{}]",
				mirror.getAttribute(), original.getAttribute(),
				mirror.getAttribute(), ((MirroredAnnotationAttribute)mirror).getLinked(),
				original.getAttribute(), ((MirroredAnnotationAttribute)original).getLinked()
			);
		}

		throw new IllegalArgumentException(errorMsg);
	}

	/**
	 * 基本校验
	 */
	private void checkMirrorRelation(final Link annotation, final AnnotationAttribute original, final AnnotationAttribute mirror) {
		// 镜像属性必须存在
		checkLinkedAttributeNotNull(original, mirror, annotation);
		// 镜像属性返回值必须一致
		checkAttributeType(original, mirror);
		// 镜像属性上必须存在对应的注解
		final Link mirrorAttributeAnnotation = getLinkAnnotation(mirror, RelationType.MIRROR_FOR);
		Assert.isTrue(
			ObjUtil.isNotNull(mirrorAttributeAnnotation) && RelationType.MIRROR_FOR.equals(mirrorAttributeAnnotation.type()),
			"mirror attribute [{}] of original attribute [{}] must marked by @Link, and also @LinkType.type() must is [{}]",
			mirror.getAttribute(), original.getAttribute(), RelationType.MIRROR_FOR
		);
		checkLinkedSelf(original, mirror);
	}

}
