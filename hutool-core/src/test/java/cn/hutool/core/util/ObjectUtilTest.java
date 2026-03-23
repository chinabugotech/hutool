package cn.hutool.core.util;

import cn.hutool.core.clone.CloneSupport;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectUtilTest {

	@Test
	public void equalsTest() {
		Object a = null;
		Object b = null;
		assertTrue(ObjectUtil.equals(a, b));
	}

	@Test
	public void lengthTest() {
		int[] array = new int[]{1, 2, 3, 4, 5};
		int length = ObjectUtil.length(array);
		assertEquals(5, length);

		Map<String, String> map = new HashMap<>();
		map.put("a", "a1");
		map.put("b", "b1");
		map.put("c", "c1");
		length = ObjectUtil.length(map);
		assertEquals(3, length);
	}

	@Test
	public void containsTest() {
		int[] array = new int[]{1, 2, 3, 4, 5};

		final boolean contains = ObjectUtil.contains(array, 1);
		assertTrue(contains);
	}

	@Test
	public void cloneTest() {
		Obj obj = new Obj();
		Obj obj2 = ObjectUtil.clone(obj);
		assertEquals("OK", obj2.doSomeThing());
	}

	static class Obj extends CloneSupport<Obj> {
		public String doSomeThing() {
			return "OK";
		}
	}

	@Test
	public void toStringTest() {
		ArrayList<String> strings = CollUtil.newArrayList("1", "2");
		String result = ObjectUtil.toString(strings);
		assertEquals("[1, 2]", result);
	}

	@Test
	public void defaultIfNullTest() {
		final String nullValue = null;
		final String dateStr = "2020-10-23 15:12:30";
		Instant result1 = ObjectUtil.defaultIfNull(dateStr,
				(source) -> DateUtil.parse(source, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		assertNotNull(result1);
		Instant result2 = ObjectUtil.defaultIfNull(nullValue,
				(source) -> DateUtil.parse(source, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		assertNotNull(result2);

		Obj obj = new Obj();
		Obj objNull = null;
		String result3 = ObjectUtil.defaultIfNull(obj, (a) -> obj.doSomeThing(), "fail");
		assertNotNull(result3);

		String result4 = ObjectUtil.defaultIfNull(objNull, Obj::doSomeThing, "fail");
		assertNotNull(result4);
	}

	@Test
	public void defaultIfEmptyTest() {
		final String emptyValue = "";
		final String dateStr = "2020-10-23 15:12:30";
		Instant result1 = ObjectUtil.defaultIfEmpty(emptyValue,
				(source) -> DateUtil.parse(source, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		assertNotNull(result1);
		Instant result2 = ObjectUtil.defaultIfEmpty(dateStr,
				(source) -> DateUtil.parse(source, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		assertNotNull(result2);
	}

	@Test
	public void isBasicTypeTest() {
		int a = 1;
		final boolean basicType = ObjectUtil.isBasicType(a);
		assertTrue(basicType);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isNotNullTest() {
		String a = null;
		assertFalse(ObjectUtil.isNotNull(a));
	}

	@Test
	public void testLengthConsumesIterator() {
		List<String> list = Arrays.asList("a", "b", "c");
		Iterator<String> iterator = list.iterator();
		// 迭代器第一次调用length
		int length1 = ObjectUtil.length(iterator);
		assertEquals(3, length1);
		// 迭代器第二次调用length - 迭代器已经被消耗，返回0
		int length2 = ObjectUtil.length(iterator);
		assertEquals(0, length2); // 但当前实现会重新遍历，但iterator已经没有元素了
		// 尝试使用迭代器 - 已经无法使用
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testLengthConsumesEnumeration() {
		Vector<String> vector = new Vector<>(Arrays.asList("a", "b", "c"));
		Enumeration<String> enumeration = vector.elements();
		// 第一次调用length
		int length1 = ObjectUtil.length(enumeration);
		assertEquals(3, length1);
		// 第二次调用length - 枚举已经被消耗
		int length2 = ObjectUtil.length(enumeration);
		assertEquals(0, length2);
		// 枚举已经无法使用
		assertFalse(enumeration.hasMoreElements());
	}

	@Test
	public void testContainsElementToStringReturnsNull() {
		Object problematicElement = new Object() {
			@Override
			public String toString() {
				return null; // 返回 null 的 toString
			}
		};
		assertFalse(ObjectUtil.contains("test", problematicElement)); //不会抛异常
	}

	@Test
	public void testContainsElementToStringInvalidSyntax() {
		//字符串包含自定义User对象不符合语义
		assertFalse(ObjectUtil.contains("User[id=123]", new User(123)));
	}


	static class User{
		private int id;
		public User(int id) {
			this.id = id;
		}
		@Override
		public String toString() {
			return "User[" +
					"id=" + id +
					']';
		}
	}

	@Test
	public void testContainsCharSequenceSupported() {
		//contains方法支持String、StringBuilder、StringBuffer
		StringBuilder stringBuilder = new StringBuilder("hello world");
		StringBuffer stringBuffer = new StringBuffer("hello world");
		String str = "hello world";
		assertTrue((ObjectUtil.contains(stringBuilder, "world")));
		assertTrue(ObjectUtil.contains(stringBuffer, "hello"));
		assertTrue(ObjectUtil.contains(str, "hello"));
	}
}
