package cn.hutool.json;

import cn.hutool.core.map.MapUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IssueID61QRTest {
	@Test
	public void testName() {
		JSONObject map1 = JSONUtil.createObj(new JSONConfig().setDateFormat("yyyy"));
		// JSONObject map1 = JSONUtil.createObj();
		map1.set("a", 3);
		map1.set("b", 5);
		map1.set("c", 5432);
		Assertions.assertEquals("{c=5432, b=5, a=3}", MapUtil.sortByValue(JSONUtil.toBean(map1, Map.class), true).toString());
	}
}
