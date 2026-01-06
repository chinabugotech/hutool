/*
 * Copyright (c) 2013-2026 Hutool Team.
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

package cn.hutool.v7.core.convert.impl;

import cn.hutool.v7.core.convert.AbstractConverter;
import cn.hutool.v7.core.classloader.ClassLoaderUtil;
import cn.hutool.v7.core.convert.MatcherConverter;

import java.io.Serial;
import java.lang.reflect.Type;

/**
 * 类转换器<br>
 * 将类名转换为类，默认初始化这个类（执行static块）
 *
 * @author Looly
 */
public class ClassConverter extends AbstractConverter implements MatcherConverter {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final ClassConverter INSTANCE = new ClassConverter();

	/**
	 * 是否初始化这个类（执行static块）
	 */
	private final boolean isInitialized;

	/**
	 * 构造
	 */
	public ClassConverter() {
		this(true);
	}

	/**
	 * 构造
	 *
	 * @param isInitialized 是否初始化类（调用static模块内容和初始化static属性）
	 * @since 5.5.0
	 */
	public ClassConverter(final boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	@Override
	public boolean match(final Type targetType, final Class<?> rawType, final Object value) {
		return "java.lang.Class".equals(rawType.getName());
	}

	@Override
	protected Class<?> convertInternal(final Class<?> targetClass, final Object value) {
		return ClassLoaderUtil.loadClass(convertToStr(value), isInitialized);
	}

}
