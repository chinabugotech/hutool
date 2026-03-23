package cn.hutool.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class IssueIBQIYQTest {
	@Test
	void normalizeParamsTest1() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", "");
		map.put("type", "4");
		String url = HttpUtil.toParams(map);
		Assertions.assertEquals("id=&type=4", url);
		url = HttpUtil.normalizeParams(url, null);
		Assertions.assertEquals("id=&type=4", url);
	}
}
