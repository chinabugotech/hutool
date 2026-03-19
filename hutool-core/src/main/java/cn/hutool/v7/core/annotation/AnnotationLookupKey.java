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

package cn.hutool.v7.core.annotation;

import java.io.Serial;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Objects;

/**
 * 注解查找键（用于细粒度缓存）
 * 用于唯一标识一次注解查找操作
 *
 * @author heco07
 * @since 7.0.0
 */
class AnnotationLookupKey implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 被注解的元素（Class、Method、Field等）
	 */
	private final AnnotatedElement element;
	/**
	 * 目标注解类型
	 */
	private final Class<? extends Annotation> annotationType;

	/**
	 * 构造
	 *
	 * @param element        被注解的元素
	 * @param annotationType 目标注解类型
	 */
	public AnnotationLookupKey(final AnnotatedElement element, final Class<? extends Annotation> annotationType) {
		this.element = element;
		this.annotationType = annotationType;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final AnnotationLookupKey that = (AnnotationLookupKey) o;
		return Objects.equals(element, that.element) && Objects.equals(annotationType, that.annotationType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(element, annotationType);
	}
}
