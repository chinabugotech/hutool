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

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.reflect.FieldUtil;
import cn.hutool.v7.core.reflect.TypeReference;
import cn.hutool.v7.core.reflect.TypeUtil;
import cn.hutool.v7.core.reflect.method.MethodUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TypeUtilTest {

	@Test
	void getMapClassTest() {
		final Class<?> aClass = TypeUtil.getClass(new TypeReference<Map<String, String>>() {});
		assertEquals(Map.class, aClass);
	}

	@Test
	public void getEleTypeTest() {
		final Method method = MethodUtil.getMethod(TestClass.class, "getList");
		final Type type = TypeUtil.getReturnType(method);
		assertEquals("java.util.List<java.lang.String>", type.toString());

		final Type type2 = TypeUtil.getTypeArgument(type);
		assertEquals(String.class, type2);
	}

	@Test
	public void getParamTypeTest() {
		final Method method = MethodUtil.getMethod(TestClass.class, "intTest", Integer.class);
		final Type type = TypeUtil.getParamType(method, 0);
		assertEquals(Integer.class, type);

		final Type returnType = TypeUtil.getReturnType(method);
		assertEquals(Integer.class, returnType);
	}

	public static class TestClass {
		public List<String> getList(){
			return new ArrayList<>();
		}

		public Integer intTest(final Integer integer) {
			return 1;
		}
	}

	@Test
	public void getTypeArgumentTest(){
		// 测试不继承父类，而是实现泛型接口时是否可以获取成功。
		final Type typeArgument = TypeUtil.getTypeArgument(IPService.class);
		assertEquals(String.class, typeArgument);
	}

	public interface OperateService<T> {
		void service(T t);
	}

	public static class IPService implements OperateService<String> {
		@Override
		public void service(final String string) {
		}
	}

	@Test
	public void getActualTypesTest(){
		// 测试多层级泛型参数是否能获取成功
		final Type idType = TypeUtil.getActualType(Level3.class,
				FieldUtil.getField(Level3.class, "id"));

		assertEquals(Long.class, idType);
	}

	@Test
	public void getClasses() {
		Method method = MethodUtil.getMethod(Parent.class, "getLevel");
		Type returnType = TypeUtil.getReturnType(method);
		Class<?> clazz = TypeUtil.getClass(returnType);
		assertEquals(Level1.class, clazz);

		method = MethodUtil.getMethod(Level1.class, "getId");
		returnType = TypeUtil.getReturnType(method);
		clazz = TypeUtil.getClass(returnType);
		assertEquals(Object.class, clazz);
	}

	public static class Level3 extends Level2<Level3>{

	}

	public static class Level2<E> extends Level1<Long>{

	}

	@Data
	public static class Level1<T>{
		private T id;
	}

	@Data
	public static class Parent<T extends Level1<B>, B extends Long> {
		private T level;
	}

	/**
	 * fix github:issue#3873
	 */
	@Test
	public void getActualTypeForGenericArrayTest() {
		TypeReference<GenericArray<GenericArrayEle>> typeReference = new TypeReference<GenericArray<GenericArrayEle>>() {

		};

		Type levelType = TypeUtil.getFieldType(GenericArray.class, "level");
		Type actualType = TypeUtil.getActualType(typeReference.getType(), levelType);
		assertEquals(ArrayUtil.getArrayType(GenericArrayEle.class), actualType);
	}

	@Data
	public static class GenericArray<T> {
		private T[] level;
	}

	@Data
	public static class GenericArrayEle {
		private Long uid;
	}

	/**
	 * 测试getClass方法对泛型数组类型T[]的处理
	 * 验证未绑定泛型参数的数组类型会被正确解析为Object[]
	 */
	@Test
	public void getClassForGenericArrayTypeTest() throws NoSuchFieldException {
		// 获取T[]类型字段的泛型类型
		Field levelField = GenericArray.class.getDeclaredField("level");
		Type genericArrayType = levelField.getGenericType();
		// 调用getClass方法处理GenericArrayType
		Class<?> clazz = TypeUtil.getClass(genericArrayType);
		// 验证返回Object[]类型
		assertNotNull(clazz, "getClass方法返回null");
		assertTrue(clazz.isArray(), "返回类型不是数组");
		assertEquals(Object.class, clazz.getComponentType(), "数组组件类型应为Object");
	}

	/**
	 * 测试getClass方法对参数化类型数组List<String>[]的处理
	 * 验证数组组件类型能正确解析为原始类型
	 */
	@Test
	public void getClassForParameterizedArrayTypeTest() {
		// 创建List<String>[]类型引用
		Type genericArrayType = new TypeReference<List<String>[]>() {}.getType();
		// 调用getClass方法处理GenericArrayType
		Class<?> clazz = TypeUtil.getClass(genericArrayType);
		// 验证返回List[]类型
		assertEquals(Array.newInstance(List.class, 0).getClass(), clazz);
	}

}
