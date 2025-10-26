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

package cn.hutool.v7.core.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import cn.hutool.v7.core.map.MapUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * CollectionStream测试方法
 */
public class CollStreamUtilTest {

	@Test
	public void testToIdentityMap() {
		Map<Long, Student> map = CollStreamUtil.toIdentityMap(null, Student::getStudentId);
		assertEquals(Collections.EMPTY_MAP, map);
		final List<Student> list = new ArrayList<>();
		map = CollStreamUtil.toIdentityMap(list, Student::getStudentId);
		assertEquals(Collections.EMPTY_MAP, map);
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 1, 2, "李四"));
		list.add(new Student(1, 1, 3, "王五"));
		map = CollStreamUtil.toIdentityMap(list, Student::getStudentId);
		assertEquals("张三", map.get(1L).getName());
		assertEquals("李四", map.get(2L).getName());
		assertEquals("王五", map.get(3L).getName());
		Assertions.assertNull(map.get(4L));

		// 测试value为空时
		list.add(null);
		map = CollStreamUtil.toIdentityMap(list, Student::getStudentId);
		Assertions.assertNull(map.get(4L));
	}

	@Test
	public void testToMap() {
		Map<Long, String> map = CollStreamUtil.toMap(null, Student::getStudentId, Student::getName);
		assertEquals(Collections.EMPTY_MAP, map);
		final List<Student> list = new ArrayList<>();
		map = CollStreamUtil.toMap(list, Student::getStudentId, Student::getName);
		assertEquals(Collections.EMPTY_MAP, map);
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 1, 2, "李四"));
		list.add(new Student(1, 1, 3, "王五"));
		map = CollStreamUtil.toMap(list, Student::getStudentId, Student::getName);
		assertEquals("张三", map.get(1L));
		assertEquals("李四", map.get(2L));
		assertEquals("王五", map.get(3L));
		Assertions.assertNull(map.get(4L));

		// 测试value为空时
		list.add(new Student(1, 1, 4, null));
		map = CollStreamUtil.toMap(list, Student::getStudentId, Student::getName);
		Assertions.assertNull(map.get(4L));
	}

	@Test
	public void testGroupByKey() {
		Map<Long, List<Student>> map = CollStreamUtil.groupByKey(null, Student::getClassId);
		assertEquals(Collections.EMPTY_MAP, map);
		final List<Student> list = new ArrayList<>();
		map = CollStreamUtil.groupByKey(list, Student::getClassId);
		assertEquals(Collections.EMPTY_MAP, map);
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 2, 2, "李四"));
		list.add(new Student(2, 1, 1, "擎天柱"));
		list.add(new Student(2, 2, 2, "威震天"));
		list.add(new Student(2, 3, 2, "霸天虎"));
		map = CollStreamUtil.groupByKey(list, Student::getClassId);
		final Map<Long, List<Student>> compare = new HashMap<>();
		final List<Student> class1 = new ArrayList<>();
		class1.add(new Student(1, 1, 1, "张三"));
		class1.add(new Student(2, 1, 1, "擎天柱"));
		compare.put(1L, class1);
		final List<Student> class2 = new ArrayList<>();
		class2.add(new Student(1, 2, 2, "李四"));
		class2.add(new Student(2, 2, 2, "威震天"));

		compare.put(2L, class2);
		final List<Student> class3 = new ArrayList<>();
		class3.add(new Student(2, 3, 2, "霸天虎"));
		compare.put(3L, class3);
		assertEquals(map, compare);
	}

	@Test
	public void testGroupBy2Key() {
		Map<Long, Map<Long, List<Student>>> map = CollStreamUtil.groupBy2Key(null, Student::getTermId, Student::getClassId);
		assertEquals(Collections.EMPTY_MAP, map);
		final List<Student> list = new ArrayList<>();
		map = CollStreamUtil.groupBy2Key(list, Student::getTermId, Student::getClassId);
		assertEquals(Collections.EMPTY_MAP, map);
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 2, 2, "李四"));
		list.add(new Student(1, 2, 3, "王五"));
		list.add(new Student(2, 1, 1, "擎天柱"));
		list.add(new Student(2, 2, 2, "威震天"));
		list.add(new Student(2, 2, 3, "霸天虎"));
		map = CollStreamUtil.groupBy2Key(list, Student::getTermId, Student::getClassId);
		final Map<Long, Map<Long, List<Student>>> compare = new HashMap<>();
		final Map<Long, List<Student>> map1 = new HashMap<>();
		final List<Student> list11 = new ArrayList<>();
		list11.add(new Student(1, 1, 1, "张三"));
		map1.put(1L, list11);
		compare.put(1L, map1);
		final List<Student> list12 = new ArrayList<>();
		list12.add(new Student(1, 2, 2, "李四"));
		list12.add(new Student(1, 2, 3, "王五"));
		map1.put(2L, list12);
		compare.put(2L, map1);
		final Map<Long, List<Student>> map2 = new HashMap<>();
		final List<Student> list21 = new ArrayList<>();
		list21.add(new Student(2, 1, 1, "擎天柱"));
		map2.put(1L, list21);
		compare.put(2L, map2);

		final List<Student> list22 = new ArrayList<>();
		list22.add(new Student(2, 2, 2, "威震天"));
		list22.add(new Student(2, 2, 3, "霸天虎"));
		map2.put(2L, list22);
		compare.put(2L, map2);
		assertEquals(map, compare);
	}

	@Test
	public void testGroup2Map() {
		Map<Long, Map<Long, Student>> map = CollStreamUtil.group2Map(null, Student::getTermId, Student::getClassId);
		assertEquals(Collections.EMPTY_MAP, map);

		final List<Student> list = new ArrayList<>();
		map = CollStreamUtil.group2Map(list, Student::getTermId, Student::getClassId);
		assertEquals(Collections.EMPTY_MAP, map);
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 2, 1, "李四"));
		list.add(new Student(2, 2, 1, "王五"));
		map = CollStreamUtil.group2Map(list, Student::getTermId, Student::getClassId);
		final Map<Long, Map<Long, Student>> compare = new HashMap<>();
		final Map<Long, Student> map1 = new HashMap<>();
		map1.put(1L, new Student(1, 1, 1, "张三"));
		map1.put(2L, new Student(1, 2, 1, "李四"));
		compare.put(1L, map1);
		final Map<Long, Student> map2 = new HashMap<>();
		map2.put(2L, new Student(2, 2, 1, "王五"));
		compare.put(2L, map2);
		assertEquals(compare, map);

		// 对null友好
		final Map<Long, Map<Long, Student>> termIdClassIdStudentMap = CollStreamUtil.group2Map(Arrays.asList(null, new Student(2, 2, 1, "王五")), Student::getTermId, Student::getClassId);
		final Map<Long, Map<Long, Student>> termIdClassIdStudentCompareMap = new HashMap<>() {
			@Serial
			private static final long serialVersionUID = 1L;

			{
				put(null, MapUtil.empty());
				put(2L, MapUtil.of(2L, new Student(2, 2, 1, "王五")));
			}
		};
		assertEquals(termIdClassIdStudentCompareMap, termIdClassIdStudentMap);
	}

	@Test
	public void testGroupKeyValue() {
		Map<Long, List<Long>> map = CollStreamUtil.groupKeyValue(null, Student::getTermId, Student::getClassId);
		assertEquals(Collections.EMPTY_MAP, map);

		final List<Student> list = new ArrayList<>();
		map = CollStreamUtil.groupKeyValue(list, Student::getTermId, Student::getClassId);
		assertEquals(Collections.EMPTY_MAP, map);
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 2, 1, "李四"));
		list.add(new Student(2, 2, 1, "王五"));
		map = CollStreamUtil.groupKeyValue(list, Student::getTermId, Student::getClassId);

		final Map<Long, List<Long>> compare = new HashMap<>();
		compare.put(1L, Arrays.asList(1L, 2L));
		compare.put(2L, Collections.singletonList(2L));
		assertEquals(compare, map);
	}

	@Test
	public void testGroupBy() {
		// groupBy作为之前所有group函数的公共部分抽取出来，并更接近于jdk原生，灵活性更强

		// 参数null测试
		Map<Long, List<Student>> map = CollStreamUtil.groupBy(null, Student::getTermId, Collectors.toList());
		assertEquals(Collections.EMPTY_MAP, map);

		// 参数空数组测试
		final List<Student> list = new ArrayList<>();
		map = CollStreamUtil.groupBy(list, Student::getTermId, Collectors.toList());
		assertEquals(Collections.EMPTY_MAP, map);

		// 放入元素
		list.add(new Student(1, 1, 1, "张三"));
		list.add(new Student(1, 2, 1, "李四"));
		list.add(new Student(2, 2, 1, "王五"));
		// 先根据termId分组，再通过classId比较，找出最大值所属的那个Student,返回的Optional
		final Map<Long, Optional<Student>> longOptionalMap = CollStreamUtil.groupBy(list, Student::getTermId, Collectors.maxBy(Comparator.comparing(Student::getClassId)));
		//noinspection OptionalGetWithoutIsPresent
		assertEquals("李四", longOptionalMap.get(1L).get().getName());

		// 先根据termId分组，再转换为Map<studentId,name>
		final Map<Long, HashMap<Long, String>> groupThen = CollStreamUtil.groupBy(list, Student::getTermId, Collector.of(HashMap::new, (m, v) -> m.put(v.getStudentId(), v.getName()), (l, r) -> l));
		assertEquals(
				MapUtil.builder()
						.put(1L, MapUtil.builder().put(1L, "李四").build())
						.put(2L, MapUtil.builder().put(1L, "王五").build())
						.build(),
				groupThen);

		// 总之，如果你是想要group分组后还要进行别的操作，用它就对了！
		// 并且对null值进行了友好处理，例如
		final List<Student> students = Arrays.asList(null, null, new Student(1, 1, 1, "张三"),
				new Student(1, 2, 1, "李四"));
		final Map<Long, List<Student>> termIdStudentsMap = CollStreamUtil.groupBy(students, Student::getTermId, Collectors.toList());
		final Map<Long, List<Student>> termIdStudentsCompareMap = new HashMap<>();
		termIdStudentsCompareMap.put(null, Collections.emptyList());
		termIdStudentsCompareMap.put(1L, Arrays.asList(new Student(1L, 1, 1, "张三"), new Student(1L, 2, 1, "李四")));
		assertEquals(termIdStudentsCompareMap, termIdStudentsMap);

		final Map<Long, Long> termIdCountMap = CollStreamUtil.groupBy(students, Student::getTermId, Collectors.counting());
		final Map<Long, Long> termIdCountCompareMap = new HashMap<>();
		termIdCountCompareMap.put(null, 2L);
		termIdCountCompareMap.put(1L, 2L);
		assertEquals(termIdCountCompareMap, termIdCountMap);
	}


	@Test
	public void testTranslate2List() {
		List<String> list = CollStreamUtil.toList(null, Student::getName);
		assertEquals(Collections.EMPTY_LIST, list);
		final List<Student> students = new ArrayList<>();
		list = CollStreamUtil.toList(students, Student::getName);
		assertEquals(Collections.EMPTY_LIST, list);
		students.add(new Student(1, 1, 1, "张三"));
		students.add(new Student(1, 2, 2, "李四"));
		students.add(new Student(2, 1, 1, "李四"));
		students.add(new Student(2, 2, 2, "李四"));
		students.add(new Student(2, 3, 2, "霸天虎"));
		list = CollStreamUtil.toList(students, Student::getName);
		final List<String> compare = new ArrayList<>();
		compare.add("张三");
		compare.add("李四");
		compare.add("李四");
		compare.add("李四");
		compare.add("霸天虎");
		assertEquals(list, compare);
	}

	@Test
	public void testTranslate2Set() {
		Set<String> set = CollStreamUtil.toSet(null, Student::getName);
		assertEquals(Collections.EMPTY_SET, set);
		final List<Student> students = new ArrayList<>();
		set = CollStreamUtil.toSet(students, Student::getName);
		assertEquals(Collections.EMPTY_SET, set);
		students.add(new Student(1, 1, 1, "张三"));
		students.add(new Student(1, 2, 2, "李四"));
		students.add(new Student(2, 1, 1, "李四"));
		students.add(new Student(2, 2, 2, "李四"));
		students.add(new Student(2, 3, 2, "霸天虎"));
		set = CollStreamUtil.toSet(students, Student::getName);
		final Set<String> compare = new HashSet<>();
		compare.add("张三");
		compare.add("李四");
		compare.add("霸天虎");
		assertEquals(set, compare);
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void testMerge() {
		Map<Long, Student> map1 = null;
		Map<Long, Student> map2 = Collections.emptyMap();
		Map<Long, String> map = CollStreamUtil.merge(map1, map2, (s1, s2) -> s1.getName() + s2.getName());
		assertEquals(Collections.EMPTY_MAP, map);
		map1 = new HashMap<>();
		map1.put(1L, new Student(1, 1, 1, "张三"));
		map = CollStreamUtil.merge(map1, map2, this::merge);
		final Map<Long, String> temp = new HashMap<>();
		temp.put(1L, "张三");
		assertEquals(map, temp);
		map2 = new HashMap<>();
		map2.put(1L, new Student(2, 1, 1, "李四"));
		map = CollStreamUtil.merge(map1, map2, this::merge);
		final Map<Long, String> compare = new HashMap<>();
		compare.put(1L, "张三李四");
		assertEquals(map, compare);
	}

	private String merge(final Student student1, final Student student2) {
		if (student1 == null && student2 == null) {
			return null;
		} else if (student1 == null) {
			return student2.getName();
		} else if (student2 == null) {
			return student1.getName();
		} else {
			return student1.getName() + student2.getName();
		}
	}

	@Test
	void cartesianProductTest() {
		final List<List<Object>> sets = new ArrayList<>();
		sets.add(ListUtil.of(10, 20));
		sets.add(ListUtil.of("John", "Jack"));
		sets.add(ListUtil.of('I', 'J'));

		final List<List<Object>> collect = CollStreamUtil.cartesianProduct(sets, 0).toList();
		assertEquals(8, collect.size());
		assertEquals("[10, John, I]", collect.get(0).toString());
		assertEquals("[10, John, J]", collect.get(1).toString());
		assertEquals("[10, Jack, I]", collect.get(2).toString());
		assertEquals("[10, Jack, J]", collect.get(3).toString());
		assertEquals("[20, John, I]", collect.get(4).toString());
		assertEquals("[20, John, J]", collect.get(5).toString());
		assertEquals("[20, Jack, I]", collect.get(6).toString());
		assertEquals("[20, Jack, J]", collect.get(7).toString());
	}

	/**
	 * 班级类
	 */
	@Data
	@AllArgsConstructor
	@ToString
	public static class Student {
		private long termId;//学期id
		private long classId;//班级id
		private long studentId;//班级id
		private String name;//学生名称
	}

	@Test
	public void testToMap_KeyCollision_SilentlyOverwrite() {
		final List<Student> list = new ArrayList<>();
		list.add(new Student(1, 101, 1, "张三"));
		list.add(new Student(1, 102, 1, "李四"));
		final Map<Long, String> map = CollStreamUtil.toMap(list, Student::getStudentId, Student::getName, false);

		assertEquals(1, map.size());
		assertEquals("李四", map.get(1L));   // 确保后面的值覆盖前面的
	}

	@Test
	public void testToMap_NullKeyOrValue() {
		final List<Student> list = new ArrayList<>();
		list.add(new Student(1, 1, 1L, "张三"));
		list.add(null);
		list.add(new Student(1, 2, 2L, null));

		assertThrows(NullPointerException.class, () -> CollStreamUtil.toMap(list, Student::getStudentId, Student::getName));
	}

	@Test
	public void testToMap_LargeInputPerformance() {
		final List<Student> list = new ArrayList<>();
		for (long i = 0; i < 10000; i++) {
			list.add(new Student(1, 1, i, "学生" + i));
		}
		final Map<Long, String> map = CollStreamUtil.toMap(list, Student::getStudentId, Student::getName);

		assertEquals(10000, map.size());
	}
}
