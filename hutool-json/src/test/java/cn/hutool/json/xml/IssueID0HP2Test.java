package cn.hutool.json.xml;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueID0HP2Test {

	/**
	 * JSON转换为XML时使用默认的日期格式，并不能自定义格式，日期格式只用于生成JSON字符串
	 */
	@Test
	void jsonWithDateToXmlTest() {
		final JSONObject json = JSONUtil.createObj(JSONConfig.create().setDateFormat("yyyy/MM/dd"))
			.set("date", DateUtil.parse("2025-10-03"));
		String xml = JSONUtil.toXmlStr(json);
		Assertions.assertEquals("<date>2025-10-03 00:00:00</date>", xml);
	}
}
