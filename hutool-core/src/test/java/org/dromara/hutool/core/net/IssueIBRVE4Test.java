package org.dromara.hutool.core.net;

import org.dromara.hutool.core.net.url.UrlQueryUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIBRVE4Test {
	@Test
	public void decodeParamMapNoParamTest() {
		// 参数值不存在分界标记等号时
		// 无参数值时
		final Map<String, String> paramMap = UrlQueryUtil.decodeQuery("https://hutool.cn/api.action", CharsetUtil.UTF_8);
	assertEquals(0,paramMap.size());
}
@Test
	public void decodeParamMapListNoParamTest() {
		// 参数值不存在分界标记等号时
		// 无参数值时
		final Map<String, String> paramMap1 = UrlQueryUtil.decodeQuery("https://hutool.cn/api.action", CharsetUtil.UTF_8);
		assertEquals(0,paramMap1.size());
	}
}
