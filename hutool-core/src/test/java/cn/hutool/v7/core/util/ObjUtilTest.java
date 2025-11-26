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

package cn.hutool.v7.core.util;

import cn.hutool.v7.core.collection.ListUtil;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import cn.hutool.v7.core.convert.ConvertUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test for {@link ObjUtil}
 */
public class ObjUtilTest {

	@SuppressWarnings("ConstantValue")
	@Test
	public void equalsTest() {
		Object a = null;
		Object b = null;
		assertTrue(ObjUtil.equals(a, b));

		a = new BigDecimal("1.1");
		b = new BigDecimal("1.10");
		assertTrue(ObjUtil.equals(a, b));

		a = 127;
		b = 127;
		assertTrue(ObjUtil.equals(a, b));

		a = 128;
		b = 128;
		assertTrue(ObjUtil.equals(a, b));

		a = LocalDateTime.of(2022, 5, 29, 22, 11);
		b = LocalDateTime.of(2022, 5, 29, 22, 11);
		assertTrue(ObjUtil.equals(a, b));

		a = 1;
		b = 1.0;
		assertFalse(ObjUtil.equals(a, b));
	}

	@Test
	public void lengthTest(){
		final int[] array = new int[]{1,2,3,4,5};
		int length = ObjUtil.length(array);
		assertEquals(5, length);

		final Map<String, String> map = new HashMap<>();
		map.put("a", "a1");
		map.put("b", "b1");
		map.put("c", "c1");
		length = ObjUtil.length(map);
		assertEquals(3, length);

		final Iterable<Integer> list = ListUtil.of(1, 2, 3);
		assertEquals(3, ObjUtil.length(list));
		assertEquals(3, ObjUtil.length(Arrays.asList(1, 2, 3).iterator()));
	}

