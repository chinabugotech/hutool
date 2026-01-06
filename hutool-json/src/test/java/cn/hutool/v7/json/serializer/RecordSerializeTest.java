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

package cn.hutool.v7.json.serializer;

import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecordSerializeTest {

	public record TestRecord(String name, String address){ }

	@Test
	void recordToJsonTest(){
		final JSON parse = JSONUtil.parse(new TestRecord("Tom", "Beijing"));
		Assertions.assertEquals(JSONObject.class, parse.getClass());
		Assertions.assertEquals("{\"name\":\"Tom\",\"address\":\"Beijing\"}", parse.toString());

		final TestRecord bean = parse.toBean(TestRecord.class);
		Assertions.assertEquals("Tom", bean.name());
		Assertions.assertEquals("Beijing", bean.address());
	}
}
