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

import cn.hutool.v7.core.bean.BeanDesc;
import cn.hutool.v7.core.bean.BeanUtil;
import cn.hutool.v7.core.bean.copier.ValueProviderToBeanCopier;
import cn.hutool.v7.core.lang.copier.Copier;
import cn.hutool.v7.core.reflect.ConstructorUtil;
import cn.hutool.v7.core.reflect.TypeUtil;
import cn.hutool.v7.core.util.ObjUtil;
import cn.hutool.v7.json.support.InternalJSONUtil;
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONConfig;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.serializer.JSONContext;
import cn.hutool.v7.json.serializer.MatcherJSONDeserializer;
import cn.hutool.v7.json.serializer.MatcherJSONSerializer;
import cn.hutool.v7.json.support.BeanToJSONCopier;
import cn.hutool.v7.json.support.JSONObjectValueProvider;

import java.lang.reflect.Type;

/**
 * Bean对象适配器，将Bean对象序列化为JSONObject，反序列化为Bean对象
 *
 * @author Looly
 * @since 7.0.0
 */
public class BeanTypeAdapter implements MatcherJSONSerializer<Object>, MatcherJSONDeserializer<Object> {

	/**
	 * 单例
	 */
	public static final BeanTypeAdapter INSTANCE = new BeanTypeAdapter();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		final JSON contextJson = ObjUtil.apply(context, JSONContext::getContextJson);
		final BeanDesc beanDesc = BeanUtil.getBeanDesc(bean.getClass());
		if(beanDesc.isEmpty()){
			// 空Bean按照Bean对待
			return true;
		}

		final boolean isTransparent = ObjUtil.defaultIfNull(
			ObjUtil.apply(contextJson, JSON::config), JSONConfig::isTransientSupport, true);
		return beanDesc.isReadable(isTransparent)
			&& (null == contextJson || contextJson instanceof JSONObject);
	}

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		return json instanceof JSONObject &&
			// 空对象转目标对象不限制目标是否可写
			(json.isEmpty() || BeanUtil.isWritableBean(TypeUtil.getClass(deserializeType)));
	}

	@Override
	public JSON serialize(final Object bean, final JSONContext context) {
		final BeanToJSONCopier copier = new BeanToJSONCopier(
			bean, context.getOrCreateObj(), context.getFactory());
		return copier.copy();
	}

	@Override
	public Object deserialize(final JSON json, final Type deserializeType) {
		final Object target = ConstructorUtil.newInstanceIfPossible(TypeUtil.getClass(deserializeType));
		if(json.isEmpty()){
			//issue#3649，对于空对象转目标对象，直接实例化一个空对象
			return target;
		}
		final Copier<Object> copier = new ValueProviderToBeanCopier<>(
			new JSONObjectValueProvider((JSONObject) json),
			target,
			deserializeType,
			InternalJSONUtil.toCopyOptions(json.config())
		);
		return copier.copy();
	}
}
