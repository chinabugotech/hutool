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

package cn.hutool.v7.http;

import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.map.MapUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IssueIDDHNSTest {

	@Test
	@Disabled
	void postTest(){
		final Map<String, Object> map = MapUtil.ofKvs(false,
			"file", FileUtil.file("bd.jpg"),
			"path", "/opt/B",
			"ojb", "{\"test\":\"test\"}"
		);

		final String post = HttpUtil.post("http://127.0.0.1:8080/upload", map);
		Console.log(post);
	}
}
