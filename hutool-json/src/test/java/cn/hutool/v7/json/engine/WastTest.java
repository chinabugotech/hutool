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

package cn.hutool.v7.json.engine;

import cn.hutool.v7.core.text.StrUtil;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WastTest {

	final String jsonStr = """
		{
		  "name": "张三",
		  "age": 18,
		  "birthday": "2020-01-01",
		  "booleanValue": true,
		  "jsonObjectSub": {
		    "subStr": "abc",
		    "subNumber": 150343445454,
		    "subBoolean": true
		  },
		  "jsonArraySub": [
		    "abc",
		    123,
		    false
		  ]
		}""";

	@Test
	void prettyPrintTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("wast");
		engine.init(JSONEngineConfig.of().setPrettyPrint(true));

		final JSONEngineTest.TestBean testBean = new JSONEngineTest.TestBean("张三", 18, true);
		String jsonString = engine.toJsonString(testBean);
		// 使用统一换行符
		jsonString = StrUtil.removeAll(jsonString, '\r');
		assertEquals("""
			{
				"age":18,
				"gender":true,
				"name":"张三"
			}""", jsonString);
	}

	@Test
	void fromJsonStringTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("wast");
		final Map<String, Object> resultMap = engine.fromJsonString(jsonStr, Map.class);
		assertNotNull(resultMap);
		assertEquals(6, resultMap.size());
	}

	@Test
	void deserializeTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("wast");
		final Map<String, Object> resultMap = engine.deserialize(new StringReader(jsonStr), Map.class);
		assertNotNull(resultMap);
		assertEquals(6, resultMap.size());
	}
}
