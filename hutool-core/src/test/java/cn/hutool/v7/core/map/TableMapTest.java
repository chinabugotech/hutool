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

package cn.hutool.v7.core.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableMapTest {

	@Test
	public void getTest(){
		final TableMap<String, Integer> tableMap = new TableMap<>(16);
		tableMap.put("aaa", 111);
		tableMap.put("bbb", 222);

		Assertions.assertEquals(Integer.valueOf(111), tableMap.get("aaa"));
		Assertions.assertEquals(Integer.valueOf(222), tableMap.get("bbb"));

		Assertions.assertEquals("aaa", tableMap.getKey(111));
		Assertions.assertEquals("bbb", tableMap.getKey(222));
	}

	@SuppressWarnings("OverwrittenKey")
	@Test
	public void removeTest() {
		final TableMap<String, Integer> tableMap = new TableMap<>(16);
		tableMap.put("a", 111);
		tableMap.put("a", 222);
		tableMap.put("a", 222);

		tableMap.remove("a");

		Assertions.assertEquals(0, tableMap.size());
	}

	@SuppressWarnings("OverwrittenKey")
	@Test
	public void removeTest2() {
		final TableMap<String, Integer> tableMap = new TableMap<>(16);
		tableMap.put("a", 111);
		tableMap.put("a", 222);
		tableMap.put("a", 222);

		tableMap.remove("a", 222);

		Assertions.assertEquals(1, tableMap.size());
	}
}
