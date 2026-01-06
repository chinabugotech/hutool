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

import cn.hutool.v7.core.convert.ConvertUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.text.split.SplitUtil;
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import cn.hutool.v7.json.serializer.JSONContext;
import cn.hutool.v7.json.serializer.MatcherJSONSerializer;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * {@link ResourceBundle}序列化器
 *
 * @author Looly
 * @since 6.0.0
 */
public class ResourceBundleSerializer implements MatcherJSONSerializer<ResourceBundle> {

	/**
	 * 单例
	 */
	public static final ResourceBundleSerializer INSTANCE = new ResourceBundleSerializer();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof ResourceBundle;
	}

	@Override
	public JSON serialize(final ResourceBundle bean, final JSONContext context) {
		final JSONObject result = context.getOrCreateObj();
		mapFromResourceBundle(bean, result);
		return result;
	}

	/**
	 * 从{@link ResourceBundle}转换
	 *
	 * @param bundle     ResourceBundle
	 * @param jsonObject {@link JSONObject}
	 */
	private void mapFromResourceBundle(final ResourceBundle bundle, final JSONObject jsonObject) {
		final Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			final String key = keys.nextElement();
			if (key != null) {
				propertyPut(jsonObject, key, bundle.getString(key));
			}
		}
	}

	/**
	 * 将Property的键转化为JSON形式<br>
	 * 用于识别类似于：cn.hutool.v7.json这类用点隔开的键<br>
	 * 注意：不允许重复键
	 *
	 * @param jsonObject JSONObject
	 * @param key        键
	 * @param value      值
	 */
	private static void propertyPut(final JSONObject jsonObject, final Object key, final Object value) {
		final String[] path = SplitUtil.splitToArray(ConvertUtil.toStr(key), StrUtil.DOT);
		final int last = path.length - 1;
		JSONObject target = jsonObject;
		for (int i = 0; i < last; i += 1) {
			final String segment = path[i];
			JSONObject nextTarget = target.getJSONObject(segment);
			if (nextTarget == null) {
				nextTarget = JSONUtil.ofObj(target.config());
				target.putValue(segment, nextTarget);
			}
			target = nextTarget;
		}
		target.putValue(path[last], value);
	}
}
