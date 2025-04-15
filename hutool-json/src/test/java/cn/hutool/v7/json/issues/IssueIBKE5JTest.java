package cn.hutool.v7.json.issues;

import lombok.Data;
import cn.hutool.v7.json.JSONArray;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IssueIBKE5JTest {

	@Test
	public void test() {
		final TestPojo testPojo = new TestPojo();
		final Map<String, Object> dataMap = new HashMap<>();
		testPojo.setDataMap(dataMap);
		dataMap.put("limit", 5);
		dataMap.put("booleanList", Arrays.asList(true, false));
		final String json = JSONUtil.toJsonStr(testPojo);
		Assertions.assertEquals("{\"dataMap\":{\"limit\":5,\"booleanList\":[true,false]}}", json);

		// 由于Map的值类型为Object类型，因此解析后，booleanList为JSONArray类型
		final TestPojo bean = JSONUtil.toBean(json, TestPojo.class);
		Assertions.assertEquals(JSONArray.class, bean.getDataMap().get("booleanList").getClass());
	}

	@Data
	public static class TestPojo {
		private Map<String, Object> dataMap;
	}
}
