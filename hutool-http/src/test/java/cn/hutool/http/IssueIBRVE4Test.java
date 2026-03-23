package cn.hutool.http;

import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIBRVE4Test {
	@Test
	public void decodeParamMapNoParamTest() {
		// 参数值不存在分界标记等号时
		// 无参数值时
		final Map<String, List<String>> paramMap = HttpUtil.decodeParams("https://hutool.cn/api.action", CharsetUtil.CHARSET_UTF_8);
		assertEquals(0,paramMap.size());
	}
	@Test
	public void decodeParamMapListNoParamTest() {
		// 参数值不存在分界标记等号时
		// 无参数值时
		final Map<String, String> paramMap1 = HttpUtil.decodeParamMap("https://hutool.cn/api.action", CharsetUtil.CHARSET_UTF_8);
		assertEquals(0,paramMap1.size());
	}
}
