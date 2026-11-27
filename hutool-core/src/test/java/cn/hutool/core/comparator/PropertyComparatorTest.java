package cn.hutool.core.comparator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PropertyComparatorTest {
	@Test
	public void sortNullTest() {
		final ArrayList<User> users = ListUtil.toList(
				new User("1", "d"),
				new User("2", null),
				new User("3", "a")
		);

		// 默认null在末尾
		final List<User> sortedList1 = ListUtil.sort(users, new PropertyComparator<>("b"));
		assertEquals("a", sortedList1.get(0).getB());
		assertEquals("d", sortedList1.get(1).getB());
		assertNull(sortedList1.get(2).getB());

		// null在首
		final List<User> sortedList2 = ListUtil.sort(users, new PropertyComparator<>("b", false));
		assertNull(sortedList2.get(0).getB());
		assertEquals("a", sortedList2.get(1).getB());
		assertEquals("d", sortedList2.get(2).getB());
	}

	@Test
	public void reversedTest() {
		final ArrayList<User> users = ListUtil.toList(
				new User("1", "d"),
				new User("2", null),
				new User("3", "a")
		);

		// 反序
		final List<User> sortedList = ListUtil.sort(users, new PropertyComparator<>("b").reversed());
		assertNull(sortedList.get(0).getB());
		assertEquals("d", sortedList.get(1).getB());
		assertEquals("a", sortedList.get(2).getB());
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
		assertTrue(comparator.compare(left, right) < 0);
		left.put("detail", Collections.singletonMap("rank", 3));
		assertTrue(comparator.compare(left, right) > 0);
		assertEquals(0, new PropertyComparator<>(" ", false, true).compare(left, right));
	}

}