	@Test
	public void containsTest(){
		assertTrue(ObjUtil.contains(new int[]{1,2,3,4,5}, 1));
		assertFalse(ObjUtil.contains(null, 1));
		assertTrue(ObjUtil.contains("123", "3"));
		final Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		assertTrue(ObjUtil.contains(map, 1));
		assertTrue(ObjUtil.contains(Arrays.asList(1, 2, 3).iterator(), 2));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isNullTest() {
		assertTrue(ObjUtil.isNull(null));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isNotNullTest() {
		assertFalse(ObjUtil.isNotNull(null));
	}

	@Test
	public void isEmptyTest() {
		assertTrue(ObjUtil.isEmpty(null));
		assertTrue(ObjUtil.isEmpty(new int[0]));
		assertTrue(ObjUtil.isEmpty(""));
		assertTrue(ObjUtil.isEmpty(Collections.emptyList()));
		assertTrue(ObjUtil.isEmpty(Collections.emptyMap()));
		assertTrue(ObjUtil.isEmpty(Collections.emptyIterator()));
	}

	@Test
	public void isNotEmptyTest() {
		assertFalse(ObjUtil.isNotEmpty(null));
		assertFalse(ObjUtil.isNotEmpty(new int[0]));
		assertFalse(ObjUtil.isNotEmpty(""));
		assertFalse(ObjUtil.isNotEmpty(Collections.emptyList()));
		assertFalse(ObjUtil.isNotEmpty(Collections.emptyMap()));
		assertFalse(ObjUtil.isNotEmpty(Collections.emptyIterator()));
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void defaultIfNullTest() {
		final Object val1 = new Object();
		final Object val2 = new Object();

		Assertions.assertSame(val1, ObjUtil.defaultIfNull(val1, () -> val2));
		Assertions.assertSame(val2, ObjUtil.defaultIfNull(null, () -> val2));

		Assertions.assertSame(val1, ObjUtil.defaultIfNull(val1, val2));
		Assertions.assertSame(val2, ObjUtil.defaultIfNull(null, val2));

		Assertions.assertSame(val1, ObjUtil.defaultIfNull(val1, Function.identity(), () -> val2));
		Assertions.assertSame(val2, ObjUtil.defaultIfNull(null, Function.identity(), () -> val2));

		Assertions.assertSame(val1, ObjUtil.defaultIfNull(val1, Function.identity(), val2));
		Assertions.assertSame(val2, ObjUtil.defaultIfNull(null, Function.identity(), val2));

		final SerializableBean obj = new SerializableBean(null);
		final SerializableBean objNull = null;
		final String result3 = ObjUtil.defaultIfNull(obj, Object::toString, "fail");
		Assertions.assertNotNull(result3);

		final String result4 = ObjUtil.defaultIfNull(objNull, Object::toString, () -> "fail");
		Assertions.assertNotNull(result4);
	}

	@Test
	void cloneListTest() {
		final ArrayList<Integer> list = ListUtil.of(1, 2);
		final ArrayList<Integer> clone = ObjUtil.clone(list);
		assertEquals(list, clone);
	}

	@Test
	public void cloneTest() {
		Assertions.assertNull(ObjUtil.clone(null));

		final CloneableBean cloneableBean1 = new CloneableBean(1);
		final CloneableBean cloneableBean2 = ObjUtil.clone(cloneableBean1);
		assertEquals(cloneableBean1, cloneableBean2);

		final SerializableBean serializableBean1 = new SerializableBean(2);
		final SerializableBean serializableBean2 = ObjUtil.clone(serializableBean1);
		assertEquals(serializableBean1, serializableBean2);

		final Bean bean1 = new Bean(3);
		Assertions.assertNull(ObjUtil.clone(bean1));
	}

	@Test
	public void cloneIfPossibleTest() {
		Assertions.assertNull(ObjUtil.clone(null));

		final CloneableBean cloneableBean1 = new CloneableBean(1);
		assertEquals(cloneableBean1, ObjUtil.cloneIfPossible(cloneableBean1));

		final SerializableBean serializableBean1 = new SerializableBean(2);
		assertEquals(serializableBean1, ObjUtil.cloneIfPossible(serializableBean1));

		final Bean bean1 = new Bean(3);
		Assertions.assertSame(bean1, ObjUtil.cloneIfPossible(bean1));

		final ExceptionCloneableBean exceptionBean1 = new ExceptionCloneableBean(3);
		Assertions.assertSame(exceptionBean1, ObjUtil.cloneIfPossible(exceptionBean1));
	}

	@Test
	public void cloneByStreamTest() {
		Assertions.assertNull(ObjUtil.cloneByStream(null));
		Assertions.assertNull(ObjUtil.cloneByStream(new CloneableBean(1)));
		final SerializableBean serializableBean1 = new SerializableBean(2);
		assertEquals(serializableBean1, ObjUtil.cloneByStream(serializableBean1));
		Assertions.assertNull(ObjUtil.cloneByStream(new Bean(1)));
	}

	@Test
	public void isBasicTypeTest(){
		final int a = 1;
		final boolean basicType = ObjUtil.isBasicType(a);
		assertTrue(basicType);
	}

	@Test
	public void isValidIfNumberTest() {
		assertTrue(ObjUtil.isValidIfNumber(null));
		assertFalse(ObjUtil.isValidIfNumber(Double.NEGATIVE_INFINITY));
		assertFalse(ObjUtil.isValidIfNumber(Double.NaN));
		assertTrue(ObjUtil.isValidIfNumber(Double.MIN_VALUE));
		assertFalse(ObjUtil.isValidIfNumber(Float.NEGATIVE_INFINITY));
		assertFalse(ObjUtil.isValidIfNumber(Float.NaN));
		assertTrue(ObjUtil.isValidIfNumber(Float.MIN_VALUE));
	}

	@Test
	public void getTypeArgumentTest() {
		final Bean bean = new Bean(1);
		assertEquals(Integer.class, ObjUtil.getTypeArgument(bean));
		assertEquals(String.class, ObjUtil.getTypeArgument(bean, 1));
	}

	@Test
	public void toStringTest() {
		assertEquals("null", ConvertUtil.toStrOrNullStr(null));
		assertEquals(Collections.emptyMap().toString(), ConvertUtil.toStrOrNullStr(Collections.emptyMap()));
		assertEquals("[1, 2]", Arrays.asList("1", "2").toString());
	}

	@Test
	public void testLengthConsumesIterator() {
		final List<String> list = Arrays.asList("a", "b", "c");
		final Iterator<String> iterator = list.iterator();
		// 迭代器第一次调用length
		final int length1 = ObjUtil.length(iterator);
		assertEquals(3, length1);
		// 迭代器第二次调用length - 迭代器已经被消耗，返回0
		final int length2 = ObjUtil.length(iterator);
		assertEquals(0, length2); // 但当前实现会重新遍历，但iterator已经没有元素了
		// 尝试使用迭代器 - 已经无法使用
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testLengthConsumesEnumeration() {
		final Vector<String> vector = new Vector<>(Arrays.asList("a", "b", "c"));
		final Enumeration<String> enumeration = vector.elements();
		// 第一次调用length
		final int length1 = ObjUtil.length(enumeration);
		assertEquals(3, length1);
		// 第二次调用length - 枚举已经被消耗
		final int length2 = ObjUtil.length(enumeration);
		assertEquals(0, length2);
		// 枚举已经无法使用
		assertFalse(enumeration.hasMoreElements());
	}

	@RequiredArgsConstructor
	@EqualsAndHashCode
	private static class ExceptionCloneableBean implements Cloneable {
		private final Integer id;
		@Override
		protected Object clone() throws CloneNotSupportedException {
			throw new CloneNotSupportedException("can not clone this object");
		}
	}

	@RequiredArgsConstructor
	@EqualsAndHashCode
	private static class CloneableBean implements Cloneable {
		private final Integer id;
		@Override
		protected Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
	}

	@RequiredArgsConstructor
	@EqualsAndHashCode
	private static class SerializableBean implements Serializable {
		@Serial
		private static final long serialVersionUID = -7759522980793544334L;
		private final Integer id;
	}

	@RequiredArgsConstructor
	@EqualsAndHashCode
	private static class Bean implements TypeArgument<Integer, String> {
		private final Integer id;
	}

	@SuppressWarnings("unused")
	private interface TypeArgument<A, B> {}

	@Test
	public void testContainsElementToStringReturnsNull() {
		final Object problematicElement = new CharSequence() {
			@Override
			public int length() {
				return 0;
			}

			@Override
			public char charAt(final int index) {
				return 0;
			}

			@SuppressWarnings("DataFlowIssue")
			@NotNull
			@Override
			public CharSequence subSequence(final int start, final int end) {
				return null;
			}

			@SuppressWarnings("NullableProblems")
			@Override
			public String toString() {
				return null;
			}
		};
		assertFalse(ObjUtil.contains("test", problematicElement)); //不会抛异常
	}

	@Test
	public void testContainsElementToStringInvalidSyntax() {
		//字符串包含自定义User对象不符合语义
		assertFalse(ObjUtil.contains("User[id=123]", new User(123)));
	}


	static class User{
		private int id;
		public User(final int id) {
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
		final StringBuilder stringBuilder = new StringBuilder("hello world");
		final StringBuffer stringBuffer = new StringBuffer("hello world");
		final String str = "hello world";
		assertTrue((ObjUtil.contains(stringBuilder, "world")));
		assertTrue(ObjUtil.contains(stringBuffer, "hello"));
		assertTrue(ObjUtil.contains(str, "hello"));
	}
}
