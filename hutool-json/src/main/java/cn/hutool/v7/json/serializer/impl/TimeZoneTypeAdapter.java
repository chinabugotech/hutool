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

package cn.hutool.v7.json.serializer.impl;

import cn.hutool.v7.core.reflect.TypeUtil;
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONPrimitive;
import cn.hutool.v7.json.serializer.JSONContext;
import cn.hutool.v7.json.serializer.MatcherJSONDeserializer;
import cn.hutool.v7.json.serializer.MatcherJSONSerializer;

import java.lang.reflect.Type;
import java.util.TimeZone;

/**
 * 时区类型适配器
 *
 * @author Looly
 * @since 6.0.0
 */
public class TimeZoneTypeAdapter implements MatcherJSONSerializer<TimeZone>, MatcherJSONDeserializer<TimeZone> {

	/**
	 * 单例
	 */
	public static final TimeZoneTypeAdapter INSTANCE = new TimeZoneTypeAdapter();

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		return TimeZone.class.isAssignableFrom(TypeUtil.getClass(deserializeType));
	}

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof TimeZone;
	}

	@Override
	public JSON serialize(final TimeZone bean, final JSONContext context) {
		return new JSONPrimitive(bean.getID(), context.config());
	}

	@Override
	public TimeZone deserialize(final JSON json, final Type deserializeType) {
		return TimeZone.getTimeZone(json.toString());
	}
}
