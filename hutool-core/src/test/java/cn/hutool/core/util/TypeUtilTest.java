package cn.hutool.core.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.lang.TypeReference;
import lombok.Data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TypeUtilTest {

	@Test
	public void getEleTypeTest() {
		Method method = ReflectUtil.getMethod(TestClass.class, "getList");
		Type type = TypeUtil.getReturnType(method);
		assertEquals("java.util.List<java.lang.String>", type.toString());

		Type type2 = TypeUtil.getTypeArgument(type);
		assertEquals(String.class, type2);
	}

	@Test
	public void getParamTypeTest() {
		Method method = ReflectUtil.getMethod(TestClass.class, "intTest", Integer.class);
		Type type = TypeUtil.getParamType(method, 0);
		assertEquals(Integer.class, type);

		Type returnType = TypeUtil.getReturnType(method);
		assertEquals(Integer.class, returnType);
	}

	@Test
	public void getClasses() {
		Method method = ReflectUtil.getMethod(Parent.class, "getLevel");
		Type returnType = TypeUtil.getReturnType(method);
		Class<?> clazz = TypeUtil.getClass(returnType);
		assertEquals(Level1.class, clazz);

		method = ReflectUtil.getMethod(Level1.class, "getId");
		returnType = TypeUtil.getReturnType(method);
		clazz = TypeUtil.getClass(returnType);
		assertEquals(Object.class, clazz);
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
	 * 测试getClass方法对参数化类型数组{@code List<String>[]}的处理
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

	public static class TestClass {
		public List<String> getList() {
			return new ArrayList<>();
		}

		public Integer intTest(Integer integer) {
			return 1;
		}

	}

	@Test
	public void getTypeArgumentTest() {
		// 测试不继承父类，而是实现泛型接口时是否可以获取成功。
		final Type typeArgument = TypeUtil.getTypeArgument(IPService.class);
		assertEquals(String.class, typeArgument);
	}

	public interface OperateService<T> {
		void service(T t);
	}

	public static class IPService implements OperateService<String> {
		@Override
		public void service(String string) {
		}
	}

	@Test
	public void getActualTypesTest() {
		// 测试多层级泛型参数是否能获取成功
		Type idType = TypeUtil.getActualType(Level3.class, ReflectUtil.getField(Level3.class, "id"));

		assertEquals(Long.class, idType);
	}

	public static class Level3 extends Level2<Level3> {

	}

	public static class Level2<E> extends Level1<Long> {

	}

	@Data
	public static class Level1<T> {
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

}
