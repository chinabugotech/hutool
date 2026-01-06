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

import cn.hutool.v7.core.bean.BeanUtil;
import cn.hutool.v7.core.convert.ConvertException;
import cn.hutool.v7.core.convert.Converter;
import cn.hutool.v7.core.convert.ConverterWithRoot;
import cn.hutool.v7.core.lang.tuple.Pair;
import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.core.reflect.TypeReference;
import cn.hutool.v7.core.reflect.TypeUtil;
import cn.hutool.v7.core.text.CharUtil;
import cn.hutool.v7.core.text.StrUtil;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * {@link Pair} 转换器，支持以下类型转为Pair
 * <ul>
 *     <li>{@link Map}</li>
 *     <li>{@link Map.Entry}</li>
 *     <li>带分隔符的字符串，支持分隔符{@code :}、{@code =}、{@code ,}</li>
 *     <li>Bean，包含{@code getLeft}和{@code getRight}方法</li>
 * </ul>
 *
 * @author Looly
 */
public class PairConverter extends ConverterWithRoot implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param rootConverter 根转换器，用于转换Pair中的值
	 */
	public PairConverter(final Converter rootConverter) {
		super(rootConverter);
	}

	@Override
	public Object convert(Type targetType, final Object value) throws ConvertException {
		if (targetType instanceof TypeReference) {
			targetType = ((TypeReference<?>) targetType).getType();
		}
		final Type leftType = TypeUtil.getTypeArgument(targetType, 0);
		final Type rightType = TypeUtil.getTypeArgument(targetType, 1);

		return convert(leftType, rightType, value);
	}

	/**
	 * 转换对象为指定键值类型的指定类型Map
	 *
	 * @param leftType  键类型
	 * @param rightType 值类型
	 * @param value     被转换的值
	 * @return 转换后的Map
	 * @throws ConvertException 转换异常或不支持的类型
	 */
	@SuppressWarnings("rawtypes")
	public Pair<?, ?> convert(final Type leftType, final Type rightType, final Object value)
		throws ConvertException {
		Map map = null;
		if (value instanceof final Map.Entry entry) {
			map = MapUtil.of(entry.getKey(), entry.getValue());
		} else if (value instanceof Pair) {
			final Pair entry = (Pair<?, ?>) value;
			map = MapUtil.of(entry.getLeft(), entry.getRight());
		} else if (value instanceof Map) {
			map = (Map) value;
		} else if (value instanceof final CharSequence str) {
			map = strToMap(str);
		} else if (BeanUtil.isReadableBean(value.getClass())) {
			// 一次性只读场景，包装为Map效率更高
			map = BeanUtil.toBeanMap(value);
		}

		if (null != map) {
			return mapToPair(leftType, rightType, map);
		}

		throw new ConvertException("Unsupported to map from [{}] of type: {}", value, value.getClass().getName());
	}

	/**
	 * 字符串转单个键值对的Map，支持分隔符{@code :}、{@code =}、{@code ,}
	 *
	 * @param str 字符串
	 * @return map or null
	 */
	private static Map<CharSequence, CharSequence> strToMap(final CharSequence str) {
		// key:value  key=value  key,value
		final int index = StrUtil.indexOf(str,
			c -> c == CharUtil.COLON || c == CharUtil.EQUAL || c == CharUtil.COMMA,
			0, str.length());

		if (index > -1) {
			return MapUtil.of(str.subSequence(0, index), str.subSequence(index + 1, str.length()));
		}
		return null;
	}

	/**
	 * Map转Pair
	 *
	 * @param keyType   键类型
	 * @param valueType 值类型
	 * @param map       被转换的map
	 * @return Pair
	 */
	@SuppressWarnings("rawtypes")
	private Pair<?, ?> mapToPair(final Type keyType, final Type valueType, final Map map) {

		final Object left;
		final Object right;
		if (1 == map.size()) {
			final Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();
			left = entry.getKey();
			right = entry.getValue();
		} else {
			// 忽略Map中其它属性
			left = map.get("left");
			right = map.get("right");
		}

		return Pair.of(
			TypeUtil.isUnknown(keyType) ? left : rootConverter.convert(keyType, left),
			TypeUtil.isUnknown(valueType) ? right : rootConverter.convert(valueType, right)
		);
	}
}
