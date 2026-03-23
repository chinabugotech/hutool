package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * EnumUtil单元测试
 *
 * @author looly
 *
 */
public class EnumUtilTest {

	@Test
	public void getNamesTest() {
		List<String> names = EnumUtil.getNames(TestEnum.class);
		assertEquals(CollUtil.newArrayList("TEST1", "TEST2", "TEST3"), names);
	}

	@Test
	public void getFieldValuesTest() {
		List<Object> types = EnumUtil.getFieldValues(TestEnum.class, "type");
		assertEquals(CollUtil.newArrayList("type1", "type2", "type3"), types);
	}

	@Test
	public void getFieldNamesTest() {
		List<String> names = EnumUtil.getFieldNames(TestEnum.class);
		assertTrue(names.contains("type"));
		assertTrue(names.contains("name"));
	}

	@Test
	public void getByTest() {
		// 枚举中字段互相映射使用
		TestEnum testEnum = EnumUtil.getBy(TestEnum::ordinal, 1);
		assertEquals("TEST2", testEnum.name());
	}

	@Test
	public void getFieldByTest() {
		// 枚举中字段互相映射使用
		String type = EnumUtil.getFieldBy(TestEnum::getType, Enum::ordinal, 1);
		assertEquals("type2", type);

		int ordinal = EnumUtil.getFieldBy(TestEnum::ordinal, Enum::ordinal, 1);
		assertEquals(1, ordinal);
	}

	@Test
	public void likeValueOfTest() {
		TestEnum value = EnumUtil.likeValueOf(TestEnum.class, "type2");
		assertEquals(TestEnum.TEST2, value);
	}

	@Test
	public void getEnumMapTest() {
		Map<String,TestEnum> enumMap = EnumUtil.getEnumMap(TestEnum.class);
		assertEquals(TestEnum.TEST1, enumMap.get("TEST1"));
	}

	@Test
	public void getNameFieldMapTest() {
		Map<String, Object> enumMap = EnumUtil.getNameFieldMap(TestEnum.class, "type");
		assert enumMap != null;
		assertEquals("type1", enumMap.get("TEST1"));
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
		List<Object> values = EnumUtil.getFieldValues(SelfRefEnum.class, "label");
		assertNotNull(values);
		assertEquals(3, values.size());
	}

	public enum TestEnum{
		TEST1("type1"), TEST2("type2"), TEST3("type3");

		TestEnum(String type) {
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
	 * 静态初始化中使用 EnumUtil 的枚举，用于测试 fix issue#IDQYJK
	 */
	public enum SelfRefEnum {
		A("labelA"), B("labelB"), C("labelC");

		// 静态初始化块中调用 EnumUtil，触发 ConcurrentHashMap.computeIfAbsent 的递归场景
		static final List<String> NAMES = EnumUtil.getNames(SelfRefEnum.class);

		private final String label;

		SelfRefEnum(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
}
