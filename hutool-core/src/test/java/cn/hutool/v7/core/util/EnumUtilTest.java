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

package cn.hutool.v7.core.util;

import cn.hutool.v7.core.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * EnumUtil单元测试
 *
 * @author Looly
 *
 */
public class EnumUtilTest {

	@Test
	public void getNamesTest() {
		final List<String> names = EnumUtil.getNames(TestEnum.class);
		assertEquals(ListUtil.of("TEST1", "TEST2", "TEST3"), names);
	}

	@Test
	public void getFieldValuesTest() {
		final List<Object> types = EnumUtil.getFieldValues(TestEnum.class, "type");
		assertEquals(ListUtil.of("type1", "type2", "type3"), types);
	}

	@Test
	public void getFieldNamesTest() {
		final List<String> names = EnumUtil.getFieldNames(TestEnum.class);
		Assertions.assertTrue(names.contains("type"));
		Assertions.assertTrue(names.contains("name"));
	}

	@Test
	public void getByTest() {
		// 枚举中字段互相映射使用
		final TestEnum testEnum = EnumUtil.getBy(TestEnum::ordinal, 1);
		assertEquals("TEST2", testEnum.name());
	}

	@Test
	public void getFieldByTest() {
		// 枚举中字段互相映射使用
		final String type = EnumUtil.getFieldBy(TestEnum::getType, Enum::ordinal, 1);
		assertEquals("type2", type);

		final int ordinal = EnumUtil.getFieldBy(TestEnum::ordinal, Enum::ordinal, 1);
		assertEquals(1, ordinal);
	}

	@Test
	public void likeValueOfTest() {
		final TestEnum value = EnumUtil.likeValueOf(TestEnum.class, "type2");
		assertEquals(TestEnum.TEST2, value);
	}

	@Test
	public void getEnumMapTest() {
		final Map<String,TestEnum> enumMap = EnumUtil.getEnumMap(TestEnum.class);
		assertEquals(TestEnum.TEST1, enumMap.get("TEST1"));
	}

	@Test
	public void getNameFieldMapTest() {
		final Map<String, Object> enumMap = EnumUtil.getNameFieldMap(TestEnum.class, "type");
		assert enumMap != null;
		assertEquals("type1", enumMap.get("TEST1"));
	}

	public enum TestEnum{
		TEST1("type1"), TEST2("type2"), TEST3("type3");

		TestEnum(final String type) {
			this.type = type;
		}

		private final String type;
		@SuppressWarnings("unused")
		private String name;

		public String getType() {
			return this.type;
		}

		public String getName() {
			return this.name;
		}
	}

	/**
	 * 测试枚举类静态初始化中调用 EnumUtil 不会导致 Recursive update 异常
	 * fix issue#IDQYJK
	 */
	@Test
	public void getFieldValuesRecursiveTest() {
		// SelfRefEnum 在静态初始化时调用了 EnumUtil.getNames，
		// 修复前会抛出 IllegalStateException: Recursive update
		// 修复后应正常返回结果
		final List<Object> values = EnumUtil.getFieldValues(SelfRefEnum.class, "label");
		assertNotNull(values);
		assertEquals(3, values.size());
	}

	/**
	 * 静态初始化中使用 EnumUtil 的枚举，用于测试 fix issue#IDQYJK
	 */
	public enum SelfRefEnum {
		A("labelA"), B("labelB"), C("labelC");

		// 静态初始化块中调用 EnumUtil，触发 ConcurrentHashMap.computeIfAbsent 的递归场景
		static final List<String> NAMES = EnumUtil.getNames(SelfRefEnum.class);

		private final String label;

		SelfRefEnum(final String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
}
