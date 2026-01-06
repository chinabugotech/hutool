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

import cn.hutool.v7.core.convert.ConvertUtil;
import cn.hutool.v7.core.lang.Opt;
import cn.hutool.v7.core.reflect.TypeReference;
import cn.hutool.v7.core.text.StrUtil;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serial;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MapUtilTest {

	enum PeopleEnum {GIRL, BOY, CHILD}

	@Data
	@Builder
	static class User {
		private Long id;
		private String name;
	}

	@Data
	@Builder
	static class Group {
		private Long id;
		private List<User> users;
	}

	@Data
	@Builder
	static class UserGroup {
		private Long userId;
		private Long groupId;
	}


	@Test
	void filterTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> map2 = MapUtil.filter(map, t -> ConvertUtil.toInt(t.getValue()) % 2 == 0);

		Assertions.assertEquals(2, map2.size());

		Assertions.assertEquals("2", map2.get("b"));
		Assertions.assertEquals("4", map2.get("d"));
	}

	@Test
	void mapTest() {
		// Add test like a foreigner
		final Map<Integer, String> adjectivesMap = MapUtil.<Integer, String>builder()
			.put(0, "lovely")
			.put(1, "friendly")
			.put(2, "happily")
			.build();

		final Map<Integer, String> resultMap = MapUtil.map(adjectivesMap, (k, v) -> v + " " + PeopleEnum.values()[k].name().toLowerCase());

		Assertions.assertEquals("lovely girl", resultMap.get(0));
		Assertions.assertEquals("friendly boy", resultMap.get(1));
		Assertions.assertEquals("happily child", resultMap.get(2));

		// 下单用户，Queue表示正在 .排队. 抢我抢不到的二次元周边！
		final Queue<String> customers = new ArrayDeque<>(Arrays.asList("刑部尚书手工耿", "木瓜大盗大漠叔", "竹鼠发烧找华农", "朴实无华朱一旦"));
		// 分组
		final List<Group> groups = Stream.iterate(0L, i -> ++i).limit(4).map(i -> Group.builder().id(i).build()).toList();
		// 如你所见，它是一个map，key由用户id，value由用户组成
		final Map<Long, User> idUserMap = Stream.iterate(0L, i -> ++i).limit(4).map(i -> User.builder().id(i).name(customers.poll()).build()).collect(Collectors.toMap(User::getId, Function.identity()));
		// 如你所见，它是一个map，key由分组id，value由用户ids组成，典型的多对多关系
		final Map<Long, List<Long>> groupIdUserIdsMap = groups.stream().flatMap(group -> idUserMap.keySet().stream().map(userId -> UserGroup.builder().groupId(group.getId()).userId(userId).build())).collect(Collectors.groupingBy(UserGroup::getUserId, Collectors.mapping(UserGroup::getGroupId, Collectors.toList())));

		// 神奇的魔法发生了， 分组id和用户ids组成的map，竟然变成了订单编号和用户实体集合组成的map
		final Map<Long, List<User>> groupIdUserMap = MapUtil.map(groupIdUserIdsMap, (groupId, userIds) -> userIds.stream().map(idUserMap::get).collect(Collectors.toList()));

		// 然后你就可以拿着这个map，去封装groups，使其能够在订单数据带出客户信息啦
		groups.forEach(group -> Opt.ofNullable(group.getId()).map(groupIdUserMap::get).ifPresent(group::setUsers));

		// 下面是测试报告
		groups.forEach(group -> {
			final List<User> users = group.getUsers();
			Assertions.assertEquals("刑部尚书手工耿", users.get(0).getName());
			Assertions.assertEquals("木瓜大盗大漠叔", users.get(1).getName());
			Assertions.assertEquals("竹鼠发烧找华农", users.get(2).getName());
			Assertions.assertEquals("朴实无华朱一旦", users.get(3).getName());
		});
		// 能写代码真开心
	}

	@Test
	void filterMapWrapperTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> camelCaseMap = MapUtil.toCamelCaseMap(map);

		final Map<String, String> map2 = MapUtil.filter(camelCaseMap, t -> ConvertUtil.toInt(t.getValue()) % 2 == 0);

		Assertions.assertEquals(2, map2.size());

		Assertions.assertEquals("2", map2.get("b"));
		Assertions.assertEquals("4", map2.get("d"));
	}

	@Test
	void filterContainsTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("abc", "1");
		map.put("bcd", "2");
		map.put("def", "3");
		map.put("fgh", "4");

		final Map<String, String> map2 = MapUtil.filter(map, t -> StrUtil.contains(t.getKey(), "bc"));
		Assertions.assertEquals(2, map2.size());
		Assertions.assertEquals("1", map2.get("abc"));
		Assertions.assertEquals("2", map2.get("bcd"));
	}

	@Test
	void editTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> map2 = MapUtil.edit(map, t -> {
			// 修改每个值使之*10
			t.setValue(t.getValue() + "0");
			return t;
		});

		Assertions.assertEquals(4, map2.size());

		Assertions.assertEquals("10", map2.get("a"));
		Assertions.assertEquals("20", map2.get("b"));
		Assertions.assertEquals("30", map2.get("c"));
		Assertions.assertEquals("40", map2.get("d"));
	}

	@Test
	void reverseTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> map2 = MapUtil.reverse(map);

		Assertions.assertEquals("a", map2.get("1"));
		Assertions.assertEquals("b", map2.get("2"));
		Assertions.assertEquals("c", map2.get("3"));
		Assertions.assertEquals("d", map2.get("4"));
	}

	@Test
	void toObjectArrayTest() {
		final Map<String, String> map = MapUtil.newHashMap(true);
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Object[][] objectArray = MapUtil.toObjectArray(map);
		Assertions.assertEquals("a", objectArray[0][0]);
		Assertions.assertEquals("1", objectArray[0][1]);
		Assertions.assertEquals("b", objectArray[1][0]);
		Assertions.assertEquals("2", objectArray[1][1]);
		Assertions.assertEquals("c", objectArray[2][0]);
		Assertions.assertEquals("3", objectArray[2][1]);
		Assertions.assertEquals("d", objectArray[3][0]);
		Assertions.assertEquals("4", objectArray[3][1]);
	}

	@Test
	void sortJoinTest() {
		final Map<String, String> build = MapUtil.builder(new HashMap<String, String>())
			.put("key1", "value1")
			.put("key3", "value3")
			.put("key2", "value2").build();

		final String join1 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false);
		Assertions.assertEquals("key1value1key2value2key3value3", join1);

		final String join2 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false, "123");
		Assertions.assertEquals("key1value1key2value2key3value3123", join2);

		final String join3 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false, "123", "abc");
		Assertions.assertEquals("key1value1key2value2key3value3123abc", join3);
	}

	@Test
	void ofEntriesTest() {
		final Map<String, Integer> map = MapUtil.ofEntries(MapUtil.entry("a", 1), MapUtil.entry("b", 2));
		Assertions.assertEquals(2, map.size());

		Assertions.assertEquals(Integer.valueOf(1), map.get("a"));
		Assertions.assertEquals(Integer.valueOf(2), map.get("b"));
	}

	@Test
	void getIntValueTest() {
		final Map<String, String> map = MapUtil.ofEntries(MapUtil.entry("a", "1"), MapUtil.entry("b", null));
		final int a = MapUtil.get(map, "a", int.class);
		Assertions.assertEquals(1, a);

		final int b = MapUtil.getInt(map, "b", 0);
		Assertions.assertEquals(0, b);
	}

	@Test
	void valuesOfKeysTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final Dict v2 = Dict.of().set("age", 13).set("id", 15).set("name", "李四");

		final String[] keys = v1.keySet().toArray(new String[0]);
		final ArrayList<Object> v1s = MapUtil.valuesOfKeys(v1, keys);
		Assertions.assertTrue(v1s.contains(12));
		Assertions.assertTrue(v1s.contains(23));
		Assertions.assertTrue(v1s.contains("张三"));

		final ArrayList<Object> v2s = MapUtil.valuesOfKeys(v2, keys);
		Assertions.assertTrue(v2s.contains(15));
		Assertions.assertTrue(v2s.contains(13));
		Assertions.assertTrue(v2s.contains("李四"));
	}

	@Test
	void createMapTest() {
		final Map<Object, Object> map = MapUtil.createMap(MapUtil.view(new HashMap<>()).getClass());
		Assertions.assertEquals(HashMap.class, map.getClass());
	}

	@Test
	public void removeNullValueTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", null).set("age", null);
		final Map<String, Object> map = MapUtil.removeNullValue(v1);
		Assertions.assertEquals(1, map.size());
	}

	@Test
	public void ofKvsLinkTest() {
		final Map<String, Long> map2 = MapUtil.ofKvs(true,
			"RED", 0xFF0000,
			"GREEN", 0x00FF00,
			"BLUE", 0x0000FF);
		Assertions.assertEquals(3, map2.size());
		Assertions.assertEquals(LinkedHashMap.class, map2.getClass());
	}

	@Test
	public void ofEntriesSimpleEntryTest() {
		final Map<String, Integer> map = MapUtil.ofEntries(
			MapUtil.entry("a", 1, false),
			MapUtil.entry("b", 2, false)
		);
		assertEquals(2, map.size());

		assertEquals(Integer.valueOf(1), map.get("a"));
		assertEquals(Integer.valueOf(2), map.get("b"));
	}

	@Test
	public void getIntTest() {
		assertThrows(NumberFormatException.class, () -> {
			final HashMap<String, String> map = MapUtil.of("age", "d");
			final Integer age = MapUtil.getInt(map, "age");
			assertNotNull(age);
		});
	}

	@Test
	public void joinIgnoreNullTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", null);
		final String s = MapUtil.joinIgnoreNull(v1, ",", "=");
		assertEquals("id=12,name=张三", s);
	}

	@Test
	public void renameKeyTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", null);
		final Map<String, Object> map = MapUtil.renameKey(v1, "name", "newName");
		assertEquals("张三", map.get("newName"));
	}

	@Test
	public void renameKeyMapEmptyNoChange() {
		final Map<String, String> map = new HashMap<>();
		final Map<String, String> result = MapUtil.renameKey(map, "oldKey", "newKey");
		assertTrue(result.isEmpty());
	}

	@Test
	public void renameKeyOldKeyNotPresentNoChange() {
		final Map<String, String> map = new HashMap<>();
		map.put("anotherKey", "value");
		final Map<String, String> result = MapUtil.renameKey(map, "oldKey", "newKey");
		assertEquals(1, result.size());
		assertEquals("value", result.get("anotherKey"));
	}

	@Test
	public void renameKeyOldKeyPresentNewKeyNotPresentKeyRenamed() {
		final Map<String, String> map = new HashMap<>();
		map.put("oldKey", "value");
		final Map<String, String> result = MapUtil.renameKey(map, "oldKey", "newKey");
		assertEquals(1, result.size());
		assertEquals("value", result.get("newKey"));
	}

	@Test
	public void renameKeyNewKeyPresentThrowsException() {
		final Map<String, String> map = new HashMap<>();
		map.put("oldKey", "value");
		map.put("newKey", "existingValue");
		assertThrows(IllegalArgumentException.class, () ->
			MapUtil.renameKey(map, "oldKey", "newKey"));
	}

	@Test
	public void issue3162Test() {
		final Map<String, Object> map = new HashMap<>() {
			@Serial
			private static final long serialVersionUID = 1L;

			{
				put("a", "1");
				put("b", "2");
				put("c", "3");
			}
		};
		final Map<String, Object> filtered = MapUtil.filter(map, "a", "b");
		assertEquals(2, filtered.size());
		assertEquals("1", filtered.get("a"));
		assertEquals("2", filtered.get("b"));
	}


	@Test
	public void partitionNullMapThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> MapUtil.partition(null, 2));
	}

	@Test
	public void partitionSizeZeroThrowsException() {
		final Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		assertThrows(IllegalArgumentException.class, () -> MapUtil.partition(map, 0));
	}

	@Test
	public void partitionSizeNegativeThrowsException() {
		final Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		assertThrows(IllegalArgumentException.class, () -> MapUtil.partition(map, -1));
	}

	@Test
	public void partitionEmptyMapReturnsEmptyList() {
		final Map<String, String> map = new HashMap<>();
		final List<Map<String, String>> result = MapUtil.partition(map, 2);
		assertTrue(result.isEmpty());
	}

	@Test
	public void partitionMapSizeMultipleOfSizePartitionsCorrectly() {
		final Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final List<Map<String, String>> result = MapUtil.partition(map, 2);

		assertEquals(2, result.size());
		assertEquals(2, result.get(0).size());
		assertEquals(2, result.get(1).size());
	}

	@Test
	public void partitionMapSizeNotMultipleOfSizePartitionsCorrectly() {
		final Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");
		map.put("e", "5");

		final List<Map<String, String>> result = MapUtil.partition(map, 2);

		assertEquals(3, result.size());
		assertEquals(2, result.get(0).size());
		assertEquals(2, result.get(1).size());
		assertEquals(1, result.get(2).size());
	}

	@Test
	public void partitionGeneralCasePartitionsCorrectly() {
		final Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");
		map.put("e", "5");
		map.put("f", "6");

		final List<Map<String, String>> result = MapUtil.partition(map, 3);

		assertEquals(2, result.size());
		assertEquals(3, result.get(0).size());
		assertEquals(3, result.get(1).size());
	}

	//----------valuesOfKeys
	@Test
	public void valuesOfKeysEmptyIteratorReturnsEmptyList() {
		final Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		final Iterator<String> emptyIterator = Collections.emptyIterator();
		final ArrayList<String> result = MapUtil.valuesOfKeys(map, emptyIterator);
		assertEquals(new ArrayList<String>(), result);
	}

	@Test
	public void valuesOfKeysNonEmptyIteratorReturnsValuesList() {
		final Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		final Iterator<String> iterator = new ArrayList<String>() {
			@Serial
			private static final long serialVersionUID = -4593258366224032110L;

			{
				add("a");
				add("b");
			}
		}.iterator();
		final ArrayList<String> result = MapUtil.valuesOfKeys(map, iterator);
		assertEquals(new ArrayList<String>() {
			@Serial
			private static final long serialVersionUID = 7218152799308667271L;

			{
				add("1");
				add("2");
			}
		}, result);
	}

	@Test
	public void valuesOfKeysKeysNotInMapReturnsNulls() {
		final Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		final Iterator<String> iterator = new ArrayList<String>() {
			@Serial
			private static final long serialVersionUID = -5479427021989481058L;

			{
				add("d");
				add("e");
			}
		}.iterator();
		final ArrayList<String> result = MapUtil.valuesOfKeys(map, iterator);
		assertEquals(new ArrayList<String>() {
			@Serial
			private static final long serialVersionUID = 4390715387901549136L;

			{
				add(null);
				add(null);
			}
		}, result);
	}

	@Test
	public void valuesOfKeysMixedKeysReturnsMixedValues() {
		final Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		final Iterator<String> iterator = new ArrayList<String>() {
			@Serial
			private static final long serialVersionUID = 8510595063492828968L;

			{
				add("a");
				add("d");
				add("b");
			}
		}.iterator();
		final ArrayList<String> result = MapUtil.valuesOfKeys(map, iterator);
		assertEquals(new ArrayList<String>() {
			@Serial
			private static final long serialVersionUID = 6383576410597048337L;

			{
				add("1");
				add(null);
				add("2");
			}
		}, result);
	}

	//--------clear
	@Test
	public void clearNoMapsProvidedNoAction() {
		MapUtil.clear();
		// 预期没有异常发生，且没有Map被处理
	}

	@Test
	public void clearEmptyMapNoChange() {
		final Map<String, String> map = new HashMap<>();
		MapUtil.clear(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void clearNonEmptyMapClearsMap() {
		final Map<String, String> map = new HashMap<>();
		map.put("key", "value");
		MapUtil.clear(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void clearMultipleMapsClearsNonEmptyMaps() {
		final Map<String, String> map1 = new HashMap<>();
		map1.put("key1", "value1");

		final Map<String, String> map2 = new HashMap<>();
		map2.put("key2", "value2");

		final Map<String, String> map3 = new HashMap<>();

		MapUtil.clear(map1, map2, map3);

		assertTrue(map1.isEmpty());
		assertTrue(map2.isEmpty());
		assertTrue(map3.isEmpty());
	}

	@Test
	public void clearMixedMapsClearsNonEmptyMaps() {
		final Map<String, String> map = new HashMap<>();
		map.put("key", "value");

		final Map<String, String> emptyMap = new HashMap<>();

		MapUtil.clear(map, emptyMap);

		assertTrue(map.isEmpty());
		assertTrue(emptyMap.isEmpty());
	}

	//-----empty

	@Test
	public void emptyNoParametersReturnsEmptyMap() {
		final Map<String, String> emptyMap = MapUtil.empty();
		assertTrue(emptyMap.isEmpty(), "The map should be empty.");
		assertSame(Collections.emptyMap(), emptyMap, "The map should be the same instance as Collections.emptyMap().");
	}

	@Test
	public void emptyNullMapClassReturnsEmptyMap() {
		final Map<String, String> emptyMap = MapUtil.empty(null);
		assertTrue(emptyMap.isEmpty(), "The map should be empty.");
		assertSame(Collections.emptyMap(), emptyMap, "The map should be the same instance as Collections.emptyMap().");
	}

	@Test
	public void emptyNavigableMapClassReturnsEmptyNavigableMap() {
		final Map<?, ?> map = MapUtil.empty(NavigableMap.class);
		assertTrue(map.isEmpty());
		assertInstanceOf(NavigableMap.class, map);
	}

	@Test
	public void emptySortedMapClassReturnsEmptySortedMap() {
		final Map<?, ?> map = MapUtil.empty(SortedMap.class);
		assertTrue(map.isEmpty());
		assertInstanceOf(SortedMap.class, map);
	}

	@Test
	public void emptyMapClassReturnsEmptyMap() {
		final Map<?, ?> map = MapUtil.empty(Map.class);
		assertTrue(map.isEmpty());
	}

	@Test
	public void emptyUnsupportedMapClassThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () ->
			MapUtil.empty(TreeMap.class));
	}

	//--------removeNullValue
	@SuppressWarnings("ConstantValue")
	@Test
	public void removeNullValueNullMapReturnsNull() {
		final Map<String, String> result = MapUtil.removeNullValue(null);
		assertNull(result);
	}

	@Test
	public void removeNullValueEmptyMapReturnsEmptyMap() {
		final Map<String, String> map = new HashMap<>();
		final Map<String, String> result = MapUtil.removeNullValue(map);
		assertEquals(0, result.size());
	}

	@Test
	public void removeNullValueNoNullValuesReturnsSameMap() {
		final Map<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");

		final Map<String, String> result = MapUtil.removeNullValue(map);

		assertEquals(2, result.size());
		assertEquals("value1", result.get("key1"));
		assertEquals("value2", result.get("key2"));
	}

	@Test
	public void removeNullValueWithNullValuesRemovesNullEntries() {
		final Map<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", null);
		map.put("key3", "value3");

		final Map<String, String> result = MapUtil.removeNullValue(map);

		assertEquals(2, result.size());
		assertEquals("value1", result.get("key1"));
		assertEquals("value3", result.get("key3"));
		assertNull(result.get("key2"));
	}

	@Test
	public void removeNullValueAllNullValuesReturnsEmptyMap() {
		final Map<String, String> map = new HashMap<>();
		map.put("key1", null);
		map.put("key2", null);

		final Map<String, String> result = MapUtil.removeNullValue(map);

		assertEquals(0, result.size());
	}


	//------getQuietly
	@Test
	public void getQuietlyMapIsNullReturnsDefaultValue() {
		String result = MapUtil.getQuietly(null, "key1", new TypeReference<>() {
		}, "default");
		assertEquals("default", result);
		result = MapUtil.getQuietly(null, "key1", String.class, "default");
		assertEquals("default", result);
	}

	@Test
	public void getQuietlyKeyExistsReturnsConvertedValue() {
		final Map<String, Object> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", 123);
		final String result = MapUtil.getQuietly(map, "key1", new TypeReference<>() {
		}, "default");
		assertEquals("value1", result);
	}

	@Test
	public void getQuietlyKeyDoesNotExistReturnsDefaultValue() {
		final Map<String, Object> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", 123);
		final String result = MapUtil.getQuietly(map, "key3", new TypeReference<>() {
		}, "default");
		assertEquals("default", result);
	}

	@Test
	public void getQuietlyConversionFailsReturnsDefaultValue() {
		final Map<String, Object> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", 123);
		final Integer result = MapUtil.getQuietly(map, "key1", new TypeReference<>() {
		}, 0);
		assertEquals(0, result);
	}

	@Test
	public void getQuietlyKeyExistsWithCorrectTypeReturnsValue() {
		final Map<String, Object> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", 123);
		final Integer result = MapUtil.getQuietly(map, "key2", new TypeReference<>() {
		}, 0);
		assertEquals(123, result);
	}

	@Test
	public void getQuietlyKeyExistsWithNullValueReturnsDefaultValue() {
		final Map<String, Object> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", 123);
		map.put("key3", null);
		final String result = MapUtil.getQuietly(map, "key3", new TypeReference<>() {
		}, "default");
		assertEquals("default", result);
	}

	@Test
	public void getMapIsNullReturnsDefaultValue() {
		assertNull(MapUtil.get(null, "age", String.class));
	}

	@Test
	public void getKeyExistsReturnsConvertedValue() {
		final Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals("18", MapUtil.get(map, "age", String.class));
	}

	@Test
	public void getKeyDoesNotExistReturnsDefaultValue() {
		final Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals("default", MapUtil.get(map, "nonexistent", String.class, "default"));
	}

	@Test
	public void getTypeConversionFailsReturnsDefaultValue() {
		final Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals(18, MapUtil.get(map, "age", Integer.class, 0));
	}

	@Test
	public void getQuietlyTypeConversionFailsReturnsDefaultValue() {
		final Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals(0, MapUtil.getQuietly(map, "name", Integer.class, 0));
	}

	@Test
	public void getTypeReferenceReturnsConvertedValue() {
		final Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals("18", MapUtil.get(map, "age", new TypeReference<String>() {
		}));
	}

	@Test
	public void getTypeReferenceWithDefaultValueReturnsConvertedValue() {
		final Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals("18", MapUtil.get(map, "age", new TypeReference<>() {
		}, "default"));
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void getTypeReferenceWithDefaultValueTypeConversionFailsReturnsDefaultValue() {
		Map<String, String> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals(18, MapUtil.get(map, "age", new TypeReference<>() {
		}, 0));

		map = null;
		assertEquals(0, MapUtil.get(map, "age", new TypeReference<>() {
		}, 0));
	}
}
