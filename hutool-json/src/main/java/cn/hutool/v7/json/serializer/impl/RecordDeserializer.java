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

import cn.hutool.v7.core.bean.RecordUtil;
import cn.hutool.v7.core.reflect.TypeUtil;
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.support.JSONObjectValueProvider;
import cn.hutool.v7.json.serializer.MatcherJSONDeserializer;

import java.lang.reflect.Type;

/**
 * Record反序列化器，用于将JSON对象转换为Record类型对象。
 *
 * @author Looly
 * @since 6.0.0
 */
public class RecordDeserializer implements MatcherJSONDeserializer<Object> {

	/**
	 * 单例
	 */
	public static final RecordDeserializer INSTANCE = new RecordDeserializer();

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		if(json instanceof JSONObject){
			final Class<?> rawType = TypeUtil.getClass(deserializeType);
			return RecordUtil.isRecord(rawType);
		}
		return false;
	}

	@Override
	public Object deserialize(final JSON json, final Type deserializeType) {
		return RecordUtil.newInstance(TypeUtil.getClass(deserializeType),
			new JSONObjectValueProvider((JSONObject) json));
	}
}
