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

package cn.hutool.v7.core.comparator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import cn.hutool.v7.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PropertyComparatorTest {

	@Test
	public void sortNullTest() {
		final ArrayList<User> users = ListUtil.of(
				new User("1", "d"),
				new User("2", null),
				new User("3", "a")
		);

		// 默认null在末尾
		final List<User> sortedList1 = ListUtil.sort(users, new PropertyComparator<>("b"));
		Assertions.assertEquals("a", sortedList1.get(0).getB());
		Assertions.assertEquals("d", sortedList1.get(1).getB());
		Assertions.assertNull(sortedList1.get(2).getB());

		// null在首
		final List<User> sortedList2 = ListUtil.sort(users, new PropertyComparator<>("b", false));
		Assertions.assertNull(sortedList2.get(0).getB());
		Assertions.assertEquals("a", sortedList2.get(1).getB());
		Assertions.assertEquals("d", sortedList2.get(2).getB());
	}

	@Test
	public void reversedTest() {
		final ArrayList<User> users = ListUtil.of(
				new User("1", "d"),
				new User("2", null),
				new User("3", "a")
		);

		// 反序
		final List<User> sortedList = ListUtil.sort(users, new PropertyComparator<>("b").reversed());
		Assertions.assertNull(sortedList.get(0).getB());
		Assertions.assertEquals("d", sortedList.get(1).getB());
		Assertions.assertEquals("a", sortedList.get(2).getB());
	}

	@Data
	@AllArgsConstructor
	static class User{
		private String a;
		private String b;
	}
	@Test
	void readsCurrentNestedValueOnEveryComparison() {
		final Map<String, Object> left = new HashMap<>();
		final Map<String, Object> right = new HashMap<>();
		left.put("detail", Collections.singletonMap("rank", 1));
		right.put("detail", Collections.singletonMap("rank", 2));
		final PropertyComparator<Map<String, Object>> comparator = new PropertyComparator<>("detail.rank", false, true);
		Assertions.assertTrue(comparator.compare(left, right) < 0);
		left.put("detail", Collections.singletonMap("rank", 3));
		Assertions.assertTrue(comparator.compare(left, right) > 0);
		left.put("detail.rank", 0);
		Assertions.assertTrue(comparator.compare(left, right) < 0);
		Assertions.assertEquals(0, new PropertyComparator<>(" ", false, true).compare(left, right));
	}

}
