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

package cn.hutool.core.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIJV845Test {
	@Test
	void test() {
		final CamelCaseLinkedMap<String, String> map = new CamelCaseLinkedMap<>();
		map.put("reading_date","1");
		map.put("class_attribute","2");
		map.put("service_location","3");
		map.put("renovation_start","4");
		map.put("renovation_end","5");
		map.put("student_no","6");
		map.put("building_name","7");

		assertEquals("{readingDate=1, classAttribute=2, serviceLocation=3, renovationStart=4, renovationEnd=5, studentNo=6, buildingName=7}", map.toString());
	}
}
