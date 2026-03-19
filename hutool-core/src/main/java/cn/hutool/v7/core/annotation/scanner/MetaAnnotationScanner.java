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

import cn.hutool.v7.core.annotation.AnnotationUtil;
import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.func.PredicateUtil;
import cn.hutool.v7.core.reflect.ClassUtil;
import cn.hutool.v7.core.util.ObjUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 扫描注解类上存在的注解，支持处理枚举实例或枚举类型
 * 需要注意，当待解析是枚举类时，有可能与{@link TypeAnnotationScanner}冲突
 *
 * @author huangchengxing
 * @see TypeAnnotationScanner
 */
public class MetaAnnotationScanner implements AnnotationScanner {

	/**
	 * 获取当前注解的元注解后，是否继续递归扫描的元注解的元注解
	 */
	private final boolean includeSupperMetaAnnotation;

	/**
	 * 构造一个元注解扫描器
	 *
	 * @param includeSupperMetaAnnotation 获取当前注解的元注解后，是否继续递归扫描的元注解的元注解
	 */
	public MetaAnnotationScanner(final boolean includeSupperMetaAnnotation) {
		this.includeSupperMetaAnnotation = includeSupperMetaAnnotation;
	}

	/**
	 * 构造一个元注解扫描器，默认在扫描当前注解上的元注解后，并继续递归扫描元注解
	 */
	public MetaAnnotationScanner() {
		this(true);
	}

	/**
	 * 判断是否支持扫描该注解元素，仅当注解元素是{@link Annotation}接口的子类{@link Class}时返回{@code true}
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @return 是否支持扫描该注解元素
	 */
	@Override
	public boolean support(final AnnotatedElement annotatedEle) {
		return (annotatedEle instanceof Class && ClassUtil.isAssignable(Annotation.class, (Class<?>) annotatedEle));
	}

	/**
	 * 获取注解元素上的全部注解。调用该方法前，需要确保调用{@link #support(AnnotatedElement)}返回为true
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @return 注解
	 */
	@Override
	public List<Annotation> getAnnotations(final AnnotatedElement annotatedEle) {
		final List<Annotation> annotations = new ArrayList<>();
		scan(
			(index, annotation) -> annotations.add(annotation), annotatedEle,
			annotation -> ObjUtil.notEquals(annotation, annotatedEle)
		);
		return annotations;
	}

	/**
	 * 按广度优先扫描指定注解上的元注解，对扫描到的注解与层级索引进行操作
	 *
	 * @param consumer     当前层级索引与操作
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param filter       过滤器
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void scan(final BiConsumer<Integer, Annotation> consumer, final AnnotatedElement annotatedEle, Predicate<Annotation> filter) {
		filter = ObjUtil.defaultIfNull(filter, PredicateUtil.alwaysTrue());
		final Set<Class<? extends Annotation>> accessed = new HashSet<>();
		final Deque<List<Class<? extends Annotation>>> deque = ListUtil.ofLinked(ListUtil.of((Class<? extends Annotation>) annotatedEle));
		int distance = 0;
		do {
			final List<Class<? extends Annotation>> annotationTypes = deque.removeFirst();
			for (final Class<? extends Annotation> type : annotationTypes) {
				final List<Annotation> metaAnnotations = Stream.of(type.getAnnotations())
					.filter(a -> !AnnotationUtil.isMetaAnnotation(a.annotationType()))
					.filter(filter)
					.toList();
				for (final Annotation metaAnnotation : metaAnnotations) {
					consumer.accept(distance, metaAnnotation);
				}
				accessed.add(type);
				final List<Class<? extends Annotation>> next = metaAnnotations.stream()
					.map(Annotation::annotationType)
					.filter(t -> !accessed.contains(t))
					.collect(Collectors.toList());
				if (CollUtil.isNotEmpty(next)) {
					deque.addLast(next);
				}
			}
			distance++;
		} while (includeSupperMetaAnnotation && !deque.isEmpty());
	}

}
