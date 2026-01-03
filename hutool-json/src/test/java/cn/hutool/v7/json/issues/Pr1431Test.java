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

import cn.hutool.v7.json.JSONConfig;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Pr1431Test {
	@Test
	void filterTest() {
		final UserC user = new UserC();
		user.setId(1);
		user.setName("张三");
		user.setProp("123456");

		final JSONObject entries = JSONUtil.parseObj(user, JSONConfig.of(), entry -> !entry.getKey().equals("prop"));
		assertEquals(2, entries.size());
		assertTrue(entries.containsKey("id"));
		assertTrue(entries.containsKey("name"));
	}

	@Data
	static class UserC {
		private int id;
		private String name;
		private String prop;
	}
}
