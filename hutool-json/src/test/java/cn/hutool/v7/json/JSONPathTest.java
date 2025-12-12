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

package cn.hutool.v7.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * JSON路径单元测试
 *
 * @author Looly
 *
 */
public class JSONPathTest {

	@Test
	public void getByPathTest() {
		final String json = "[{\"id\":\"1\",\"name\":\"xingming\"},{\"id\":\"2\",\"name\":\"mingzi\"}]";
		Object value = JSONUtil.parseArray(json).getByPath("[0].name", Object.class);
		assertEquals("xingming", value);
		value = JSONUtil.parseArray(json).getByPath("[1].name", Object.class);
		assertEquals("mingzi", value);
	}

	@Test
	public void getByPathTest2(){
		final String str = "{'accountId':111}";
		final JSON json = JSONUtil.parse(str);
		final Long accountId = JSONUtil.getByPath(json, "$.accountId", 0L);
		assertEquals(111L, accountId.longValue());
	}

	@Test
	void issueIDC78BTest() {
		final String json = "{\"actionMessage\":{\"alertResults\":[],\"decodeFeas\":[{\"body\":{\"lats\":[{\"begin\":4260,\"text\":\"呵呵\"},{\"begin\":4260,\"text\":\"你好 \"}]}}]}}";
		final JSON json1 = JSONUtil.parse(json);
		final JSON byPath = json1.getByPath("$.actionMessage.decodeFeas[0].body.lats[*]");
		assertInstanceOf(JSONArray.class, byPath);
		assertEquals(2, byPath.size());

		final JSON byPath2 = json1.getByPath("$.actionMessage.decodeFeas[0].body.lats[*].text");
		assertInstanceOf(JSONArray.class, byPath2);
		assertEquals(2, byPath2.size());
		assertEquals("呵呵", byPath2.asJSONArray().getStr(0));
		assertEquals("你好 ", byPath2.asJSONArray().getStr(1));
	}
}
