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

package cn.hutool.v7.json.issues;

import lombok.Data;
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.reflect.TypeReference;
import cn.hutool.v7.json.JSONConfig;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue3873Test {
	/**
	 * 类型引用数组泛型丢失
	 */
	@Test
	public void toBeanTest() {
		final String json = "{\"results\":[{\"uid\":\"1\"}],\"offset\":0,\"limit\":20,\"total\":0}";
		final Results<Index> deserialize = JSONUtil.toBean(json, JSONConfig.of(), (new TypeReference<Results<Index>>() {}));

		assertEquals(Results.class, deserialize.getClass());
		assertEquals(ArrayUtil.getArrayType(Index.class), deserialize.results.getClass());
	}

	@Data
	public static class Results<T> {
		public T[] results;
	}

	@Data
	public static class Index {
		public String uid;
	}
}
