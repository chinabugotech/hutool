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

import cn.hutool.v7.core.io.resource.Resource;
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONFactory;
import cn.hutool.v7.json.reader.JSONParser;
import cn.hutool.v7.json.reader.JSONTokener;
import cn.hutool.v7.json.serializer.JSONContext;
import cn.hutool.v7.json.serializer.MatcherJSONSerializer;

/**
 * {@link Resource}序列化器
 *
 * @author Looly
 * @since 7.0.0
 */
public class ResourceSerializer implements MatcherJSONSerializer<Resource> {

	/**
	 * 单例
	 */
	public static final ResourceSerializer INSTANCE = new ResourceSerializer();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof Resource;
	}

	@Override
	public JSON serialize(final Resource bean, final JSONContext context) {
		return context.getFactory().ofParser(new JSONTokener(bean.getStream(), context.config().isIgnoreZeroWithChar())).parse();
	}

	/**
	 * 从{@link JSONTokener} 中读取JSON字符串，并转换为JSON
	 *
	 * @param tokener {@link JSONTokener}
	 * @return JSON
	 */
	private JSON mapFromTokener(final JSONTokener tokener, final JSONFactory factory) {
		return JSONParser.of(tokener, factory).parse();
	}
}
