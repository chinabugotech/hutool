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
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.func.PredicateUtil;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.util.ObjUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 为需要从类的层级结构中获取注解的{@link AnnotationScanner}提供基本实现
 *
 * @param <T> 当前实例的类型
 * @author huangchengxing
 */
public abstract class AbstractTypeAnnotationScanner<T extends AbstractTypeAnnotationScanner<T>> implements AnnotationScanner {

	/**
	 * 是否允许扫描父类
	 */
	private boolean includeSuperClass;

	/**
	 * 是否允许扫描父接口
	 */
	private boolean includeInterfaces;

	/**
	 * 过滤器，若类型无法通过该过滤器，则该类型及其树结构将直接不被查找
	 */
	private Predicate<Class<?>> filter;

	/**
	 * 排除的类型，以上类型及其树结构将直接不被查找
	 */
	private final Set<Class<?>> excludeTypes;

	/**
	 * 转换器
	 */
	private final List<UnaryOperator<Class<?>>> converters;

	/**
	 * 是否有转换器
	 */
	private boolean hasConverters;

	/**
	 * 当前实例
	 */
	private final T typedThis;

	/**
	 * 构造一个类注解扫描器
	 *
	 * @param includeSuperClass 是否允许扫描父类
	 * @param includeInterfaces 是否允许扫描父接口
	 * @param filter            过滤器
	 * @param excludeTypes      不包含的类型
	 */
	@SuppressWarnings("unchecked")
	protected AbstractTypeAnnotationScanner(final boolean includeSuperClass, final boolean includeInterfaces, final Predicate<Class<?>> filter, final Set<Class<?>> excludeTypes) {
		Assert.notNull(filter, "filter must not null");
		Assert.notNull(excludeTypes, "excludeTypes must not null");
		this.includeSuperClass = includeSuperClass;
		this.includeInterfaces = includeInterfaces;
		this.filter = filter;
		this.excludeTypes = excludeTypes;
		this.converters = new ArrayList<>();
		this.typedThis = (T) this;
	}

	/**
	 * 是否允许扫描父类
	 *
	 * @return 是否允许扫描父类
	 */
	public boolean isIncludeSuperClass() {
		return includeSuperClass;
	}

	/**
	 * 是否允许扫描父接口
	 *
	 * @return 是否允许扫描父接口
	 */
	public boolean isIncludeInterfaces() {
		return includeInterfaces;
	}

	/**
	 * 设置过滤器，若类型无法通过该过滤器，则该类型及其树结构将直接不被查找
	 *
	 * @param filter 过滤器
	 * @return 当前实例
	 */
	public T setFilter(final Predicate<Class<?>> filter) {
		Assert.notNull(filter, "filter must not null");
		this.filter = filter;
		return typedThis;
	}

	/**
	 * 添加不扫描的类型，该类型及其树结构将直接不被查找
	 *
	 * @param excludeTypes 不扫描的类型
	 * @return 当前实例
	 */
	public T addExcludeTypes(final Class<?>... excludeTypes) {
		CollUtil.addAll(this.excludeTypes, excludeTypes);
		return typedThis;
	}

	/**
	 * 添加转换器
	 *
	 * @param converter 转换器
	 * @return 当前实例
	 * @see JdkProxyClassConverter
	 */
	public T addConverters(final UnaryOperator<Class<?>> converter) {
		Assert.notNull(converter, "converter must not null");
		this.converters.add(converter);
		if (!this.hasConverters) {
			this.hasConverters = CollUtil.isNotEmpty(this.converters);
		}
		return typedThis;
	}

	/**
	 * 是否允许扫描父类
	 *
	 * @param includeSuperClass 是否
	 * @return 当前实例
	 */
	protected T setIncludeSuperClass(final boolean includeSuperClass) {
		this.includeSuperClass = includeSuperClass;
		return typedThis;
	}

	/**
	 * 是否允许扫描父接口
	 *
	 * @param includeInterfaces 是否
	 * @return 当前实例
	 */
	protected T setIncludeInterfaces(final boolean includeInterfaces) {
		this.includeInterfaces = includeInterfaces;
		return typedThis;
	}

