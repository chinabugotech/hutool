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

import cn.hutool.v7.core.net.url.UrlQueryUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class IssueIBQIYQTest {
	@Test
	void normalizeQueryTest() {
		final Map<String, Object> map = new HashMap<>();
		map.put("id", "");
		map.put("type", "4");
		String url = UrlQueryUtil.toQuery(map);
		Assertions.assertEquals("id=&type=4", url);
		url = UrlQueryUtil.normalizeQuery(url, null);
		Assertions.assertEquals("id=&type=4", url);
	}

	@Test
	void normalizeQueryTest2() {
		final Map<String, Object> map = new HashMap<>();
		map.put("id", "");
		map.put("type", "4");
		map.put("", "3");
		String url = UrlQueryUtil.toQuery(map);
		// 根据HTTP协议和URL规范（RFC 3986），name为空是合法的，例如：?value 或 ?=value
		Assertions.assertEquals("=3&id=&type=4", url);
		url = UrlQueryUtil.normalizeQuery(url, null);
		Assertions.assertEquals("=3&id=&type=4", url);
	}
}
