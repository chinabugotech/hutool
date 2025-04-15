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

package cn.hutool.v7.json.issues;

import cn.hutool.v7.core.lang.Opt;
import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue3681Test {
	@Test
	void toJsonStrOfOptionalTest() {
		String abc = JSONUtil.toJsonStr(Optional.of("abc"));
		assertEquals("\"abc\"", abc);

		// 默认解析
		abc = JSONUtil.toJsonStr(Optional.of("123"));
		assertEquals("123", abc);

		// 按照字符串对待
		abc = JSONUtil.toJSON(Optional.of("123")).toString();
		assertEquals("\"123\"", abc);
	}

	@Test
	void toJsonStrOfOptionalTest2() {
		final String abc = JSONUtil.toJsonStr(Optional.of(MapUtil.of("a", 1)));
		assertEquals("{\"a\":1}", abc);
	}

	@Test
	void toJsonStrOfOptTest() {
		String abc = JSONUtil.toJsonStr(Opt.of("abc"));
		assertEquals("\"abc\"", abc);

		abc = JSONUtil.toJsonStr(Opt.of("123"));
		assertEquals("123", abc);

		// 按照字符串对待
		abc = JSONUtil.toJSON(Opt.of("123")).toString();
		assertEquals("\"123\"", abc);
	}
}
