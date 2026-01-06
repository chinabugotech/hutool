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

import cn.hutool.v7.core.reflect.TypeReference;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IssueIAWE3HTest {
	@Test
	void toMapTest() {
		final String jsonStr = """
			{
			  "reply": "hi",
			  "solved": true,
			  "notifyTypes": [
			    "push"
			  ]
			}""";

		final JSONObject jsonObject = JSONUtil.parseObj(jsonStr);

		final Map<String, Object> map = jsonObject.toBean(new TypeReference<Map<String, Object>>() {});
		Assertions.assertEquals("hi", map.get("reply"));

		final Map<String, Object> map2 = jsonObject.toMap(String.class, Object.class);
		Assertions.assertEquals("hi", map2.get("reply"));
	}
}
