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

import cn.hutool.v7.core.convert.ConvertException;
import cn.hutool.v7.core.convert.Converter;
import cn.hutool.v7.core.lang.tuple.Tuple;

import java.lang.reflect.Type;

/**
 * {@link Tuple}转换器
 *
 * @author Looly
 * @since 7.0.0
 */
public class TupleConverter implements Converter {

	/**
	 * 单例
	 */
	public static final TupleConverter INSTANCE = new TupleConverter();

	@Override
	public Object convert(final Type targetType, final Object value) throws ConvertException {
		final Object[] convert = ArrayConverter.INSTANCE.convert(Object[].class, value);
		return Tuple.of(convert);
	}
}
