package cn.hutool.v7.json.xml;

import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.json.JSONConfig;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueID0HP2Test {
	/**
	 * JSON转换为XML时使用自定义格式<br>
	 * putValue时就会把日期转为自定义格式的字符串，因此转XML是直接转换
	 */
	@Test
	void jsonWithDateToXmlTest() {
		final JSONObject json = JSONUtil.ofObj(JSONConfig.of().setDateFormat("yyyy/MM/dd"))
			.putValue("date", DateUtil.parse("2025-10-03"));

		final String xml = JSONUtil.toXmlStr(json);
		Assertions.assertEquals("<date>2025/10/03</date>", xml);
	}
}
