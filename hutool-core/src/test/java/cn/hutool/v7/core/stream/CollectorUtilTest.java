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

package cn.hutool.v7.core.stream;

import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.lang.tuple.Pair;
import cn.hutool.v7.core.lang.tuple.Triple;
import cn.hutool.v7.core.map.MapUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CollectorUtilTest
 *
 * @author VampireAchao
 * @since 2022/7/3
 */
public class CollectorUtilTest {

	@Test
	public void reduceListMapTest() {
		final Set<Map<String, Integer>> nameScoreMapList = StreamUtil.of(
				// 集合内的第一个map，包含两个key value
				MapUtil.builder("苏格拉底", 1).put("特拉叙马霍斯", 3).build(),
				MapUtil.of("苏格拉底", 2),
				MapUtil.of("特拉叙马霍斯", 1),
				MapUtil.of("特拉叙马霍斯", 2)
		).collect(Collectors.toSet());
		// 执行聚合
		final Map<String, List<Integer>> nameScoresMap = nameScoreMapList.stream().collect(CollectorUtil.reduceListMap());

		Assertions.assertEquals(MapUtil.builder("苏格拉底", Arrays.asList(1, 2))
						.put("特拉叙马霍斯", Arrays.asList(3, 1, 2)).build(),
				nameScoresMap);

		final List<Map<String, String>> data = ListUtil.of(
			MapUtil.builder("name", "sam").put("count", "80").map(),
			MapUtil.builder("name", "sam").put("count", "81").map(),
			MapUtil.builder("name", "sam").put("count", "82").map(),
			MapUtil.builder("name", "jack").put("count", "80").map(),
			MapUtil.builder("name", "jack").put("count", "90").map()
		);

		final Map<String, Map<String, List<String>>> nameMap = data.stream()
			.collect(Collectors.groupingBy(e -> e.get("name"), CollectorUtil.reduceListMap()));
		Assertions.assertEquals(MapUtil.builder("jack", MapUtil.builder("name", Arrays.asList("jack", "jack"))
				.put("count", Arrays.asList("80", "90")).build())
			.put("sam", MapUtil.builder("name", Arrays.asList("sam", "sam", "sam"))
				.put("count", Arrays.asList("80", "81", "82")).build())
			.build(), nameMap);
	}

	@Test
	public void testTransform() {
		Stream<Integer> stream = Stream.of(1, 2, 3, 4)
				.collect(CollectorUtil.transform(EasyStream::of));
		Assertions.assertEquals(EasyStream.class, stream.getClass());

		stream = Stream.of(1, 2, 3, 4)
				.collect(CollectorUtil.transform(HashSet::new, EasyStream::of));
		Assertions.assertEquals(EasyStream.class, stream.getClass());
	}

	@Test
	public void testToEasyStream() {
		final Stream<Integer> stream = Stream.of(1, 2, 3, 4)
				.collect(CollectorUtil.toEasyStream());
		Assertions.assertEquals(EasyStream.class, stream.getClass());
	}

	@Test
	public void testToEntryStream() {
		final Map<String, Integer> map = Stream.of(1, 2, 3, 4, 5)
				// 转为EntryStream
				.collect(CollectorUtil.toEntryStream(Function.identity(), String::valueOf))
				// 过滤偶数
				.filterByKey(k -> (k & 1) == 1)
				.inverse()
				.toMap();
		Assertions.assertEquals((Integer) 1, map.get("1"));
		Assertions.assertEquals((Integer) 3, map.get("3"));
		Assertions.assertEquals((Integer) 5, map.get("5"));
	}

	@Test
	public void testFiltering() {
		final Map<Integer, Long> map = Stream.of(1, 2, 3)
				.collect(Collectors.groupingBy(Function.identity(),
						CollectorUtil.filtering(i -> i > 1, Collectors.counting())
				));
		Assertions.assertEquals(MapUtil.builder()
				.put(1, 0L)
				.put(2, 1L)
				.put(3, 1L)
				.build(), map);
	}

	@Test
	public void testGroupingByAfterValueMapped() {
		final List<Integer> list = Arrays.asList(1, 1, 2, 2, 3, 4);
		Map<Boolean, Set<String>> map = list.stream()
			.collect(CollectorUtil.groupingBy(t -> (t & 1) == 0, String::valueOf, LinkedHashSet::new, LinkedHashMap::new));

		Assertions.assertEquals(LinkedHashMap.class, map.getClass());
		Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("2", "4")), map.get(Boolean.TRUE));
		Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("1", "3")), map.get(Boolean.FALSE));

		map = list.stream()
			.collect(CollectorUtil.groupingBy(t -> (t & 1) == 0, String::valueOf, LinkedHashSet::new));
		Assertions.assertEquals(HashMap.class, map.getClass());
		Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("2", "4")), map.get(Boolean.TRUE));
		Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList("1", "3")), map.get(Boolean.FALSE));

		final Map<Boolean, List<String>> map2 = list.stream()
			.collect(CollectorUtil.groupingBy(t -> (t & 1) == 0, String::valueOf));
		Assertions.assertEquals(Arrays.asList("2", "2", "4"), map2.get(Boolean.TRUE));
		Assertions.assertEquals(Arrays.asList("1", "1", "3"), map2.get(Boolean.FALSE));

	}


	@Test
	public void testToPairList() {
		final List<Pair<Integer,String>> list = Arrays.asList(Pair.of(1,"one"), Pair.of(2,"two"));
		final Pair<List<Integer>, List<String>> pairList = list.stream()
				.collect(CollectorUtil.toPairList(Pair::getLeft, Pair::getRight));

		Assertions.assertEquals(pairList.getLeft().size(),list.size());
		Assertions.assertEquals(pairList.getRight().size(),list.size());

		final Pair<HashSet<Integer>, ArrayList<String>> pairMixed = list.stream()
				.collect(CollectorUtil.toPairCollection(Pair::getLeft, Pair::getRight, HashSet::new, ArrayList::new));

		Assertions.assertEquals(pairMixed.getLeft().size(),list.size());
		Assertions.assertEquals(pairMixed.getRight().size(),list.size());

	}


	@Test
	public void testToTripleList() {
		final List<Triple<Integer,Long,String>> list
				= Arrays.asList(Triple.of(1,1L,"one"), Triple.of(2,2L,"two"));
		final Triple<List<Integer>, List<Long>, List<String>> tripleList = list.stream()
				.collect(CollectorUtil.toTripleList(Triple::getLeft, Triple::getMiddle, Triple::getRight));

		Assertions.assertEquals(tripleList.getLeft().size(),list.size());
		Assertions.assertEquals(tripleList.getMiddle().size(),list.size());
		Assertions.assertEquals(tripleList.getRight().size(),list.size());

		final Triple<HashSet<Integer>, HashSet<Long>, ArrayList<String>> tripleMixed = list.stream()
				.collect(CollectorUtil.toTripleCollection(Triple::getLeft, Triple::getMiddle, Triple::getRight, HashSet::new, HashSet::new, ArrayList::new));

		Assertions.assertEquals(tripleMixed.getLeft().size(),list.size());
		Assertions.assertEquals(tripleMixed.getMiddle().size(),list.size());
		Assertions.assertEquals(tripleMixed.getRight().size(),list.size());

	}

}
