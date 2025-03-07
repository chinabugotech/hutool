package org.dromara.hutool.http;

import org.dromara.hutool.core.net.url.UrlQueryUtil;
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
