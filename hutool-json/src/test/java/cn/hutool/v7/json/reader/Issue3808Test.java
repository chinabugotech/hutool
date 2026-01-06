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

package cn.hutool.v7.json.reader;

import cn.hutool.v7.core.io.resource.ResourceUtil;
import cn.hutool.v7.core.util.CharsetUtil;
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONConfig;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3808Test {
	@Test
	void parseEscapeZeroWithCharTest() {
		final String str = ResourceUtil.readStr("issue3808.json", CharsetUtil.UTF_8);
		final JSON parse = JSONUtil.parse(str, JSONConfig.of().setIgnoreZeroWithChar(false));
		Assertions.assertEquals("{\"recommend_text\":\"✅宁波，\\u200c一座历史悠久的文化名城\\n你好\",\"，\\u200c一\":\"aaa\"}", parse.toString());
	}

	@Test
	void parseIgnoreZeroWithCharTest() {
		final String str = ResourceUtil.readStr("issue3808.json", CharsetUtil.UTF_8);
		final JSON parse = JSONUtil.parse(str, JSONConfig.of().setIgnoreZeroWithChar(true));
		Assertions.assertEquals("{\"recommend_text\":\"✅宁波，一座历史悠久的文化名城\\n你好\",\"，一\":\"aaa\"}", parse.toString());
	}
}