	/**
	 * 则根据广度优先递归扫描类的层级结构，并对层级结构中类/接口声明的层级索引和它们声明的注解对象进行处理
	 *
	 * @param consumer     对获取到的注解和注解对应的层级索引的处理
	 * @param annotatedEle 注解元素
	 * @param filter       注解过滤器，无法通过过滤器的注解不会被处理。该参数允许为空。
	 */
	@Override
	public void scan(final BiConsumer<Integer, Annotation> consumer, final AnnotatedElement annotatedEle, Predicate<Annotation> filter) {
		filter = ObjUtil.defaultIfNull(filter, PredicateUtil.alwaysTrue());
		final Class<?> sourceClass = getClassFormAnnotatedElement(annotatedEle);
		final Deque<List<Class<?>>> classDeque = ListUtil.ofLinked(ListUtil.of(sourceClass));
		final Set<Class<?>> accessedTypes = new LinkedHashSet<>();
		int index = 0;
		while (!classDeque.isEmpty()) {
			final List<Class<?>> currClassQueue = classDeque.removeFirst();
			final List<Class<?>> nextClassQueue = new ArrayList<>();
			for (Class<?> targetClass : currClassQueue) {
				targetClass = convert(targetClass);
				// 过滤不需要处理的类
				if (isNotNeedProcess(accessedTypes, targetClass)) {
					continue;
				}
				accessedTypes.add(targetClass);
				// 扫描父类
				scanSuperClassIfNecessary(nextClassQueue, targetClass);
				// 扫描接口
				scanInterfaceIfNecessary(nextClassQueue, targetClass);
				// 处理层级索引和注解
				final Annotation[] targetAnnotations = getAnnotationsFromTargetClass(annotatedEle, index, targetClass);
				for (final Annotation annotation : targetAnnotations) {
					if (!AnnotationUtil.isMetaAnnotation(annotation.annotationType()) && filter.test(annotation)) {
						consumer.accept(index, annotation);
					}
				}
				index++;
			}
			if (CollUtil.isNotEmpty(nextClassQueue)) {
				classDeque.addLast(nextClassQueue);
			}
		}
	}

	/**
	 * 从要搜索的注解元素上获得要递归的类型
	 *
	 * @param annotatedElement 注解元素
	 * @return 要递归的类型
	 */
	protected abstract Class<?> getClassFormAnnotatedElement(AnnotatedElement annotatedElement);

	/**
	 * 从类上获取最终所需的目标注解
	 *
	 * @param source      最初的注解元素
	 * @param index       类的层级索引
	 * @param targetClass 类
	 * @return 最终所需的目标注解
	 */
	protected abstract Annotation[] getAnnotationsFromTargetClass(AnnotatedElement source, int index, Class<?> targetClass);

	/**
	 * 当前类是否不需要处理
	 *
	 * @param accessedTypes 访问类型
	 * @param targetClass   目标类型
	 * @return 是否不需要处理
	 */
	protected boolean isNotNeedProcess(final Set<Class<?>> accessedTypes, final Class<?> targetClass) {
		return ObjUtil.isNull(targetClass)
			|| accessedTypes.contains(targetClass)
			|| excludeTypes.contains(targetClass)
			|| filter.negate().test(targetClass);
	}

	/**
	 * 若{@link #includeInterfaces}为{@code true}，则将目标类的父接口也添加到nextClasses
	 *
	 * @param nextClasses 下一个类集合
	 * @param targetClass 目标类型
	 */
	protected void scanInterfaceIfNecessary(final List<Class<?>> nextClasses, final Class<?> targetClass) {
		if (includeInterfaces) {
			final Class<?>[] interfaces = targetClass.getInterfaces();
			if (ArrayUtil.isNotEmpty(interfaces)) {
				CollUtil.addAll(nextClasses, interfaces);
			}
		}
	}

	/**
	 * 若{@link #includeSuperClass}为{@code true}，则将目标类的父类也添加到nextClasses
	 *
	 * @param nextClassQueue 下一个类队列
	 * @param targetClass    目标类型
	 */
	protected void scanSuperClassIfNecessary(final List<Class<?>> nextClassQueue, final Class<?> targetClass) {
		if (includeSuperClass) {
			final Class<?> superClass = targetClass.getSuperclass();
			if (!ObjUtil.equals(superClass, Object.class) && ObjUtil.isNotNull(superClass)) {
				nextClassQueue.add(superClass);
			}
		}
	}

	/**
	 * 若存在转换器，则使用转换器对目标类进行转换
	 *
	 * @param target 目标类
	 * @return 转换后的类
	 */
	protected Class<?> convert(Class<?> target) {
		if (hasConverters) {
			for (final UnaryOperator<Class<?>> converter : converters) {
				target = converter.apply(target);
			}
		}
		return target;
	}

	/**
	 * 若类型为jdk代理类，则尝试转换为原始被代理类
	 */
	public static class JdkProxyClassConverter implements UnaryOperator<Class<?>> {
		@Override
		public Class<?> apply(final Class<?> sourceClass) {
			return Proxy.isProxyClass(sourceClass) ? apply(sourceClass.getSuperclass()) : sourceClass;
		}
	}

}
