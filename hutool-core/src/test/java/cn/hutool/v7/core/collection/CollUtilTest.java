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

package cn.hutool.v7.core.collection;

import cn.hutool.v7.core.collection.iter.IterUtil;
import cn.hutool.v7.core.collection.set.SetUtil;
import cn.hutool.v7.core.comparator.CompareUtil;
import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.map.Dict;
import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.core.text.StrUtil;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 集合工具类单元测试
 *
 * @author Looly
 */
public class CollUtilTest {

	@SuppressWarnings("ConstantConditions")
	@Test
	public void emptyIfNullTest() {
		final Set<?> set = null;
		final Set<?> set1 = CollUtil.emptyIfNull(set);
		assertEquals(SetUtil.empty(), set1);

		final List<?> list = null;
		final List<?> list1 = CollUtil.emptyIfNull(list);
		assertEquals(ListUtil.empty(), list1);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void hasNullTest() {
		List<Object> list = null;
		assertTrue(CollUtil.hasNull(list));

		list = ListUtil.of();
		assertFalse(CollUtil.hasNull(list));

		list = ListUtil.of("");
		assertFalse(CollUtil.hasNull(list));

		list = ListUtil.of("", null);
		assertTrue(CollUtil.hasNull(list));
	}

	@Test
	public void defaultIfEmpty() {
		List<String> strings = CollUtil.defaultIfEmpty(ListUtil.of(), ListUtil.of("1"));
		assertEquals(ListUtil.of("1"), strings);

		strings = CollUtil.defaultIfEmpty(null, ListUtil.of("1"));
		assertEquals(ListUtil.of("1"), strings);
	}

	@Test
	public void defaultIfEmpty2() {
		final List<String> strings = CollUtil.defaultIfEmpty(ListUtil.of(), Function.identity(), () -> ListUtil.of("1"));
		assertEquals(ListUtil.of("1"), strings);
	}

	@Test
	public void testPredicateContains() {
		final List<String> list = ListUtil.of("bbbbb", "aaaaa", "ccccc");
		assertTrue(CollUtil.contains(list, s -> s.startsWith("a")));
		assertFalse(CollUtil.contains(list, s -> s.startsWith("d")));
	}

	@Test
	public void testRemoveWithAddIf() {
		List<Integer> list = ListUtil.of(1, 2, 3);
		final List<Integer> exceptRemovedList = ListUtil.of(2, 3);
		final List<Integer> exceptResultList = ListUtil.of(1);

		List<Integer> resultList = CollUtil.removeWithAddIf(list, ele -> 1 == ele);
		assertEquals(list, exceptRemovedList);
		assertEquals(resultList, exceptResultList);

		list = ListUtil.of(1, 2, 3);
		resultList = new ArrayList<>();
		CollUtil.removeWithAddIf(list, resultList, ele -> 1 == ele);
		assertEquals(list, exceptRemovedList);
		assertEquals(resultList, exceptResultList);
	}

	@Test
	public void testPadLeft() {
		List<String> srcList = ListUtil.of();
		List<String> answerList = ListUtil.of("a", "b");
		CollUtil.padLeft(srcList, 1, "b");
		CollUtil.padLeft(srcList, 2, "a");
		assertEquals(srcList, answerList);

		srcList = ListUtil.of("a", "b");
		answerList = ListUtil.of("a", "b");
		CollUtil.padLeft(srcList, 2, "a");
		assertEquals(srcList, answerList);

		srcList = ListUtil.of("c");
		answerList = ListUtil.of("a", "a", "c");
		CollUtil.padLeft(srcList, 3, "a");
		assertEquals(srcList, answerList);
	}

	@Test
	public void testPadRight() {
		final List<String> srcList = ListUtil.of("a");
		final List<String> answerList = ListUtil.of("a", "b", "b", "b", "b");
		CollUtil.padRight(srcList, 5, "b");
		assertEquals(srcList, answerList);
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void isNotEmptyTest() {
		assertFalse(CollUtil.isNotEmpty((Collection<?>) null));
	}

	@Test
	public void newHashSetTest() {
		final Set<String> set = SetUtil.of((String[]) null);
		Assertions.assertNotNull(set);
	}

	@Test
	public void unionTest() {
		final List<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d");

		final Collection<String> union = CollUtil.union(list1, list2);

		assertEquals(3, CollUtil.count(union, "b"::equals));
	}

	@Test
	public void intersectionTest() {
		final List<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d");

		final Collection<String> intersection = CollUtil.intersection(list1, list2);
		assertEquals(2, CollUtil.count(intersection, "b"::equals));
		assertEquals(0, CollUtil.count(intersection, "x"::equals));
	}

	@Test
	public void intersectionDistinctTest() {
		final List<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d");
		final List<String> list3 = ListUtil.of();

		final Collection<String> intersectionDistinct = CollUtil.intersectionDistinct(list1, list2);
		assertEquals(SetUtil.ofLinked("a", "b", "c", "d"), intersectionDistinct);

		final Collection<String> intersectionDistinct2 = CollUtil.intersectionDistinct(list1, list2, list3);
		Console.log(intersectionDistinct2);
		assertTrue(intersectionDistinct2.isEmpty());
	}

	@Test
	public void disjunctionTest() {
		final List<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d", "x2");

		final Collection<String> disjunction = CollUtil.disjunction(list1, list2);
		assertTrue(disjunction.contains("b"));
		assertTrue(disjunction.contains("x2"));
		assertTrue(disjunction.contains("x"));

		final Collection<String> disjunction2 = CollUtil.disjunction(list2, list1);
		assertTrue(disjunction2.contains("b"));
		assertTrue(disjunction2.contains("x2"));
		assertTrue(disjunction2.contains("x"));
	}

	@Test
	public void disjunctionTest2() {
		// 任意一个集合为空，差集为另一个集合
		final List<String> list1 = ListUtil.of();
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d", "x2");

		final Collection<String> disjunction = CollUtil.disjunction(list1, list2);
		assertEquals(list2, disjunction);
		final Collection<String> disjunction2 = CollUtil.disjunction(list2, list1);
		assertEquals(list2, disjunction2);
	}

	@Test
	public void disjunctionTest3() {
		// 无交集下返回共同的元素
		final List<String> list1 = ListUtil.of("1", "2", "3");
		final List<String> list2 = ListUtil.of("a", "b", "c");

		final Collection<String> disjunction = CollUtil.disjunction(list1, list2);
		assertTrue(disjunction.contains("1"));
		assertTrue(disjunction.contains("2"));
		assertTrue(disjunction.contains("3"));
		assertTrue(disjunction.contains("a"));
		assertTrue(disjunction.contains("b"));
		assertTrue(disjunction.contains("c"));
		final Collection<String> disjunction2 = CollUtil.disjunction(list2, list1);
		assertTrue(disjunction2.contains("1"));
		assertTrue(disjunction2.contains("2"));
		assertTrue(disjunction2.contains("3"));
		assertTrue(disjunction2.contains("a"));
		assertTrue(disjunction2.contains("b"));
		assertTrue(disjunction2.contains("c"));
	}

	@Test
	public void subtractTest() {
		final List<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d", "x2");
		final Collection<String> subtract = CollUtil.subtract(list1, list2);
		assertEquals(1, subtract.size());
		assertEquals("x", subtract.iterator().next());
	}

	@Test
	public void subtractSetTest() {
		final HashMap<String, Object> map1 = MapUtil.newHashMap();
		final HashMap<String, Object> map2 = MapUtil.newHashMap();
		map1.put("1", "v1");
		map1.put("2", "v2");
		map2.put("2", "v2");
		final Collection<String> r2 = CollUtil.subtract(map1.keySet(), map2.keySet());
		assertEquals("[1]", r2.toString());
	}

	@Test
	public void subtractSetToListTest() {
		final HashMap<String, Object> map1 = MapUtil.newHashMap();
		final HashMap<String, Object> map2 = MapUtil.newHashMap();
		map1.put("1", "v1");
		map1.put("2", "v2");
		map2.put("2", "v2");
		final List<String> r2 = CollUtil.subtractToList(map1.keySet(), map2.keySet());
		assertEquals("[1]", r2.toString());
	}

	@Test
	public void toMapListAndToListMapTest() {
		final HashMap<String, String> map1 = new HashMap<>();
		map1.put("a", "值1");
		map1.put("b", "值1");

		final HashMap<String, String> map2 = new HashMap<>();
		map2.put("a", "值2");
		map2.put("c", "值3");

		// ----------------------------------------------------------------------------------------
		final List<HashMap<String, String>> list = ListUtil.of(map1, map2);
		final Map<String, List<String>> map = CollUtil.toListMap(list);
		assertEquals("值1", map.get("a").get(0));
		assertEquals("值2", map.get("a").get(1));

		// ----------------------------------------------------------------------------------------
		final List<Map<String, String>> listMap = CollUtil.toMapList(map);
		assertEquals("值1", listMap.get(0).get("a"));
		assertEquals("值2", listMap.get(1).get("a"));
	}

	@Test
	public void getFieldValuesTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final Dict v2 = Dict.of().set("age", 13).set("id", 15).set("name", "李四");
		final List<Dict> list = ListUtil.of(v1, v2);

		final List<Object> fieldValues = (List<Object>) CollUtil.getFieldValues(list, "name");

		assertEquals("张三", fieldValues.get(0));
		assertEquals("李四", fieldValues.get(1));
	}

	@Test
	public void partitionTest() {
		final List<Integer> list = ListUtil.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
		final List<List<Integer>> split = CollUtil.partition(list, 3);
		assertEquals(3, split.size());
		assertEquals(3, split.get(0).size());
	}

	@Test
	public void partitionTest2() {
		final ArrayList<Integer> list = ListUtil.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
		final List<List<Integer>> split = CollUtil.partition(list, Integer.MAX_VALUE);
		assertEquals(1, split.size());
		assertEquals(9, split.get(0).size());
	}

	@Test
	public void foreachTest() {
		final HashMap<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");

		final String[] result = new String[1];
		final String a = "a";
		CollUtil.forEach(map, (index, key, value) -> {
			if (a.equals(key)) {
				result[0] = value;
			}
		});
		assertEquals("1", result[0]);
	}

	@Test
	public void editTest() {
		final List<String> list = ListUtil.of("a", "b", "c");

		final Collection<String> filtered = CollUtil.edit(list, t -> t + 1);

		assertEquals(ListUtil.of("a1", "b1", "c1"), filtered);
	}

	@Test
	public void removeTest() {
		final List<String> list = ListUtil.of("a", "b", "c");

		final List<String> filtered = CollUtil.remove(list, "a"::equals);

		// 原地过滤
		Assertions.assertSame(list, filtered);
		assertEquals(ListUtil.of("b", "c"), filtered);
	}

	@Test
	public void removeForSetTest() {
		final Set<String> set = SetUtil.ofLinked("a", "b", "", "  ", "c");
		final Set<String> filtered = CollUtil.remove(set, StrUtil::isBlank);

		assertEquals(SetUtil.ofLinked("a", "b", "c"), filtered);
	}

	@Test
	public void filterRemoveTest() {
		final List<String> list = ListUtil.of("a", "b", "c");

		final List<String> removed = new ArrayList<>();
		final List<String> filtered = CollUtil.remove(list, t -> {
			if ("a".equals(t)) {
				removed.add(t);
				return true;
			}
			return false;
		});

		assertEquals(1, removed.size());
		assertEquals("a", removed.get(0));

		// 原地过滤
		Assertions.assertSame(list, filtered);
		assertEquals(ListUtil.of("b", "c"), filtered);
	}

	@Test
	public void removeNullTest() {
		final List<String> list = ListUtil.of("a", "b", "c", null, "", "  ");

		final List<String> filtered = CollUtil.removeNull(list);

		// 原地过滤
		Assertions.assertSame(list, filtered);
		assertEquals(ListUtil.of("a", "b", "c", "", "  "), filtered);
	}

	@Test
	public void removeEmptyTest() {
		final List<String> list = ListUtil.of("a", "b", "c", null, "", "  ");

		final List<String> filtered = CollUtil.removeEmpty(list);

		// 原地过滤
		Assertions.assertSame(list, filtered);
		assertEquals(ListUtil.of("a", "b", "c", "  "), filtered);
	}

	@Test
	public void removeBlankTest() {
		final List<String> list = ListUtil.of("a", "b", "c", null, "", "  ");

		final List<String> filtered = CollUtil.removeBlank(list);

		// 原地过滤
		Assertions.assertSame(list, filtered);
		assertEquals(ListUtil.of("a", "b", "c"), filtered);
	}

	@Test
	public void groupTest() {
		final List<String> list = ListUtil.of("1", "2", "3", "4", "5", "6");
		final List<List<String>> group = CollUtil.group(list, null);
		assertFalse(group.isEmpty());

		final List<List<String>> group2 = CollUtil.group(list, t -> {
			// 按照奇数偶数分类
			return Integer.parseInt(t) % 2;
		});
		assertEquals(ListUtil.of("2", "4", "6"), group2.get(0));
		assertEquals(ListUtil.of("1", "3", "5"), group2.get(1));
	}

	@Test
	public void groupByFieldTest() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12), new TestBean("李四", 13), new TestBean("王五", 12));
		final List<List<TestBean>> groupByField = CollUtil.groupByField(list, "age");
		assertEquals("张三", groupByField.get(0).get(0).getName());
		assertEquals("王五", groupByField.get(0).get(1).getName());

		assertEquals("李四", groupByField.get(1).get(0).getName());
	}

	@Test
	public void groupByFuncTest() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12), new TestBean("李四", 13), new TestBean("王五", 12));
		final List<List<TestBean>> groupByField = CollUtil.groupByFunc(list, TestBean::getAge);
		assertEquals("张三", groupByField.get(0).get(0).getName());
		assertEquals("王五", groupByField.get(0).get(1).getName());

		assertEquals("李四", groupByField.get(1).get(0).getName());
	}

	@Test
	public void groupByFunc2Test() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12), new TestBean("李四", 13), new TestBean("王五", 12));
		final List<List<TestBean>> groupByField = CollUtil.groupByFunc(list, a -> a.getAge() > 12);
		assertEquals("张三", groupByField.get(0).get(0).getName());
		assertEquals("王五", groupByField.get(0).get(1).getName());

		assertEquals("李四", groupByField.get(1).get(0).getName());
	}

	@Test
	public void sortByPropertyTest() {
		final List<TestBean> list = ListUtil.of(
			new TestBean("张三", 12, DateUtil.parse("2018-05-01")), //
			new TestBean("李四", 13, DateUtil.parse("2018-03-01")), //
			new TestBean("王五", 12, DateUtil.parse("2018-04-01"))//
		);

		CollUtil.sortByProperty(list, "createTime");
		assertEquals("李四", list.get(0).getName());
		assertEquals("王五", list.get(1).getName());
		assertEquals("张三", list.get(2).getName());
	}

	@Test
	public void sortByPropertyTest2() {
		final List<TestBean> list = ListUtil.of(
			new TestBean("张三", 0, DateUtil.parse("2018-05-01")), //
			new TestBean("李四", -12, DateUtil.parse("2018-03-01")), //
			new TestBean("王五", 23, DateUtil.parse("2018-04-01"))//
		);

		CollUtil.sortByProperty(list, "age");
		assertEquals("李四", list.get(0).getName());
		assertEquals("张三", list.get(1).getName());
		assertEquals("王五", list.get(2).getName());
	}

	@Test
	public void fieldValueMapTest() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12, DateUtil.parse("2018-05-01")), //
			new TestBean("李四", 13, DateUtil.parse("2018-03-01")), //
			new TestBean("王五", 12, DateUtil.parse("2018-04-01"))//
		);

		final Map<String, TestBean> map = CollUtil.fieldValueMap(list, "name");
		assertEquals("李四", map.get("李四").getName());
		assertEquals("王五", map.get("王五").getName());
		assertEquals("张三", map.get("张三").getName());
	}

	@Test
	public void fieldValueAsMapTest() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12, DateUtil.parse("2018-05-01")), //
			new TestBean("李四", 13, DateUtil.parse("2018-03-01")), //
			new TestBean("王五", 14, DateUtil.parse("2018-04-01"))//
		);

		final Map<String, Integer> map = CollUtil.fieldValueAsMap(list, "name", "age");
		assertEquals(Integer.valueOf(12), map.get("张三"));
		assertEquals(Integer.valueOf(13), map.get("李四"));
		assertEquals(Integer.valueOf(14), map.get("王五"));
	}

	@Test
	public void emptyTest() {
		final SortedSet<String> emptySortedSet = CollUtil.empty(SortedSet.class);
		assertEquals(Collections.emptySortedSet(), emptySortedSet);

		final Set<String> emptySet = CollUtil.empty(Set.class);
		assertEquals(Collections.emptySet(), emptySet);

		final List<String> emptyList = CollUtil.empty(List.class);
		assertEquals(Collections.emptyList(), emptyList);
	}

	@Data
	@AllArgsConstructor
	public static class TestBean {
		private String name;
		private int age;
		private Date createTime;

		public TestBean(final String name, final int age) {
			this.name = name;
			this.age = age;
		}
	}

	@Test
	public void listTest() {
		final List<Object> list1 = ListUtil.of(false);
		final List<Object> list2 = ListUtil.of(true);

		Assertions.assertInstanceOf(ArrayList.class, list1);
		Assertions.assertInstanceOf(LinkedList.class, list2);
	}

	@Test
	public void listTest2() {
		final List<String> list1 = ListUtil.of("a", "b", "c");
		final List<String> list2 = ListUtil.ofLinked("a", "b", "c");
		assertEquals("[a, b, c]", list1.toString());
		assertEquals("[a, b, c]", list2.toString());
	}

	@Test
	public void listTest3() {
		final HashSet<String> set = new LinkedHashSet<>();
		set.add("a");
		set.add("b");
		set.add("c");

		final List<String> list1 = ListUtil.of(false, set);
		final List<String> list2 = ListUtil.of(true, set);
		assertEquals("[a, b, c]", list1.toString());
		assertEquals("[a, b, c]", list2.toString());
	}

	@Test
	public void getTest() {
		final HashSet<String> set = SetUtil.ofLinked("A", "B", "C", "D");
		String str = CollUtil.get(set, 2);
		assertEquals("C", str);

		str = CollUtil.get(set, -1);
		assertEquals("D", str);
	}

	@Test
	public void subInput1PositiveNegativePositiveOutput1() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 3;
		final int end = -1;
		final int step = 2;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		arrayList.add(null);
		assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1ZeroPositivePositiveOutput1() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 0;
		final int end = 1;
		final int step = 2;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);

		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		arrayList.add(null);
		assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1PositiveZeroOutput0() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 1;
		final int end = 0;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end);

		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		assertEquals(arrayList, retval);
	}

	@Test
	public void subInput0ZeroZeroZeroOutputNull() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		final int start = 0;
		final int end = 0;
		final int step = 0;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		assertTrue(retval.isEmpty());
	}

	@Test
	public void subInput1PositiveNegativeZeroOutput0() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 1;
		final int end = -2_147_483_648;
		final int step = 0;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1PositivePositivePositiveOutput0() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 2_147_483_647;
		final int end = 2_147_483_647;
		final int step = 1_073_741_824;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1PositiveNegativePositiveOutputArrayIndexOutOfBoundsException() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			// Arrange
			final List<Integer> list = new ArrayList<>();
			list.add(null);
			final int start = 2_147_483_643;
			final int end = -2_147_483_648;
			final int step = 2;

			// Act
			CollUtil.sub(list, start, end, step);
			// Method is not expected to return due to exception thrown
		});
	}

	@Test
	public void subInput0ZeroPositiveNegativeOutputNull() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		final int start = 0;
		final int end = 1;
		final int step = -2_147_483_646;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		assertTrue(retval.isEmpty());
	}

	@Test
	public void subInput1PositivePositivePositiveOutput02() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 2_147_483_643;
		final int end = 2_147_483_642;
		final int step = 1_073_741_824;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1ZeroZeroPositiveOutput0() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(0);
		final int start = 0;
		final int end = 0;
		final int step = 2;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1NegativeZeroPositiveOutput0() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(0);
		final int start = -1;
		final int end = 0;
		final int step = 2;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		assertEquals(arrayList, retval);
	}

	@Test
	public void subInput0ZeroZeroOutputNull() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		final int start = 0;
		final int end = 0;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end);
		// Assert result
		assertTrue(retval.isEmpty());
	}

	@Test
	public void sortPageAllTest() {
		final List<Integer> list = ListUtil.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
		final List<Integer> sortPageAll = CollUtil.sortPageAll(1, 5, Comparator.reverseOrder(), list);

		assertEquals(ListUtil.of(4, 3, 2, 1), sortPageAll);
	}

	@Test
	public void containsAnyTest() {
		final List<Integer> list1 = ListUtil.of(1, 2, 3, 4, 5);
		final List<Integer> list2 = ListUtil.of(5, 3, 1, 9, 11);

		assertTrue(CollUtil.containsAny(list1, list2));
	}

	@Test
	public void containsAllTest() {
		final List<Integer> list1 = ListUtil.of(1, 2, 3, 4, 5);
		final List<Integer> list2 = ListUtil.of(5, 3, 1);
		assertTrue(CollUtil.containsAll(list1, list2));

		final List<Integer> list3 = ListUtil.of(1);
		final List<Integer> list4 = ListUtil.of();
		assertTrue(CollUtil.containsAll(list3, list4));
	}

	@Test
	public void getLastTest() {
		// 测试：空数组返回null而不是报错
		final List<String> test = ListUtil.of();
		final String last = CollUtil.getLast(test);
		Assertions.assertNull(last);
	}

	@Test
	public void zipTest() {
		final Collection<String> keys = ListUtil.of("a", "b", "c", "d");
		final Collection<Integer> values = ListUtil.of(1, 2, 3, 4);

		final Map<String, Integer> map = CollUtil.zip(keys, values);

		assertEquals(4, Objects.requireNonNull(map).size());

		assertEquals(1, map.get("a").intValue());
		assertEquals(2, map.get("b").intValue());
		assertEquals(3, map.get("c").intValue());
		assertEquals(4, map.get("d").intValue());
	}

	@Test
	public void toMapTest() {
		final Collection<String> keys = ListUtil.of("a", "b", "c", "d");
		final Map<String, String> map = IterUtil.toMap(keys, (value) -> "key" + value);
		assertEquals("a", map.get("keya"));
		assertEquals("b", map.get("keyb"));
		assertEquals("c", map.get("keyc"));
		assertEquals("d", map.get("keyd"));
	}

	@Test
	public void mapToMapTest() {
		final HashMap<String, String> oldMap = new HashMap<>();
		oldMap.put("a", "1");
		oldMap.put("b", "12");
		oldMap.put("c", "134");

		final Map<String, Long> map = IterUtil.toMap(oldMap.entrySet(),
			Map.Entry::getKey,
			entry -> Long.parseLong(entry.getValue()));

		assertEquals(1L, (long) map.get("a"));
		assertEquals(12L, (long) map.get("b"));
		assertEquals(134L, (long) map.get("c"));
	}

	@Test
	public void countMapTest() {
		final List<String> list = ListUtil.of("a", "b", "c", "c", "a", "b", "d");
		final Map<String, Integer> countMap = CollUtil.countMap(list);

		assertEquals(Integer.valueOf(2), countMap.get("a"));
		assertEquals(Integer.valueOf(2), countMap.get("b"));
		assertEquals(Integer.valueOf(2), countMap.get("c"));
		assertEquals(Integer.valueOf(1), countMap.get("d"));
	}

	@Test
	public void indexOfTest() {
		final List<String> list = ListUtil.of("a", "b", "c", "c", "a", "b", "d");
		final int i = CollUtil.indexOf(list, (str) -> str.charAt(0) == 'c');
		assertEquals(2, i);
	}

	@Test
	public void lastIndexOfTest() {
		// List有优化
		final List<String> list = ListUtil.of("a", "b", "c", "c", "a", "b", "d");
		final int i = CollUtil.lastIndexOf(list, (str) -> str.charAt(0) == 'a');
		assertEquals(4, i);

		final Queue<Integer> set = new ArrayDeque<>(Arrays.asList(1, 2, 3, 3, 2, 1));
		assertEquals(5, CollUtil.lastIndexOf(set, num -> num.equals(1)));
		assertEquals(4, CollUtil.lastIndexOf(set, num -> num.equals(2)));
		assertEquals(3, CollUtil.lastIndexOf(set, num -> num.equals(3)));
		assertEquals(-1, CollUtil.lastIndexOf(set, num -> num.equals(4)));
	}

	@Test
	public void lastIndexOfSetTest() {
		final Set<String> list = SetUtil.ofLinked("a", "b", "c", "c", "a", "b", "d");
		// 去重后c排第三
		final int i = CollUtil.lastIndexOf(list, (str) -> str.charAt(0) == 'c');
		assertEquals(2, i);
	}

	@Test
	public void pageTest() {
		final List<Dict> objects = ListUtil.of();
		for (int i = 0; i < 10; i++) {
			objects.add(Dict.of().set("name", "姓名：" + i));
		}

		assertEquals(0, ListUtil.page(objects, 3, 5).size());
	}

	@Test
	public void subtractToListTest() {
		final List<Long> list1 = Arrays.asList(1L, 2L, 3L);
		final List<Long> list2 = Arrays.asList(2L, 3L);

		final List<Long> result = CollUtil.subtractToList(list1, list2);
		assertEquals(1, result.size());
		assertEquals(1L, (long) result.get(0));
	}

	@Test
	public void sortNaturalTest() {
		final List<String> of = ListUtil.of("a", "c", "b");
		final List<String> sort = CollUtil.sort(of, CompareUtil.natural());
		assertEquals("a,b,c", CollUtil.join(sort, ","));
	}

	@Test
	public void setValueByMapTest() {
		// https://gitee.com/chinabugotech/hutool/pulls/482
		final List<Person> people = Arrays.asList(
			new Person("aa", 12, "man", 1),
			new Person("bb", 13, "woman", 2),
			new Person("cc", 14, "man", 3),
			new Person("dd", 15, "woman", 4),
			new Person("ee", 16, "woman", 5),
			new Person("ff", 17, "man", 6)
		);

		final Map<Integer, String> genderMap = new HashMap<>();
		genderMap.put(1, null);
		genderMap.put(2, "妇女");
		genderMap.put(3, "少女");
		genderMap.put(4, "女");
		genderMap.put(5, "小孩");
		genderMap.put(6, "男");

		assertEquals("woman", people.get(1).getGender());
		CollUtil.setValueByMap(people, genderMap, Person::getId, Person::setGender);
		assertEquals("妇女", people.get(1).getGender());

		final Map<Integer, Person> personMap = new HashMap<>();
		personMap.put(1, new Person("AA", 21, "男", 1));
		personMap.put(2, new Person("BB", 7, "小孩", 2));
		personMap.put(3, new Person("CC", 65, "老人", 3));
		personMap.put(4, new Person("DD", 35, "女人", 4));
		personMap.put(5, new Person("EE", 14, "少女", 5));
		personMap.put(6, null);

		CollUtil.setValueByMap(people, personMap, Person::getId, (x, y) -> {
			x.setGender(y.getGender());
			x.setName(y.getName());
			x.setAge(y.getAge());
		});

		assertEquals("小孩", people.get(1).getGender());
	}

	@Test
	public void distinctTest() {
		final List<Integer> distinct = CollUtil.distinct(ListUtil.view(5, 3, 10, 9, 0, 5, 10, 9));
		assertEquals(ListUtil.view(5, 3, 10, 9, 0), distinct);
	}

	@Test
	public void distinctByFunctionTest() {
		final List<Person> people = Arrays.asList(
			new Person("aa", 12, "man", 1),
			new Person("bb", 13, "woman", 2),
			new Person("cc", 14, "man", 3),
			new Person("dd", 15, "woman", 4),
			new Person("ee", 16, "woman", 5),
			new Person("ff", 17, "man", 6)
		);

		// 覆盖模式下ff覆盖了aa，ee覆盖了bb
		List<Person> distinct = CollUtil.distinct(people, Person::getGender, true);
		assertEquals(2, distinct.size());
		assertEquals("ff", distinct.get(0).getName());
		assertEquals("ee", distinct.get(1).getName());

		// 非覆盖模式下，保留了最早加入的aa和bb
		distinct = CollUtil.distinct(people, Person::getGender, false);
		assertEquals(2, distinct.size());
		assertEquals("aa", distinct.get(0).getName());
		assertEquals("bb", distinct.get(1).getName());
	}

	@Data
	@AllArgsConstructor
	static class Person {
		private String name;
		private Integer age;
		private String gender;
		private Integer id;
	}

	@Test
	public void mapTest() {
		final List<String> list = ListUtil.of("a", "b", "c");
		final List<Object> extract = CollUtil.map(list, (e) -> e + "_1");
		assertEquals(ListUtil.of("a_1", "b_1", "c_1"), extract);
	}

	@Test
	public void mapBeanTest() {
		final List<Person> people = Arrays.asList(
			new Person("aa", 12, "man", 1),
			new Person("bb", 13, "woman", 2),
			new Person("cc", 14, "man", 3),
			new Person("dd", 15, "woman", 4)
		);

		final List<Object> extract = CollUtil.map(people, Person::getName);
		assertEquals(ListUtil.of("aa", "bb", "cc", "dd"), extract);
	}

	@Test
	public void zipTest2() {
		// 1. 正常情况测试
		final List<String> list1 = ListUtil.of("a", "b", "c");
		final List<Integer> list2 = ListUtil.of(1, 2, 3);
		final List<String> result = CollUtil.zip(list1, list2, (s, i) -> s + i);
		assertEquals(ListUtil.of("a1", "b2", "c3"), result);

		// 2. 空集合测试
		final List<String> emptyList = ListUtil.of();
		final List<String> emptyResult = CollUtil.zip(emptyList, list2, (s, i) -> s + i);
		assertTrue(emptyResult.isEmpty());

		// 3. 不同大小集合测试(以较小集合为准)
		final List<Integer> longerList = ListUtil.of(1, 2, 3, 4, 5);
		final List<String> sizedResult = CollUtil.zip(list1, longerList, (s, i) -> s + i);
		assertEquals(3, sizedResult.size());
		assertEquals("a1", sizedResult.get(0));

		// 4. 自定义zipper函数测试
		final List<Double> list3 = ListUtil.of(1.1, 2.2, 3.3);
		final List<String> customResult = CollUtil.zip(list2, list3, (i, d) -> String.format("%d-%.1f", i, d));
		assertEquals(ListUtil.of("1-1.1", "2-2.2", "3-3.3"), customResult);
	}

	@Test
	public void createTest() {
		final Collection<Object> collection = CollUtil.create(Collections.emptyList().getClass());
		Console.log(collection.getClass());
		Assertions.assertNotNull(collection);
	}

	@Test
	public void transTest() {
		final List<Person> people = Arrays.asList(
			new Person("aa", 12, "man", 1),
			new Person("bb", 13, "woman", 2),
			new Person("cc", 14, "man", 3),
			new Person("dd", 15, "woman", 4)
		);

		final Collection<String> trans = CollUtil.trans(people, Person::getName);
		assertEquals("[aa, bb, cc, dd]", trans.toString());
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void unionNullTest() {
		final List<String> list1 = new ArrayList<>();
		final List<String> list2 = null;
		final List<String> list3 = null;
		final Collection<String> union = CollUtil.union(list1, list2, list3);
		Assertions.assertNotNull(union);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void unionDistinctNullTest() {
		final List<String> list1 = new ArrayList<>();
		final List<String> list2 = null;
		final List<String> list3 = null;
		final Set<String> set = CollUtil.unionDistinct(list1, list2, list3);
		Assertions.assertNotNull(set);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void unionAllNullTest() {
		final List<String> list1 = new ArrayList<>();
		final List<String> list2 = null;
		final List<String> list3 = null;
		final List<String> list = CollUtil.unionAll(list1, list2, list3);
		Assertions.assertNotNull(list);

		assertEquals(
			ListUtil.of(1, 2, 3, 4),
			CollUtil.unionAll(ListUtil.of(1), ListUtil.of(2), ListUtil.of(3), ListUtil.of(4))
		);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void intersectionNullTest() {
		final List<String> list1 = new ArrayList<>();
		list1.add("aa");
		final List<String> list2 = new ArrayList<>();
		list2.add("aa");
		final List<String> list3 = null;
		final Collection<String> collection = CollUtil.intersection(list1, list2, list3);
		Assertions.assertNotNull(collection);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void intersectionDistinctNullTest() {
		final List<String> list1 = new ArrayList<>();
		list1.add("aa");
		final List<String> list2 = null;
		// list2.add("aa");
		final List<String> list3 = null;
		final Collection<String> collection = CollUtil.intersectionDistinct(list1, list2, list3);
		Assertions.assertNotNull(collection);
	}

	@Test
	public void addIfAbsentTest() {
		// 为false的情况
		assertFalse(CollUtil.addIfAbsent(null, null));
		assertFalse(CollUtil.addIfAbsent(ListUtil.of(), null));
		assertFalse(CollUtil.addIfAbsent(null, "123"));
		assertFalse(CollUtil.addIfAbsent(ListUtil.of("123"), "123"));
		assertFalse(CollUtil.addIfAbsent(ListUtil.of(new Animal("jack", 20)),
			new Animal("jack", 20)));

		// 正常情况
		assertTrue(CollUtil.addIfAbsent(ListUtil.of("456"), "123"));
		assertTrue(CollUtil.addIfAbsent(ListUtil.of(new Animal("jack", 20)),
			new Dog("jack", 20)));
		assertTrue(CollUtil.addIfAbsent(ListUtil.of(new Animal("jack", 20)),
			new Animal("tom", 20)));
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class Animal {
		private String name;
		private Integer age;
	}

	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	@Data
	static class Dog extends Animal {
		public Dog(final String name, final Integer age) {
			super(name, age);
		}
	}

	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	@Data
	static class Cat extends Animal {

		public Cat(final String name, final Integer age) {
			super(name, age);
		}
	}

	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	@Data
	static class Pig extends Animal {

		public Pig(final String name, final Integer age) {
			super(name, age);
		}
	}

	@Test
	public void getFirstTest() {
		Assertions.assertNull(CollUtil.getFirst(null));
		Assertions.assertNull(CollUtil.getFirst(ListUtil.of()));

		assertEquals("1", CollUtil.getFirst(ListUtil.of("1", "2", "3")));
		final ArrayDeque<String> deque = new ArrayDeque<>();
		deque.add("3");
		deque.add("4");
		assertEquals("3", CollUtil.getFirst(deque));
	}

	@Test
	void getFirstTestWithPredicateTest(){
		final Animal dog = new Animal("dog", 2);
		final Animal cat = new Animal("cat", 3);
		final Animal bear = new Animal("bear", 4);

		final List<Animal> list = new ArrayList<>();
		list.add(dog);
		list.add(cat);
		list.add(bear);

		final Animal cat1 = CollUtil.getFirst(list, (t) -> t.getName().equals("cat"));
		assertNotNull(cat1);
		assertEquals("cat", cat1.getName());
	}

	@Test
	public void popPartTest() {
		final Stack<Integer> stack = new Stack<>();
		for (int i = 0; i < 10; i++) {
			stack.push(i);
		}
		final List<Integer> popPart1 = CollUtil.popPart(stack, 3);
		assertEquals(ListUtil.of(9, 8, 7), popPart1);
		assertEquals(7, stack.size());

		final ArrayDeque<Integer> queue = new ArrayDeque<>();
		for (int i = 0; i < 10; i++) {
			queue.push(i);
		}
		final List<Integer> popPart2 = CollUtil.popPart(queue, 3);
		assertEquals(ListUtil.of(9, 8, 7), popPart2);
		assertEquals(7, queue.size());
	}

	@Test
	public void isEqualListTest() {
		final List<Integer> list = ListUtil.of(1, 2, 3, 4);
		assertTrue(CollUtil.isEqualList(null, null));
		assertTrue(CollUtil.isEqualList(ListUtil.of(), ListUtil.of()));
		assertTrue(CollUtil.isEqualList(list, list));
		assertTrue(CollUtil.isEqualList(list, ListUtil.of(1, 2, 3, 4)));

		assertFalse(CollUtil.isEqualList(null, ListUtil.of()));
		assertFalse(CollUtil.isEqualList(list, ListUtil.of(1, 2, 3, 3)));
		assertFalse(CollUtil.isEqualList(list, ListUtil.of(1, 2, 3)));
		assertFalse(CollUtil.isEqualList(list, ListUtil.of(4, 3, 2, 1)));
	}

	@Test
	public void testMatch() {
		final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
		assertTrue(CollUtil.anyMatch(list, i -> i == 1));
		assertFalse(CollUtil.anyMatch(list, i -> i > 6));
		assertFalse(CollUtil.allMatch(list, i -> i == 1));
		assertTrue(CollUtil.allMatch(list, i -> i <= 6));
	}

	@Test
	public void maxTest() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
		assertEquals((Integer) 6, CollUtil.max(list));

		list = Arrays.asList(1, 2, 3, null, 5, 6);
		assertEquals((Integer) 6, CollUtil.max(list));
	}

	@Test
	public void minTest() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
		assertEquals((Integer) 1, CollUtil.min(list));

		list = Arrays.asList(1, 2, 3, null, 5, 6);
		assertEquals((Integer) 1, CollUtil.min(list));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Test
	public void maxEmptyTest() {
		final List<? extends Comparable> emptyList = Collections.emptyList();
		Assertions.assertNull(CollUtil.max(emptyList));
	}

	@Test
	public void minNullTest() {
		Assertions.assertNull(CollUtil.min(null));
	}

	@Test
	public void maxNullTest() {
		Assertions.assertNull(CollUtil.max(null));
	}

	@Test
	public void unionExtendTest() {
		final List<Dog> dog = Arrays.asList(new Dog("dog1", 12), new Dog("dog2", 12));
		final List<Cat> cat = Arrays.asList(new Cat("cat1", 12), new Cat("cat2", 12));
		assertEquals(CollUtil.union(dog, cat).size(), dog.size() + cat.size());
	}

	@Test
	public void unionAllExtendTest() {
		final List<Dog> dog = Arrays.asList(new Dog("dog1", 12), new Dog("dog2", 12));
		final List<Cat> cat = Arrays.asList(new Cat("cat1", 12), new Cat("cat2", 12));
		final List<Pig> pig = Arrays.asList(new Pig("pig1", 12), new Pig("pig2", 12));
		assertEquals(CollUtil.unionAll(dog, cat, pig).size(), dog.size() + cat.size() + pig.size());
	}

	@Test
	public void unionDistinctExtendTest() {
		final List<Dog> dog = Arrays.asList(new Dog("dog1", 12), new Dog("dog1", 12)); // same
		final List<Cat> cat = Arrays.asList(new Cat("cat1", 12), new Cat("cat2", 12));
		final List<Pig> pig = Arrays.asList(new Pig("pig1", 12), new Pig("pig2", 12));
		assertEquals(5, CollUtil.unionDistinct(dog, cat, pig).size());
	}


	@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
	@Test
	public void flatListTest1() {
		final List<List<List<String>>> list = Arrays.asList(Arrays.asList(Arrays.asList("1", "2", "3"), Arrays.asList("5", "6", "7")));

		final List<Object> objects = CollUtil.flat(list);

		Assertions.assertArrayEquals(new String[]{"1", "2", "3", "5", "6", "7"}, objects.toArray());
	}


	@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
	@Test
	public void flatListTest2() {
		final List<List<List<String>>> list = Arrays.asList(
			Arrays.asList(
				Arrays.asList("a"),
				Arrays.asList("b", "c"),
				Arrays.asList("d", "e", "f")
			),
			Arrays.asList(
				Arrays.asList("g", "h", "i"),
				Arrays.asList("j", "k", "l")
			)
		);
		final List<Object> flat = CollUtil.flat(list);
		Assertions.assertArrayEquals(new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l"}, flat.toArray());

	}


	@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
	@Test
	void flatListTest3() {
		final List<List<List<String>>> list = Arrays.asList(
			Arrays.asList(
				Arrays.asList("a"),
				Arrays.asList("b", "c", null),
				Arrays.asList("d", "e", "f")
			),
			Arrays.asList(
				Arrays.asList("g", "h", "i"),
				Arrays.asList("j", "k", "l")
			)
		);
		final List<Object> flat = CollUtil.flat(list, false);
		Assertions.assertArrayEquals(new String[]{"a", "b", "c", null, "d", "e", "f", "g", "h", "i", "j", "k", "l"}, flat.toArray());
	}

	@Test
	void issueI8Z2Q4Test() {
		final ArrayList<String> coll1 = new ArrayList<>();
		coll1.add("1");
		coll1.add("2");
		coll1.add("3");
		coll1.add("4");
		final ArrayList<String> coll2 = new ArrayList<>();
		coll2.add("1");
		coll2.add("1");
		coll2.add("1");
		coll2.add("1");
		coll2.add("1");

		assertTrue(CollUtil.containsAll(coll1, coll2));
	}

	@Test
	void issueIBHP68Test() {
		final List<Integer> view1 = ListUtil.view(1, 2, 3);
		final List<Integer> l3 = ListUtil.of(1);
		final Collection<Integer> subtract = CollUtil.subtract(view1, l3);
		assertEquals(2, subtract.size());
		assertEquals(2, subtract.iterator().next());
		assertFalse(subtract.contains(1));
		assertTrue(subtract.contains(2));
		assertTrue(subtract.contains(3));
	}

	@Test
	public void testPadLeft_NegativeMinLen_ShouldNotModifyList() {
		final List<String> list = ListUtil.of("a", "b", "c");
		final List<String> original = ListUtil.of("a", "b", "c");

		CollUtil.padLeft(list, -5, "x");

		assertEquals(original, list, "List should remain unchanged when minLen is negative");
	}

	@Test
	public void testPadLeft_EmptyList_MinLenZero() {
		final List<String> list = ListUtil.of();

		CollUtil.padLeft(list, 0, "x");

		assertTrue(list.isEmpty(), "List should remain empty when minLen is 0");
	}

	@Test
	public void testSubtractWithDuplicates() {
		final Collection<String> coll1 = new ArrayList<>(Arrays.asList("a", "b", "b", "c"));
		final Collection<String> coll2 = Collections.singletonList("b");
		final Collection<String> result = CollUtil.subtract(coll1, coll2);

		final List<String> expected = Arrays.asList("a", "c");
		final List<String> resultList = new ArrayList<>(result);
		Collections.sort(resultList);
		Collections.sort(expected);
		assertEquals(expected, resultList);
	}

	@Test
	public void lastIndexOf_NoMatchExists() {
		final List<String> list = ListUtil.of("a", "b", "c");
		final int idx = CollUtil.lastIndexOf(list, item -> item.equals("z"));
		assertEquals(-1, idx);
	}

	@Test
	public void lastIndexOf_MatcherIsNull_MatchAll() {
		final List<String> list = ListUtil.of("x", "y", "z");
		final int idx = CollUtil.lastIndexOf(list, null);
		assertEquals(2, idx);
	}

	@Test
	public void lastIndexOf_EmptyCollection() {
		final List<String> list = ListUtil.of();
		final int idx = CollUtil.lastIndexOf(list, Objects::nonNull);
		assertEquals(-1, idx);
	}

	@Test
	public void lastIndexOf_SingletonCollection_Match() {
		final List<String> list = ListUtil.of("foo");
		final int idx = CollUtil.lastIndexOf(list, item -> item.equals("foo"));
		assertEquals(0, idx);
	}

	@Test
	void issueIDBU9HTest(){
		final List<ToolTest> list = new ArrayList<>();
		final ToolTest t1 = new ToolTest("a");
		final ToolTest t2 = new ToolTest("b");
		list.add(t1);
		list.add(t2);
		final Map<String, ToolTest> map = list.stream().collect(Collectors.toMap(ToolTest::getName, Function.identity(), (k1, k2) -> k2));
		CollUtil.subtract(map.keySet(), map.keySet());
	}

	@Data
	@AllArgsConstructor
	private static class ToolTest {
		private String name;
	}
}
