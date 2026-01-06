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
