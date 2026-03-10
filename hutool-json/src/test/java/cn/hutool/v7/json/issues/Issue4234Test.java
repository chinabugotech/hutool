/*
 * Copyright (c) 2026 Hutool Team.
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

import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue4234Test {
	@Test
	void getStrTest(){
		final JSONObject entries = JSONUtil.parseObj("{\"a\":\"1\", \"b\": 100}");
		final String a = MapUtil.getStr(entries, "a");
		assertEquals("1", a);

		final Integer b = MapUtil.getInt(entries, "b");
		assertEquals(100, b);
	}
}
