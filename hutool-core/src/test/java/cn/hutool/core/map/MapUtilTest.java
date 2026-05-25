package cn.hutool.core.map;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MapUtilTest {

	enum PeopleEnum {GIRL, BOY, CHILD}

	@Data
	@Builder
	public static class User {
		private Long id;
		private String name;
	}

	@Data
	@Builder
	public static class Group {
		private Long id;
		private List<User> users;
	}

	@Data
	@Builder
	public static class UserGroup {
		private Long userId;
		private Long groupId;
	}


	@Test
	public void filterTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> map2 = MapUtil.filter(map, t -> Convert.toInt(t.getValue()) % 2 == 0);

		assertEquals(2, map2.size());

		assertEquals("2", map2.get("b"));
		assertEquals("4", map2.get("d"));
	}

	@Test
	public void mapTest() {
		// Add test like a foreigner
		final Map<Integer, String> adjectivesMap = MapUtil.<Integer, String>builder()
			.put(0, "lovely")
			.put(1, "friendly")
			.put(2, "happily")
			.build();

		final Map<Integer, String> resultMap = MapUtil.map(adjectivesMap, (k, v) -> v + " " + PeopleEnum.values()[k].name().toLowerCase());

		assertEquals("lovely girl", resultMap.get(0));
		assertEquals("friendly boy", resultMap.get(1));
		assertEquals("happily child", resultMap.get(2));

		// 下单用户，Queue表示正在 .排队. 抢我抢不到的二次元周边！
		final Queue<String> customers = new ArrayDeque<>(Arrays.asList("刑部尚书手工耿", "木瓜大盗大漠叔", "竹鼠发烧找华农", "朴实无华朱一旦"));
		// 分组
		final List<Group> groups = Stream.iterate(0L, i -> ++i).limit(4).map(i -> Group.builder().id(i).build()).collect(Collectors.toList());
		// 如你所见，它是一个map，key由用户id，value由用户组成
		final Map<Long, User> idUserMap = Stream.iterate(0L, i -> ++i).limit(4).map(i -> User.builder().id(i).name(customers.poll()).build()).collect(Collectors.toMap(User::getId, Function.identity()));
		// 如你所见，它是一个map，key由分组id，value由用户ids组成，典型的多对多关系
		final Map<Long, List<Long>> groupIdUserIdsMap = groups.stream().flatMap(group -> idUserMap.keySet().stream().map(userId -> UserGroup.builder().groupId(group.getId()).userId(userId).build()))
			.collect(Collectors.groupingBy(UserGroup::getGroupId, Collectors.mapping(UserGroup::getUserId, Collectors.toList())));

		// 神奇的魔法发生了， 分组id和用户ids组成的map，竟然变成了订单编号和用户实体集合组成的map
		final Map<Long, List<User>> groupIdUserMap = MapUtil.map(groupIdUserIdsMap, (groupId, userIds) -> userIds.stream().map(idUserMap::get).collect(Collectors.toList()));

		// 然后你就可以拿着这个map，去封装groups，使其能够在订单数据带出客户信息啦
		groups.forEach(group -> Opt.ofNullable(group.getId()).map(groupIdUserMap::get).ifPresent(group::setUsers));

		// 下面是测试报告
		groups.forEach(group -> {
			final List<User> users = group.getUsers();
			assertEquals("刑部尚书手工耿", users.get(0).getName());
			assertEquals("木瓜大盗大漠叔", users.get(1).getName());
			assertEquals("竹鼠发烧找华农", users.get(2).getName());
			assertEquals("朴实无华朱一旦", users.get(3).getName());
		});
		// 对null友好
		MapUtil.map(MapUtil.of(0, 0), (k, v) -> null).forEach((k, v) -> assertNull(v));
	}

	@Test
	public void filterMapWrapperTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> camelCaseMap = MapUtil.toCamelCaseMap(map);

		final Map<String, String> map2 = MapUtil.filter(camelCaseMap, t -> Convert.toInt(t.getValue()) % 2 == 0);

		assertEquals(2, map2.size());

		assertEquals("2", map2.get("b"));
		assertEquals("4", map2.get("d"));
	}

	@Test
	public void filterContainsTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("abc", "1");
		map.put("bcd", "2");
		map.put("def", "3");
		map.put("fgh", "4");

		final Map<String, String> map2 = MapUtil.filter(map, t -> StrUtil.contains(t.getKey(), "bc"));
		assertEquals(2, map2.size());
		assertEquals("1", map2.get("abc"));
		assertEquals("2", map2.get("bcd"));
	}

	@Test
	public void editTest() {
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

		assertEquals(4, map2.size());

		assertEquals("10", map2.get("a"));
		assertEquals("20", map2.get("b"));
		assertEquals("30", map2.get("c"));
		assertEquals("40", map2.get("d"));
	}

	@Test
	public void reverseTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Map<String, String> map2 = MapUtil.reverse(map);

		assertEquals("a", map2.get("1"));
		assertEquals("b", map2.get("2"));
		assertEquals("c", map2.get("3"));
		assertEquals("d", map2.get("4"));
	}

	@Test
	public void toObjectArrayTest() {
		final Map<String, String> map = MapUtil.newHashMap(true);
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		final Object[][] objectArray = MapUtil.toObjectArray(map);
		assertEquals("a", objectArray[0][0]);
		assertEquals("1", objectArray[0][1]);
		assertEquals("b", objectArray[1][0]);
		assertEquals("2", objectArray[1][1]);
		assertEquals("c", objectArray[2][0]);
		assertEquals("3", objectArray[2][1]);
		assertEquals("d", objectArray[3][0]);
		assertEquals("4", objectArray[3][1]);
	}

	@Test
	public void sortJoinTest() {
		final Map<String, String> build = MapUtil.builder(new HashMap<String, String>())
			.put("key1", "value1")
			.put("key3", "value3")
			.put("key2", "value2").build();

		final String join1 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false);
		assertEquals("key1value1key2value2key3value3", join1);

		final String join2 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false, "123");
		assertEquals("key1value1key2value2key3value3123", join2);

		final String join3 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false, "123", "abc");
		assertEquals("key1value1key2value2key3value3123abc", join3);
	}

	@Test
	public void ofEntriesTest() {
		final Map<String, Integer> map = MapUtil.ofEntries(MapUtil.entry("a", 1), MapUtil.entry("b", 2));
		assertEquals(2, map.size());

		assertEquals(Integer.valueOf(1), map.get("a"));
		assertEquals(Integer.valueOf(2), map.get("b"));
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
		Map<String, String> map = new HashMap<>();
		Map<String, String> result = MapUtil.renameKey(map, "oldKey", "newKey");
		assertTrue(result.isEmpty());
	}

	@Test
	public void renameKeyOldKeyNotPresentNoChange() {
		Map<String, String> map = new HashMap<>();
		map.put("anotherKey", "value");
		Map<String, String> result = MapUtil.renameKey(map, "oldKey", "newKey");
		assertEquals(1, result.size());
		assertEquals("value", result.get("anotherKey"));
	}

	@Test
	public void renameKeyOldKeyPresentNewKeyNotPresentKeyRenamed() {
		Map<String, String> map = new HashMap<>();
		map.put("oldKey", "value");
		Map<String, String> result = MapUtil.renameKey(map, "oldKey", "newKey");
		assertEquals(1, result.size());
		assertEquals("value", result.get("newKey"));
	}

	@Test
	public void renameKeyNewKeyPresentThrowsException() {
		Map<String, String> map = new HashMap<>();
		map.put("oldKey", "value");
		map.put("newKey", "existingValue");
		assertThrows(IllegalArgumentException.class, () -> {
			MapUtil.renameKey(map, "oldKey", "newKey");
		});
	}

	@Test
	public void issue3162Test() {
		final Map<String, Object> map = new HashMap<String, Object>() {
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
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		assertThrows(IllegalArgumentException.class, () -> MapUtil.partition(map, 0));
	}

	@Test
	public void partitionSizeNegativeThrowsException() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		assertThrows(IllegalArgumentException.class, () -> MapUtil.partition(map, -1));
	}

	@Test
	public void partitionEmptyMapReturnsEmptyList() {
		Map<String, String> map = new HashMap<>();
		List<Map<String, String>> result = MapUtil.partition(map, 2);
		assertTrue(result.isEmpty());
	}

	@Test
	public void partitionMapSizeMultipleOfSizePartitionsCorrectly() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		List<Map<String, String>> result = MapUtil.partition(map, 2);

		assertEquals(2, result.size());
		assertEquals(2, result.get(0).size());
		assertEquals(2, result.get(1).size());
	}

	@Test
	public void partitionMapSizeNotMultipleOfSizePartitionsCorrectly() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");
		map.put("e", "5");

		List<Map<String, String>> result = MapUtil.partition(map, 2);

		assertEquals(3, result.size());
		assertEquals(2, result.get(0).size());
		assertEquals(2, result.get(1).size());
		assertEquals(1, result.get(2).size());
	}

	@Test
	public void partitionGeneralCasePartitionsCorrectly() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");
		map.put("e", "5");
		map.put("f", "6");

		List<Map<String, String>> result = MapUtil.partition(map, 3);

		assertEquals(2, result.size());
		assertEquals(3, result.get(0).size());
		assertEquals(3, result.get(1).size());
	}


	// ---------MapUtil.computeIfAbsentForJdk8
	@Test
	public void computeIfAbsentForJdk8KeyExistsReturnsExistingValue() {
		Map<String, Integer> map = new HashMap<>();
		map.put("key", 10);
		Integer result = MapUtil.computeIfAbsentForJdk8(map, "key", k -> 20);
		assertEquals(10, result);
	}

	@Test
	public void computeIfAbsentForJdk8KeyDoesNotExistComputesAndInsertsValue() {
		Map<String, Integer> map = new HashMap<>();
		Integer result = MapUtil.computeIfAbsentForJdk8(map, "key", k -> 20);
		assertEquals(20, result);
		assertEquals(20, map.get("key"));
	}

	@Test
	public void computeIfAbsentForJdk8ConcurrentInsertReturnsOldValue() {
		ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
		concurrentMap.put("key", 30);
		AtomicInteger counter = new AtomicInteger(0);

		// 模拟并发插入
		concurrentMap.computeIfAbsent("key", k -> {
			counter.incrementAndGet();
			return 40;
		});

		Integer result = MapUtil.computeIfAbsentForJdk8(concurrentMap, "key", k -> 50);
		assertEquals(30, result);
		assertEquals(30, concurrentMap.get("key"));
		assertEquals(0, counter.get());
	}

	@Test
	public void computeIfAbsentForJdk8NullValueComputesAndInsertsValue() {
		Map<String, Integer> map = new HashMap<>();
		map.put("key", null);
		Integer result = MapUtil.computeIfAbsentForJdk8(map, "key", k -> 20);
		assertEquals(20, result);
		assertEquals(20, map.get("key"));
	}

	//--------MapUtil.computeIfAbsent
	@Test
	public void computeIfAbsentKeyExistsReturnsExistingValue() {
		Map<String, Integer> map = new HashMap<>();
		map.put("key", 10);
		Integer result = MapUtil.computeIfAbsent(map, "key", k -> 20);
		assertEquals(10, result);
	}

	@Test
	public void computeIfAbsentKeyDoesNotExistComputesAndInsertsValue() {
		Map<String, Integer> map = new HashMap<>();
		Integer result = MapUtil.computeIfAbsent(map, "key", k -> 20);
		assertEquals(20, result);
		assertEquals(20, map.get("key"));
	}

	@Test
	public void computeIfAbsentConcurrentInsertReturnsOldValue() {
		ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
		concurrentMap.put("key", 30);
		AtomicInteger counter = new AtomicInteger(0);

		// 模拟并发插入
		concurrentMap.computeIfAbsent("key", k -> {
			counter.incrementAndGet();
			return 40;
		});

		Integer result = MapUtil.computeIfAbsent(concurrentMap, "key", k -> 50);
		assertEquals(30, result);
		assertEquals(30, concurrentMap.get("key"));
		assertEquals(0, counter.get());
	}

	@Test
	public void computeIfAbsentNullValueComputesAndInsertsValue() {
		Map<String, Integer> map = new HashMap<>();
		map.put("key", null);
		Integer result = MapUtil.computeIfAbsent(map, "key", k -> 20);
		assertEquals(20, result);
		assertEquals(20, map.get("key"));
	}

	@Test
	public void computeIfAbsentEmptyMapInsertsValue() {
		Map<String, Integer> map = new HashMap<>();
		Integer result = MapUtil.computeIfAbsent(map, "newKey", k -> 100);
		assertEquals(100, result);
		assertEquals(100, map.get("newKey"));
	}

	@Test
	public void computeIfAbsentJdk8KeyExistsReturnsExistingValue() {
		Map<String, Integer> map = new HashMap<>();
		// 假设JdkUtil.ISJDK8为true
		map.put("key", 10);
		Integer result = MapUtil.computeIfAbsent(map, "key", k -> 20);
		assertEquals(10, result);
	}

	@Test
	public void computeIfAbsentJdk8KeyDoesNotExistComputesAndInsertsValue() {
		Map<String, Integer> map = new HashMap<>();
		// 假设JdkUtil.ISJDK8为true
		Integer result = MapUtil.computeIfAbsent(map, "key", k -> 20);
		assertEquals(20, result);
		assertEquals(20, map.get("key"));
	}


	//----------valuesOfKeys
	@Test
	public void valuesOfKeysEmptyIteratorReturnsEmptyList() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		Iterator<String> emptyIterator = Collections.emptyIterator();
		ArrayList<String> result = MapUtil.valuesOfKeys(map, emptyIterator);
		assertEquals(new ArrayList<String>(), result);
	}

	@Test
	public void valuesOfKeysNonEmptyIteratorReturnsValuesList() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		Iterator<String> iterator = new ArrayList<String>() {
			private static final long serialVersionUID = -4593258366224032110L;

			{
				add("a");
				add("b");
			}
		}.iterator();
		ArrayList<String> result = MapUtil.valuesOfKeys(map, iterator);
		assertEquals(new ArrayList<String>() {
			private static final long serialVersionUID = 7218152799308667271L;

			{
				add("1");
				add("2");
			}
		}, result);
	}

	@Test
	public void valuesOfKeysKeysNotInMapReturnsNulls() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		Iterator<String> iterator = new ArrayList<String>() {
			private static final long serialVersionUID = -5479427021989481058L;

			{
				add("d");
				add("e");
			}
		}.iterator();
		ArrayList<String> result = MapUtil.valuesOfKeys(map, iterator);
		assertEquals(new ArrayList<String>() {
			private static final long serialVersionUID = 4390715387901549136L;

			{
				add(null);
				add(null);
			}
		}, result);
	}

	@Test
	public void valuesOfKeysMixedKeysReturnsMixedValues() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		Iterator<String> iterator = new ArrayList<String>() {
			private static final long serialVersionUID = 8510595063492828968L;

			{
				add("a");
				add("d");
				add("b");
			}
		}.iterator();
		ArrayList<String> result = MapUtil.valuesOfKeys(map, iterator);
		assertEquals(new ArrayList<String>() {
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
		Map<String, String> map = new HashMap<>();
		MapUtil.clear(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void clearNonEmptyMapClearsMap() {
		Map<String, String> map = new HashMap<>();
		map.put("key", "value");
		MapUtil.clear(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void clearMultipleMapsClearsNonEmptyMaps() {
		Map<String, String> map1 = new HashMap<>();
		map1.put("key1", "value1");

		Map<String, String> map2 = new HashMap<>();
		map2.put("key2", "value2");

		Map<String, String> map3 = new HashMap<>();

		MapUtil.clear(map1, map2, map3);

		assertTrue(map1.isEmpty());
		assertTrue(map2.isEmpty());
		assertTrue(map3.isEmpty());
	}

	@Test
	public void clearMixedMapsClearsNonEmptyMaps() {
		Map<String, String> map = new HashMap<>();
		map.put("key", "value");

		Map<String, String> emptyMap = new HashMap<>();

		MapUtil.clear(map, emptyMap);

		assertTrue(map.isEmpty());
		assertTrue(emptyMap.isEmpty());
	}

	//-----empty

	@Test
	public void emptyNoParametersReturnsEmptyMap() {
		Map<String, String> emptyMap = MapUtil.empty();
		assertTrue(emptyMap.isEmpty(), "The map should be empty.");
		assertSame(Collections.emptyMap(), emptyMap, "The map should be the same instance as Collections.emptyMap().");
	}

	@Test
	public void emptyNullMapClassReturnsEmptyMap() {
		Map<String, String> emptyMap = MapUtil.empty(null);
		assertTrue(emptyMap.isEmpty(), "The map should be empty.");
		assertSame(Collections.emptyMap(), emptyMap, "The map should be the same instance as Collections.emptyMap().");
	}

	@Test
	public void emptyNavigableMapClassReturnsEmptyNavigableMap() {
		Map<?, ?> map = MapUtil.empty(NavigableMap.class);
		assertTrue(map.isEmpty());
		assertInstanceOf(NavigableMap.class, map);
	}

	@Test
	public void emptySortedMapClassReturnsEmptySortedMap() {
		Map<?, ?> map = MapUtil.empty(SortedMap.class);
		assertTrue(map.isEmpty());
		assertInstanceOf(SortedMap.class, map);
	}

	@Test
	public void emptyMapClassReturnsEmptyMap() {
		Map<?, ?> map = MapUtil.empty(Map.class);
		assertTrue(map.isEmpty());
	}

	@Test
	public void emptyUnsupportedMapClassThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
			MapUtil.empty(TreeMap.class);
		});
	}

	//--------removeNullValue
	@Test
	public void removeNullValueNullMapReturnsNull() {
		Map<String, String> result = MapUtil.removeNullValue(null);
		assertNull(result);
	}

	@Test
	public void removeNullValueEmptyMapReturnsEmptyMap() {
		Map<String, String> map = new HashMap<>();
		Map<String, String> result = MapUtil.removeNullValue(map);
		assertEquals(0, result.size());
	}

	@Test
	public void removeNullValueNoNullValuesReturnsSameMap() {
		Map<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");

		Map<String, String> result = MapUtil.removeNullValue(map);

		assertEquals(2, result.size());
		assertEquals("value1", result.get("key1"));
		assertEquals("value2", result.get("key2"));
	}

	@Test
	public void removeNullValueWithNullValuesRemovesNullEntries() {
		Map<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", null);
		map.put("key3", "value3");

		Map<String, String> result = MapUtil.removeNullValue(map);

		assertEquals(2, result.size());
		assertEquals("value1", result.get("key1"));
		assertEquals("value3", result.get("key3"));
		assertNull(result.get("key2"));
	}

	@Test
	public void removeNullValueAllNullValuesReturnsEmptyMap() {
		Map<String, String> map = new HashMap<>();
		map.put("key1", null);
		map.put("key2", null);

		Map<String, String> result = MapUtil.removeNullValue(map);

		assertEquals(0, result.size());
	}


	//------getQuietly
	@Test
	public void getQuietlyMapIsNullReturnsDefaultValue() {
		String result = MapUtil.getQuietly(null, "key1", new TypeReference<String>() {
		}, "default");
		assertEquals("default", result);
		result = MapUtil.getQuietly(null, "key1", String.class, "default");
		assertEquals("default", result);
	}

	@Test
	public void getQuietlyKeyExistsReturnsConvertedValue() {
		Map<String, Object> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", 123);
		String result = MapUtil.getQuietly(map, "key1", new TypeReference<String>() {
		}, "default");
		assertEquals("value1", result);
	}

	@Test
	public void getQuietlyKeyDoesNotExistReturnsDefaultValue() {
		Map<String, Object> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", 123);
		String result = MapUtil.getQuietly(map, "key3", new TypeReference<String>() {
		}, "default");
		assertEquals("default", result);
	}

	@Test
	public void getQuietlyConversionFailsReturnsDefaultValue() {
		Map<String, Object> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", 123);
		Integer result = MapUtil.getQuietly(map, "key1", new TypeReference<Integer>() {
		}, 0);
		assertEquals(0, result);
	}

	@Test
	public void getQuietlyKeyExistsWithCorrectTypeReturnsValue() {
		Map<String, Object> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", 123);
		Integer result = MapUtil.getQuietly(map, "key2", new TypeReference<Integer>() {
		}, 0);
		assertEquals(123, result);
	}

	@Test
	public void getQuietlyKeyExistsWithNullValueReturnsDefaultValue() {
		Map<String, Object> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", 123);
		map.put("key3", null);
		String result = MapUtil.getQuietly(map, "key3", new TypeReference<String>() {
		}, "default");
		assertEquals("default", result);
	}

	@Test
	public void getMapIsNullReturnsDefaultValue() {
		assertNull(MapUtil.get(null, "age", String.class));
	}

	@Test
	public void getKeyExistsReturnsConvertedValue() {
		Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals("18", MapUtil.get(map, "age", String.class));
	}

	@Test
	public void getKeyDoesNotExistReturnsDefaultValue() {
		Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals("default", MapUtil.get(map, "nonexistent", String.class, "default"));
	}

	@Test
	public void getTypeConversionFailsReturnsDefaultValue() {
		Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals(18, MapUtil.get(map, "age", Integer.class, 0));
	}

	@Test
	public void getQuietlyTypeConversionFailsReturnsDefaultValue() {
		Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals(0, MapUtil.getQuietly(map, "name", Integer.class, 0));
	}

	@Test
	public void getTypeReferenceReturnsConvertedValue() {
		Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals("18", MapUtil.get(map, "age", new TypeReference<String>() {
		}));
	}

	@Test
	public void getTypeReferenceWithDefaultValueReturnsConvertedValue() {
		Map<String, Object> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals("18", MapUtil.get(map, "age", new TypeReference<String>() {
		}, "default"));
	}

	@Test
	public void getTypeReferenceWithDefaultValueTypeConversionFailsReturnsDefaultValue() {
		Map<String, String> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "Hutool");
		assertEquals(18, MapUtil.get(map, "age", new TypeReference<Integer>() {
		}, 0));

		map = null;
		assertEquals(0, MapUtil.get(map, "age", new TypeReference<Integer>() {
		}, 0));
	}

	@Test
	public void flattenMapReturnsTest() {
		Map<String, String> clothes = new HashMap<>();
		clothes.put("clothesName", "ANTA");
		clothes.put("clothesPrice", "200");

		Map<String, Object> person = new HashMap<>();
		person.put("personName", "XXXX");
		person.put("clothes", clothes);

		Map<String, Object> map = new HashMap<>();
		map.put("home", "AAA");
		map.put("person", person);

		Map<String, Object> flattenMap = MapUtil.flatten(map);
		assertEquals("ANTA", MapUtil.get(flattenMap, "clothesName", new TypeReference<String>() {
		}));
		assertEquals("200", MapUtil.get(flattenMap, "clothesPrice", new TypeReference<String>() {
		}));
		assertEquals("XXXX", MapUtil.get(flattenMap, "personName", new TypeReference<String>() {
		}));
		assertEquals("AAA", MapUtil.get(flattenMap, "home", new TypeReference<String>() {
		}));
	}
}
