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

package cn.hutool.v7.core.reflect;

import cn.hutool.v7.core.collection.CollUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldUtilTest {
	@Test
	public void getFieldTest() {
		// 能够获取到父类字段
		final Field privateField = FieldUtil.getField(ReflectTestBeans.TestSubClass.class, "privateField");
		Assertions.assertNotNull(privateField);
	}

	@Test
	public void getFieldsTest() {
		// 能够获取到父类字段
		Field[] fields = FieldUtil.getFields(ReflectTestBeans.TestSubClass.class);
		assertEquals(4, fields.length);

		// 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
		fields = FieldUtil.getFields(TestSubUser.class);
		assertEquals(4, fields.length);
		final List<Field> idFieldList = Arrays.stream(fields).filter(f -> Objects.equals(f.getName(), TestSubUser.Fields.id)).collect(Collectors.toList());
		final Field firstIdField = CollUtil.getFirst(idFieldList);
		assertEquals(Objects.requireNonNull(firstIdField).getDeclaringClass().getName(), TestSubUser.class.getName());
	}

	@Test
	public void setFieldTest() {
		final ReflectTestBeans.AClass testClass = new ReflectTestBeans.AClass();
		FieldUtil.setFieldValue(testClass, "a", "111");
		assertEquals(111, testClass.getA());
	}

	@Test
	public void getDeclaredField() {
		final Field noField = FieldUtil.getField(ReflectTestBeans.TestSubClass.class, "noField");
		Assertions.assertNull(noField);

		// 获取不到父类字段
		final Field field = FieldUtil.getDeclaredField(ReflectTestBeans.TestSubClass.class, "field");
		Assertions.assertNull(field);

		final Field subField = FieldUtil.getField(ReflectTestBeans.TestSubClass.class, "subField");
		Assertions.assertNotNull(subField);
	}

	@Test
	void getFieldsValueTest() {
		final TestBean testBean = new TestBean();
		testBean.setA("A");
		testBean.setB(1);

		final Object[] fieldsValue = FieldUtil.getFieldsValue(testBean);
		assertEquals(2, fieldsValue.length);
		assertEquals("A", fieldsValue[0]);
		assertEquals(1, fieldsValue[1]);
	}

	@Test
	void getFieldsValueTest2() {
		final TestBean testBean = new TestBean();
		testBean.setA("A");
		testBean.setB(1);

		final Object[] fieldsValue = FieldUtil.getFieldsValue(testBean, (field ->  field.getName().equals("a")));
		assertEquals(1, fieldsValue.length);
		assertEquals("A", fieldsValue[0]);
	}

	@Test
	public void getFieldMapTest() {
		// 获取指定类中字段名和字段对应的有序Map，包括其父类中的字段
		// 如果子类与父类中存在同名字段，则后者覆盖前者。
		final Map<String, Field> fieldMap = FieldUtil.getFieldMap(TestSubUser.class);
		assertEquals(3, fieldMap.size());
	}

	@Data
	static class TestBean{
		private String a;
		private int b;
	}

	@Data
	static class TestBaseEntity {
		private Long id;
		private String remark;
	}

	@Data
	@FieldNameConstants
	@EqualsAndHashCode(callSuper = true)
	static class TestSubUser extends TestBaseEntity {
		private Long id;
		private String name;
	}
}
