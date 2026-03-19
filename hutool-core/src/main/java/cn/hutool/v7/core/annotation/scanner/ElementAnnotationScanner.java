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

package cn.hutool.v7.core.annotation.scanner;

import cn.hutool.v7.core.func.PredicateUtil;
import cn.hutool.v7.core.util.ObjUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 扫描{@link AnnotatedElement}上的注解，不支持处理层级对象
 *
 * @author huangchengxing
 */
public class ElementAnnotationScanner implements AnnotationScanner {

	/**
	 * 判断是否支持扫描该注解元素，仅当注解元素不为空时返回{@code true}
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @return 是否支持扫描该注解元素
	 */
	@Override
	public boolean support(final AnnotatedElement annotatedEle) {
		return ObjUtil.isNotNull(annotatedEle);
	}

	/**
	 * 扫描{@link AnnotatedElement}上直接声明的注解，调用前需要确保调用{@link #support(AnnotatedElement)}返回为true
	 *
	 * @param consumer     对获取到的注解和注解对应的层级索引的处理
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param filter       注解过滤器，无法通过过滤器的注解不会被处理。该参数允许为空。
	 */
	@Override
	public void scan(final BiConsumer<Integer, Annotation> consumer, final AnnotatedElement annotatedEle, Predicate<Annotation> filter) {
		filter = ObjUtil.defaultIfNull(filter, PredicateUtil.alwaysTrue());
		Stream.of(annotatedEle.getAnnotations())
			.filter(filter)
			.forEach(annotation -> consumer.accept(0, annotation));
	}

}
