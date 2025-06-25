/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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
import cn.hutool.v7.core.convert.Converter;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.reflect.TypeUtil;
import cn.hutool.v7.core.text.StrUtil;

import java.io.Serial;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

/**
 * {@link Reference}转换器
 *
 * @author Looly
 * @since 3.0.8
 */
@SuppressWarnings("rawtypes")
public class ReferenceConverter extends AbstractConverter {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 */
	private final Converter rootConverter;

	/**
	 * 构造
	 *
	 * @param rootConverter 根转换器，用于转换Reference泛型的类型
	 */
	public ReferenceConverter(final Converter rootConverter) {
		this.rootConverter = Assert.notNull(rootConverter);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Reference<?> convertInternal(final Class<?> targetClass, final Object value) {

		//尝试将值转换为Reference泛型的类型
		Object targetValue = null;
		final Type paramType = TypeUtil.getTypeArgument(targetClass);
		if (!TypeUtil.isUnknown(paramType)) {
			targetValue = rootConverter.convert(paramType, value);
		}
		if (null == targetValue) {
			targetValue = value;
		}

		if (targetClass == WeakReference.class) {
			return new WeakReference(targetValue);
		} else if (targetClass == SoftReference.class) {
			return new SoftReference(targetValue);
		}

		throw new UnsupportedOperationException(StrUtil.format("Unsupport Reference type: {}", targetClass.getName()));
	}

}
