/*
 * Copyright (c) 2026 Hutool Team.
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

import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Issue4269Test {
	record TestRecord(String name, int age) {}

	@Test
	void toRecordTest(){
		final JSONObject jsonObject = JSONUtil.parseObj("{name:'张三',age:18}");
		final TestRecord testRecord = jsonObject.toBean(TestRecord.class);
		assertEquals("张三", testRecord.name());
		assertEquals(18, testRecord.age());

		// 当name这个字段为null时，转为json会去除null字段，可能导致反序列化为Record时找不到构造，此处修正
		jsonObject.remove("name");

		final TestRecord testRecord2 = jsonObject.toBean(TestRecord.class);
		assertNull(testRecord2.name());
		assertEquals(18, testRecord2.age());
	}
}
