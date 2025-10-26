/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.json.engine;

import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.date.DateTime;
import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.date.TimeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GsonTest {
	/**
	 * Gson默认缩进两个空格，使用\n换行符
	 */
	@Test
	void prettyPrintTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("gson");
		engine.init(JSONEngineConfig.of().setPrettyPrint(true));

		final JSONEngineTest.TestBean testBean = new JSONEngineTest.TestBean("张三", 18, true);
		final String jsonString = engine.toJsonString(testBean);
		// 使用统一换行符
		Assertions.assertEquals("""
			{
			  "name": "张三",
			  "age": 18,
			  "gender": true
			}""", jsonString);
	}

	@Test
	void writeLocalDateFormatTest() {
		final DateTime date = DateUtil.parse("2024-01-01 01:12:21");
		final BeanWithLocalDate bean = new BeanWithLocalDate(TimeUtil.of(date).toLocalDate());
		final JSONEngine engine = JSONEngineFactory.createEngine("gson");

		final String jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{\"date\":1704038400000}", jsonString);

		engine.init(JSONEngineConfig.of().setDateFormat("yyyy-MM-dd HH:mm:ss"));
		Assertions.assertEquals("{\"date\":\"2024-01-01 00:00:00\"}", engine.toJsonString(bean));
	}

	@Test
	void arrayToStringTest() {
		final ArrayList<Integer> integers = ListUtil.of(1, 2, 3);
		final JSONEngine engine = JSONEngineFactory.createEngine("gson");
		final String jsonString = engine.toJsonString(integers);
		Assertions.assertEquals("[1,2,3]", jsonString);
	}
}
