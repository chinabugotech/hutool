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

package cn.hutool.v7.json.serializer;

import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONFactory;

/**
 * 简单的JSON上下文，用于在JSON序列化时提供配置项
 *
 * @author Looly
 * @since 6.0.0
 */
public class SimpleJSONContext implements JSONContext {

	private final JSON contextJson;
	private final JSONFactory factory;

	/**
	 * 构造
	 *
	 * @param contextJson   JSON对象
	 * @param factory 配置项
	 */
	public SimpleJSONContext(final JSON contextJson, final JSONFactory factory) {
		this.contextJson = contextJson;
		this.factory = factory;
	}

	@Override
	public JSON getContextJson() {
		return this.contextJson;
	}

	@Override
	public JSONFactory getFactory() {
		return factory;
	}
}
