/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.jmh.json;

import lombok.Data;

import java.util.Date;
import java.util.List;

public class JSONJmhData {
	public static String jsonStr = "{\n" +
		"  \"name\": \"张三\",\n" +
		"  \"age\": 18,\n" +
		"  \"birthday\": \"2020-01-01\",\n" +
		"  \"booleanValue\": true,\n" +
		"  \"jsonObjectSub\": {\n" +
		"    \"subStr\": \"abc\",\n" +
		"    \"subNumber\": 150343445454,\n" +
		"    \"subBoolean\": true\n" +
		"  },\n" +
		"  \"jsonArraySub\": [\n" +
		"    \"abc\",\n" +
		"    123,\n" +
		"    false\n" +
		"  ]\n" +
		"}";

	@Data
	public static class TestBean{
		private String name;
		private int age;
		private boolean gender;
		private Date createDate;
		private Object nullObj;
		private SubBean jsonObjectSub;
		private List<Object> jsonArraySub;
	}

	@Data
	public static class SubBean{
		private String subStr;
		private Long subNumber;
		private boolean subBoolean;
	}
}
