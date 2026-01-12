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

import cn.hutool.v7.core.lang.tuple.Triple;
import cn.hutool.v7.core.reflect.TypeReference;
import cn.hutool.v7.core.reflect.TypeUtil;
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.serializer.JSONDeserializer;

import java.lang.reflect.Type;

/**
 * 三元组反序列化器
 *
 * @author Looly
 * @since 7.0.0
 */
public class TripleDeserializer implements JSONDeserializer<Triple<?, ?, ?>> {

	/**
	 * 单例
	 */
	public static final TripleDeserializer INSTANCE = new TripleDeserializer();

	@Override
	public Triple<?, ?, ?> deserialize(final JSON json, Type deserializeType) {
		if (deserializeType instanceof TypeReference) {
			deserializeType = ((TypeReference<?>) deserializeType).getType();
		}
		final Type leftType = TypeUtil.getTypeArgument(deserializeType, 0);
		final Type middileType = TypeUtil.getTypeArgument(deserializeType, 1);
		final Type rightType = TypeUtil.getTypeArgument(deserializeType, 2);

		final JSONObject jsonObject = json.asJSONObject();
		final JSON left = jsonObject.get("left");
		final JSON middle = jsonObject.get("middle");
		final JSON right = jsonObject.get("right");

		return Triple.of(
			left.toBean(leftType),
			middle.toBean(middileType),
			right.toBean(rightType)
		);
	}
}
