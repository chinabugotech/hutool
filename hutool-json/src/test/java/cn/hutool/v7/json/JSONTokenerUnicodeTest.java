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

package cn.hutool.v7.json;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONTokenerUnicodeTest {

	@Test
	public void validUnicodeTest() {
		final String json = "{\"\\u006eame\":\"\\u4e2d\\u6587\",\"face\":\"\\uD83D\\uDE00\"}";
		final JSONObject object = JSONUtil.parseObj(json);
		assertEquals("中文", object.getStr("name"));
		assertEquals("😀", object.getStr("face"));
		assertEquals("中文", JSONUtil.toBean(json, UnicodeBean.class).getName());
	}

	@Getter
	@Setter
	public static class UnicodeBean {
		private String name;
	}
}
