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

package cn.hutool.v7.core.bean;

import cn.hutool.v7.core.bean.copier.ValueProvider;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RecordUtil 单元测试
 */
class RecordUtilTest {

	// 定义测试用的Record类
	record SimpleRecord(String name, int age) {
	}

	record EmptyRecord() {
	}

	record ComplexRecord(String name, int age, boolean active) {
	}

	/**
	 * 测试 isRecord 方法
	 */
	@SuppressWarnings("ConstantValue")
	@Test
	void testIsRecord() {
		// 测试正常Record类
		assertTrue(RecordUtil.isRecord(SimpleRecord.class));
		assertTrue(RecordUtil.isRecord(EmptyRecord.class));
		assertTrue(RecordUtil.isRecord(ComplexRecord.class));

		// 测试非Record类
		assertFalse(RecordUtil.isRecord(String.class));
		assertFalse(RecordUtil.isRecord(Integer.class));
		assertFalse(RecordUtil.isRecord(Object.class));

		// 测试边界值：null输入
		assertFalse(RecordUtil.isRecord(null));

		// 测试边界值：接口
		assertFalse(RecordUtil.isRecord(Runnable.class));

		// 测试边界值：枚举
		assertFalse(RecordUtil.isRecord(TestEnum.class));
	}

	/**
	 * 测试 getRecordComponents 方法
	 */
	@Test
	void testGetRecordComponents() {
		// 测试SimpleRecord的组件
		final Map.Entry<String, Type>[] simpleComponents = RecordUtil.getRecordComponents(SimpleRecord.class);
		assertNotNull(simpleComponents);
		assertEquals(2, simpleComponents.length);

		// 验证字段名和类型
		assertEquals("name", simpleComponents[0].getKey());
		assertEquals(String.class, simpleComponents[0].getValue());
		assertEquals("age", simpleComponents[1].getKey());
		assertEquals(int.class, simpleComponents[1].getValue());

		// 测试EmptyRecord（空Record）
		final Map.Entry<String, Type>[] emptyComponents = RecordUtil.getRecordComponents(EmptyRecord.class);
		assertNotNull(emptyComponents);
		assertEquals(0, emptyComponents.length);

		// 测试ComplexRecord的组件
		final Map.Entry<String, Type>[] complexComponents = RecordUtil.getRecordComponents(ComplexRecord.class);
		assertNotNull(complexComponents);
		assertEquals(3, complexComponents.length);

		assertEquals("name", complexComponents[0].getKey());
		assertEquals(String.class, complexComponents[0].getValue());
		assertEquals("age", complexComponents[1].getKey());
		assertEquals(int.class, complexComponents[1].getValue());
		assertEquals("active", complexComponents[2].getKey());
		assertEquals(boolean.class, complexComponents[2].getValue());

		// 边界值测试：非Record类（应该抛出异常）
		assertNull(RecordUtil.getRecordComponents(String.class));
	}

	/**
	 * 测试 newInstance 方法
	 */
	@Test
	void testNewInstance() {
		// 测试正常创建SimpleRecord实例
		final ValueProvider<String> simpleProvider = new TestValueProvider(
			Map.of("name", "张三", "age", 25)
		);

		final Object simpleRecord = RecordUtil.newInstance(SimpleRecord.class, simpleProvider);
		assertNotNull(simpleRecord);
		assertInstanceOf(SimpleRecord.class, simpleRecord);
		assertEquals("张三", ((SimpleRecord) simpleRecord).name());
		assertEquals(25, ((SimpleRecord) simpleRecord).age());

		// 测试创建EmptyRecord实例
		final ValueProvider<String> emptyProvider = new TestValueProvider(Map.of());
		final Object emptyRecord = RecordUtil.newInstance(EmptyRecord.class, emptyProvider);
		assertNotNull(emptyRecord);
		assertInstanceOf(EmptyRecord.class, emptyRecord);

		// 测试创建ComplexRecord实例
		final ValueProvider<String> complexProvider = new TestValueProvider(
			Map.of("name", "李四", "age", 30, "active", true)
		);
		final Object complexRecord = RecordUtil.newInstance(ComplexRecord.class, complexProvider);
		assertNotNull(complexRecord);
		assertInstanceOf(ComplexRecord.class, complexRecord);
		final ComplexRecord cr = (ComplexRecord) complexRecord;
		assertEquals("李四", cr.name());
		assertEquals(30, cr.age());
		assertTrue(cr.active());

		// 测试部分参数缺失的情况
		final ValueProvider<String> partialProvider = new TestValueProvider(
			Map.of("name", "王五") // 缺少age参数
		);
		assertThrows(RuntimeException.class, () ->
			RecordUtil.newInstance(SimpleRecord.class, partialProvider));

		// 测试类型转换错误的情况
		final ValueProvider<String> typeErrorProvider = new TestValueProvider(
			Map.of("name", "赵六", "age", "invalid") // age应该是int类型
		);
		assertThrows(RuntimeException.class, () ->
			RecordUtil.newInstance(SimpleRecord.class, typeErrorProvider));

		// 测试边界值：null输入
		assertThrows(NullPointerException.class, () ->
			RecordUtil.newInstance(null, simpleProvider));
		assertThrows(NullPointerException.class, () ->
			RecordUtil.newInstance(SimpleRecord.class, null));

		// 测试边界值：非Record类
		assertThrows(NullPointerException.class, () ->
			RecordUtil.newInstance(String.class, simpleProvider));
	}

	/**
	 * 测试枚举类（用于边界值测试）
	 */
	enum TestEnum {
		VALUE1, VALUE2
	}

	/**
	 * 测试用的ValueProvider实现
	 */
	static class TestValueProvider implements ValueProvider<String> {
		private final Map<String, Object> values;

		TestValueProvider(final Map<String, Object> values) {
			this.values = values;
		}

		@Override
		public Object value(final String key, final Type valueType) {
			return values.get(key);
		}

		@Override
		public boolean containsKey(final String key) {
			return values.containsKey(key);
		}
	}
}
