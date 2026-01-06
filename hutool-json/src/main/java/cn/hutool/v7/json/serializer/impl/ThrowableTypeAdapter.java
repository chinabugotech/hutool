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

package cn.hutool.v7.json.serializer.impl;

import cn.hutool.v7.core.reflect.ConstructorUtil;
import cn.hutool.v7.core.reflect.TypeUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.ObjUtil;
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONPrimitive;
import cn.hutool.v7.json.serializer.JSONContext;
import cn.hutool.v7.json.serializer.MatcherJSONDeserializer;
import cn.hutool.v7.json.serializer.MatcherJSONSerializer;

import java.lang.reflect.Type;

/**
 * Throwable类型适配器，用于将Throwable对象转换为JSON对象
 *
 * @author Looly
 * @since 6.0.0
 */
public class ThrowableTypeAdapter implements MatcherJSONSerializer<Throwable>, MatcherJSONDeserializer<Throwable> {

	/**
	 * 单例
	 */
	public static final ThrowableTypeAdapter INSTANCE = new ThrowableTypeAdapter();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof Throwable;
	}

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		return Throwable.class.isAssignableFrom(TypeUtil.getClass(deserializeType));
	}

	@Override
	public JSON serialize(final Throwable bean, final JSONContext context) {
		return new JSONPrimitive(
			StrUtil.format("{}: {}", bean.getClass().getName(), bean.getMessage()),
			ObjUtil.apply(context, JSONContext::config));
	}

	@Override
	public Throwable deserialize(final JSON json, final Type deserializeType) {
		final String value = (String) json.asJSONPrimitive().getValue();
		return (Throwable) ConstructorUtil.newInstance(TypeUtil.getClass(deserializeType),
			StrUtil.subAfter(value, ": ", false));
	}
}
