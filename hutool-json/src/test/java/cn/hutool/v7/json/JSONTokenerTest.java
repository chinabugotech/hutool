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

package cn.hutool.v7.json;

import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.io.resource.ResourceUtil;
import cn.hutool.v7.json.reader.JSONTokener;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JSONTokenerTest {
	@Test
	void parseTest() {
		final JSONObject jsonObject = JSONUtil.parseObj(ResourceUtil.getUtf8Reader("issue1200.json"));
		assertNotNull(jsonObject);
		//Console.log(jsonObject.toStringPretty());
	}

	@Test
	void nextTest() {
		final JSONTokener jsonTokener = new JSONTokener("{\"ab\": \"abc\"}", true);
		final char c = jsonTokener.nextTokenChar();
		assertEquals('{', c);
		assertEquals("ab", jsonTokener.nextString());
		final char c2 = jsonTokener.nextTokenChar();
		assertEquals(':', c2);
		assertEquals("abc", jsonTokener.nextString());


		IoUtil.closeQuietly(jsonTokener);
	}

	/**
	 * 兼容非包装符包装的value和key
	 */
	@Test
	void nextWithoutWrapperTest() {
		final JSONTokener jsonTokener = new JSONTokener("{ab: abc}", true);
		final char c = jsonTokener.nextTokenChar();
		assertEquals('{', c);
		assertEquals("ab", jsonTokener.nextString());
		final char c2 = jsonTokener.nextTokenChar();
		assertEquals(':', c2);
		assertEquals("abc", jsonTokener.nextString());


		IoUtil.closeQuietly(jsonTokener);
	}
}
