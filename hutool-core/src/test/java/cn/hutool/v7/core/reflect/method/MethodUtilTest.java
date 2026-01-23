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

package cn.hutool.v7.core.reflect.method;

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.date.StopWatch;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.lang.test.bean.ExamInfoDict;
import cn.hutool.v7.core.reflect.ClassUtil;
import cn.hutool.v7.core.reflect.ReflectTestBeans;
import cn.hutool.v7.core.text.StrUtil;
import lombok.Data;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class MethodUtilTest extends ReflectTestBeans {

	@Test
	public void getMethodsTest() {
		Method[] methods = MethodUtil.getMethods(ExamInfoDict.class);
		assertTrue(methods.length >= 19 && methods.length <= 20);

		//过滤器测试
		methods = MethodUtil.getMethods(ExamInfoDict.class, t -> Integer.class.equals(t.getReturnType()));

		assertEquals(4, methods.length);
		final Method method = methods[0];
		assertNotNull(method);

		//null过滤器测试
		methods = MethodUtil.getMethods(ExamInfoDict.class, null);

		assertTrue(methods.length >= 19 && methods.length <= 20);
		final Method method2 = methods[0];
		assertNotNull(method2);
	}

	@Test
	public void getMethodTest() {
		Method method = MethodUtil.getMethod(ExamInfoDict.class, "getId");
		assertEquals("getId", method.getName());
		assertEquals(0, method.getParameterTypes().length);

		method = MethodUtil.getMethod(ExamInfoDict.class, "getId", Integer.class);
		assertEquals("getId", method.getName());
		assertEquals(1, method.getParameterTypes().length);
	}

	@Test
	public void getMethodIgnoreCaseTest() {
		Method method = MethodUtil.getMethodIgnoreCase(ExamInfoDict.class, "getId");
		assertEquals("getId", method.getName());
		assertEquals(0, method.getParameterTypes().length);

		method = MethodUtil.getMethodIgnoreCase(ExamInfoDict.class, "GetId");
		assertEquals("getId", method.getName());
		assertEquals(0, method.getParameterTypes().length);

		method = MethodUtil.getMethodIgnoreCase(ExamInfoDict.class, "setanswerIs", Integer.class);
		assertEquals("setAnswerIs", method.getName());
		assertEquals(1, method.getParameterTypes().length);
	}

	@Test
	public void invokeTest() {
		final AClass testClass = new AClass();
		MethodUtil.invoke(testClass, "setA", 10);
		assertEquals(10, testClass.getA());
	}

	@Test
	public void getDeclaredMethodsTest() {
		Class<?> type = TestBenchClass.class;
		Method[] methods = type.getDeclaredMethods();
		assertArrayEquals(methods, MethodUtil.getDeclaredMethods(type));
		assertSame(MethodUtil.getDeclaredMethods(type), MethodUtil.getDeclaredMethods(type));

		type = Object.class;
		methods = type.getDeclaredMethods();
		assertArrayEquals(methods, MethodUtil.getDeclaredMethods(type));
	}

	@Test
	@Disabled
	public void getMethodBenchTest() {
		// 预热
		getMethodWithReturnTypeCheck(TestBenchClass.class, false, "getH");

		final StopWatch timer = new StopWatch();
		timer.start();
		for (int i = 0; i < 100000000; i++) {
			MethodUtil.getMethod(TestBenchClass.class, false, "getH");
		}
		timer.stop();
		Console.log(timer.getLastTaskTimeMillis());

		timer.start();
		for (int i = 0; i < 100000000; i++) {
			getMethodWithReturnTypeCheck(TestBenchClass.class, false, "getH");
		}
		timer.stop();
		Console.log(timer.getLastTaskTimeMillis());
	}

	@SuppressWarnings("UnusedReturnValue")
	public static Method getMethodWithReturnTypeCheck(final Class<?> clazz, final boolean ignoreCase, final String methodName, final Class<?>... paramTypes) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(methodName)) {
			return null;
		}

		Method res = null;
		final Method[] methods = MethodUtil.getMethods(clazz);
		if (ArrayUtil.isNotEmpty(methods)) {
			for (final Method method : methods) {
				if (StrUtil.equals(methodName, method.getName(), ignoreCase)
						&& ClassUtil.isAllAssignableFrom(method.getParameterTypes(), paramTypes)
						&& (res == null
						|| res.getReturnType().isAssignableFrom(method.getReturnType()))) {
					res = method;
				}
			}
		}
		return res;
	}

	@Test
	public void getMethodsFromClassExtends() {
		// 继承情况下，需解决方法去重问题
		Method[] methods = MethodUtil.getMethods(C2.class);
		// 在使用jacoco时，会多出jacocoInit方法，在此兼容
		assertTrue(methods.length >= 14 && methods.length <= 15);

		// 排除Object中的方法
		// 3个方法包括类
		methods = MethodUtil.getMethodsDirectly(C2.class, true, false);
		assertTrue(methods.length >= 3 && methods.length <= 4);

		int index = 0;
		if(methods[index].toString().contains("jacocoInit")){
			// 引入jacoco后，会出jacocoInit方法，忽略之
			index++;
		}

		// getA属于本类
		assertEquals("public void cn.hutool.v7.core.reflect.ReflectTestBeans$C2.getA()", methods[index].toString());
		// getB属于父类
		assertEquals("public void cn.hutool.v7.core.reflect.ReflectTestBeans$C1.getB()", methods[index + 1].toString());
		// getC属于接口中的默认方法
		assertEquals("public default void cn.hutool.v7.core.reflect.ReflectTestBeans$TestInterface1.getC()", methods[index + 2].toString());
	}

	@Test
	public void getMethodsFromInterfaceTest() {
		// 对于接口，直接调用Class.getMethods方法获取所有方法，因为接口都是public方法
		// 因此此处得到包括TestInterface1、TestInterface2、TestInterface3中一共4个方法
		final Method[] methods = MethodUtil.getMethods(TestInterface3.class);
		assertEquals(4, methods.length);

		// 接口里，调用getMethods和getPublicMethods效果相同
		final Method[] publicMethods = MethodUtil.getPublicMethods(TestInterface3.class);
		assertArrayEquals(methods, publicMethods);
	}

	@Test
	public void getPublicMethod() {
		final Method superPublicMethod = MethodUtil.getPublicMethod(TestSubClass.class, false, "publicMethod");
		assertNotNull(superPublicMethod);
		final Method superPrivateMethod = MethodUtil.getPublicMethod(TestSubClass.class, false, "privateMethod");
		assertNull(superPrivateMethod);

		final Method publicMethod = MethodUtil.getPublicMethod(TestSubClass.class, false, "publicSubMethod");
		assertNotNull(publicMethod);
		final Method privateMethod = MethodUtil.getPublicMethod(TestSubClass.class, false, "privateSubMethod");
		assertNull(privateMethod);
	}

	@Test
	public void getDeclaredMethod() {
		final Method noMethod = MethodUtil.getMethod(TestSubClass.class, "noMethod");
		assertNull(noMethod);

		final Method privateMethod = MethodUtil.getMethod(TestSubClass.class, "privateMethod");
		assertNotNull(privateMethod);
		final Method publicMethod = MethodUtil.getMethod(TestSubClass.class, "publicMethod");
		assertNotNull(publicMethod);

		final Method publicSubMethod = MethodUtil.getMethod(TestSubClass.class, "publicSubMethod");
		assertNotNull(publicSubMethod);
		final Method privateSubMethod = MethodUtil.getMethod(TestSubClass.class, "privateSubMethod");
		assertNotNull(privateSubMethod);
	}

	@Test
	public void issue2625Test(){
		// 内部类继承的情况下父类方法会被定义为桥接方法，因此按照pr#1965@Github判断返回值的继承关系来代替判断桥接。
		final Method getThis = MethodUtil.getMethod(A.C.class, "getThis");
		assertTrue(getThis.isBridge());
	}

	@SuppressWarnings("InnerClassMayBeStatic")
	public class A{

		public class C extends B{

		}

		protected class B{
			public B getThis(){
				return this;
			}
		}
	}

	@Data
	static class TestClass {
		private int a;
	}

	@Test
	public void invokeMethodTest() {
		final TestClass testClass = new TestClass();
		final Method method = MethodUtil.getMethod(TestClass.class, "setA", int.class);
		MethodUtil.invoke(testClass, method, 10);
		assertEquals(10, testClass.getA());
	}

	@Test
	public void invokeMethodWithParamConvertTest() {
		final TestClass testClass = new TestClass();
		final Method method = MethodUtil.getMethod(TestClass.class, "setA", int.class);
		MethodUtil.invoke(testClass, method, "10");
		assertEquals(10, testClass.getA());
	}

	@Test
	public void invokeMethodWithParamConvertFailedTest() {
		final TestClass testClass = new TestClass();
		final Method method = MethodUtil.getMethod(TestClass.class, "setA", int.class);
		assertThrows(IllegalArgumentException.class,
				() -> MethodUtil.invoke(testClass, method, "aaa"));
	}
}
