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

package cn.hutool.v7.core.convert;

import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.reflect.TypeReference;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * 转换为集合测试
 *
 * @author Looly
 *
 */
public class ConvertToCollectionTest {

	@Test
	public void toCollectionTest() {
		final Object[] a = {"a", "你", "好", "", 1};
		final List<?> list = (List<?>) ConvertUtil.convert(Collection.class, a);
		assertEquals("a", list.get(0));
		assertEquals("你", list.get(1));
		assertEquals("好", list.get(2));
		assertEquals("", list.get(3));
		assertEquals(1, list.get(4));
	}

	@Test
	public void toListTest() {
		final Object[] a = {"a", "你", "好", "", 1};
		final List<?> list = ConvertUtil.toList(a);
		assertEquals("a", list.get(0));
		assertEquals("你", list.get(1));
		assertEquals("好", list.get(2));
		assertEquals("", list.get(3));
		assertEquals(1, list.get(4));
	}

	@Test
	public void toListTest2() {
		final Object[] a = {"a", "你", "好", "", 1};
		final List<String> list = ConvertUtil.toList(String.class, a);
		assertEquals("a", list.get(0));
		assertEquals("你", list.get(1));
		assertEquals("好", list.get(2));
		assertEquals("", list.get(3));
		assertEquals("1", list.get(4));
	}

	@Test
	public void toListTest3() {
		final Object[] a = {"a", "你", "好", "", 1};
		final List<String> list = ConvertUtil.toList(String.class, a);
		assertEquals("a", list.get(0));
		assertEquals("你", list.get(1));
		assertEquals("好", list.get(2));
		assertEquals("", list.get(3));
		assertEquals("1", list.get(4));
	}

	// TODO 此测试在Linux下不一致
	@Test
	@Disabled
	public void toListTest4() {
		final Object[] a = {"a", "你", "好", "", 1};
		final List<String> list = ConvertUtil.convert(new TypeReference<>() {}, a);
		assertEquals("a", list.get(0));
		assertEquals("你", list.get(1));
		assertEquals("好", list.get(2));
		assertEquals("", list.get(3));
		assertEquals("1", list.get(4));
	}

	@Test
	public void strToListTest() {
		final String a = "a,你,好,123";
		final List<?> list = ConvertUtil.toList(a);
		assertEquals(4, list.size());
		assertEquals("a", list.get(0));
		assertEquals("你", list.get(1));
		assertEquals("好", list.get(2));
		assertEquals("123", list.get(3));

		final String b = "a";
		final List<?> list2 = ConvertUtil.toList(b);
		assertEquals(1, list2.size());
		assertEquals("a", list2.get(0));
	}

	@Test
	public void strToListTest2() {
		final String a = "a,你,好,123";
		final List<String> list = ConvertUtil.toList(String.class, a);
		assertEquals(4, list.size());
		assertEquals("a", list.get(0));
		assertEquals("你", list.get(1));
		assertEquals("好", list.get(2));
		assertEquals("123", list.get(3));
	}

	@Test
	public void numberToListTest() {
		final Integer i = 1;
		final ArrayList<?> list = ConvertUtil.convert(ArrayList.class, i);
		assertSame(i, list.get(0));

		final BigDecimal b = BigDecimal.ONE;
		final ArrayList<?> list2 = ConvertUtil.convert(ArrayList.class, b);
		assertEquals(b, list2.get(0));
	}

	@Test
	public void toLinkedListTest() {
		final Object[] a = {"a", "你", "好", "", 1};
		final List<?> list = ConvertUtil.convert(LinkedList.class, a);
		assertEquals("a", list.get(0));
		assertEquals("你", list.get(1));
		assertEquals("好", list.get(2));
		assertEquals("", list.get(3));
		assertEquals(1, list.get(4));
	}

	@Test
	public void toSetTest() {
		final Object[] a = {"a", "你", "好", "", 1};
		final LinkedHashSet<?> set = ConvertUtil.convert(LinkedHashSet.class, a);
		final ArrayList<?> list = ListUtil.of(set);
		assertEquals("a", list.get(0));
		assertEquals("你", list.get(1));
		assertEquals("好", list.get(2));
		assertEquals("", list.get(3));
		assertEquals(1, list.get(4));
	}
}
