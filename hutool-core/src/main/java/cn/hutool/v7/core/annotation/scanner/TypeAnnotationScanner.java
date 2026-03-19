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

import cn.hutool.v7.core.collection.set.SetUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 扫描{@link Class}上的注解
 *
 * @author huangchengxing
 */
public class TypeAnnotationScanner extends AbstractTypeAnnotationScanner<TypeAnnotationScanner> implements AnnotationScanner {

	/**
	 * 构造一个类注解扫描器
	 *
	 * @param includeSupperClass 是否允许扫描父类
	 * @param includeInterfaces  是否允许扫描父接口
	 * @param filter             过滤器
	 * @param excludeTypes       不包含的类型
	 */
	public TypeAnnotationScanner(final boolean includeSupperClass, final boolean includeInterfaces, final Predicate<Class<?>> filter, final Set<Class<?>> excludeTypes) {
		super(includeSupperClass, includeInterfaces, filter, excludeTypes);
	}

	/**
	 * 构建一个类注解扫描器，默认允许扫描指定元素的父类以及父接口
	 */
	public TypeAnnotationScanner() {
		this(true, true, t -> true, SetUtil.ofLinked());
	}

	/**
	 * 判断是否支持扫描该注解元素，仅当注解元素是{@link Class}接时返回{@code true}
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @return 是否支持扫描该注解元素
	 */
	@Override
	public boolean support(final AnnotatedElement annotatedEle) {
		return annotatedEle instanceof Class;
	}

	/**
	 * 将注解元素转为{@link Class}
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @return 要递归的类型
	 */
	@Override
	protected Class<?> getClassFormAnnotatedElement(final AnnotatedElement annotatedEle) {
		return (Class<?>)annotatedEle;
	}

	/**
	 * 获取{@link Class#getAnnotations()}
	 *
	 * @param source      最初的注解元素
	 * @param index       类的层级索引
	 * @param targetClass 类
	 * @return 类上直接声明的注解
	 */
	@Override
	protected Annotation[] getAnnotationsFromTargetClass(final AnnotatedElement source, final int index, final Class<?> targetClass) {
		return targetClass.getAnnotations();
	}

	/**
	 * 是否允许扫描父类
	 *
	 * @param includeSuperClass 是否允许扫描父类
	 * @return 当前实例
	 */
	@Override
	public TypeAnnotationScanner setIncludeSuperClass(final boolean includeSuperClass) {
		return super.setIncludeSuperClass(includeSuperClass);
	}

	/**
	 * 是否允许扫描父接口
	 *
	 * @param includeInterfaces 是否允许扫描父类
	 * @return 当前实例
	 */
	@Override
	public TypeAnnotationScanner setIncludeInterfaces(final boolean includeInterfaces) {
		return super.setIncludeInterfaces(includeInterfaces);
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
