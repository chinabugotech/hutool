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

package cn.hutool.v7.core.annotation;

import cn.hutool.v7.core.annotation.elements.CombinationAnnotatedElement;
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.reflect.FieldUtil;
import cn.hutool.v7.core.reflect.method.MethodUtil;
import cn.hutool.v7.core.util.ObjUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * test for {@link AnnotationUtil}
 */
public class AnnotationUtilTest {

	@Test
	public void testGetDeclaredAnnotations() {
		final Annotation[] annotations = AnnotationUtil.getDeclaredAnnotations(ClassForTest.class);
		Assertions.assertArrayEquals(annotations, ClassForTest.class.getDeclaredAnnotations());
		Assertions.assertSame(annotations, AnnotationUtil.getDeclaredAnnotations(ClassForTest.class));

		AnnotationUtil.clearCaches();
		Assertions.assertNotSame(annotations, AnnotationUtil.getDeclaredAnnotations(ClassForTest.class));
	}

	@Test
	public void testToCombination() {
		final CombinationAnnotatedElement element = AnnotationUtil.toCombination(ClassForTest.class);
		assertEquals(2, element.getAnnotations().length);
	}

	@Test
	public void testGetAnnotations() {
		Annotation[] annotations = AnnotationUtil.getAnnotations(ClassForTest.class, true);
		assertEquals(2, annotations.length);
		annotations = AnnotationUtil.getAnnotations(ClassForTest.class, false);
		assertEquals(1, annotations.length);
	}

	@Test
	void getMethodAnnotationsTest() {
		final Method doSomeThing = MethodUtil.getMethodByName(ClassForTest.class, "doSomeThing");
		assertNotNull(doSomeThing);

		// doSomeThing方法定义了一个注解
		final Annotation[] declaredAnnotations = AnnotationUtil.getDeclaredAnnotations(doSomeThing);
		assertEquals(1, declaredAnnotations.length);

		// ClassForTest.doSomeThing没有继承，因此与getDeclaredAnnotations结果相同
		final Annotation[] annotations = AnnotationUtil.getAnnotations(doSomeThing, false);
		assertEquals(1, annotations.length);

		// ClassForTest.doSomeThing上的注解为AnnotationForTest，这个注解上也定义了一个AnnotationForTest，因此支持组合注解后，获取注解数量为2
		final Annotation[] combinationAnnotations = AnnotationUtil.getAnnotations(doSomeThing, true);
		assertEquals(2, combinationAnnotations.length);
	}

	@Test
	void getSubClassAnnotationsTest() {
		// SubClassForTest类本身无注解
		Annotation[] annotations = AnnotationUtil.getDeclaredAnnotations(SubClassForTest.class);
		assertEquals(0, annotations.length);

		// SubClassForTest类继承了ClassForTest类，因此继承了ClassForTest类上的注解，因此获取注解数量为1
		annotations = AnnotationUtil.getAnnotations(SubClassForTest.class, false);
		assertEquals(1, annotations.length);

		final Method doSomeThing = MethodUtil.getMethodByName(SubClassForTest.class, "doSomeThing");
		assertNotNull(doSomeThing);

		// doSomeThing方法在子类中无注解，因此获取注解数量为0
		annotations = AnnotationUtil.getDeclaredAnnotations(doSomeThing);
		assertEquals(0, annotations.length);

		// 方法上的注解不支持继承，因此获取注解数量为0
		annotations = AnnotationUtil.getAnnotations(doSomeThing, false);
		assertEquals(0, annotations.length);

		final Field a = FieldUtil.getField(SubClassForTest.class, "a");
		assertNotNull(doSomeThing);

		// a字段在子类中无注解，因此获取注解数量为0
		annotations = AnnotationUtil.getDeclaredAnnotations(a);
		assertEquals(0, annotations.length);

		// a字段上的注解不支持继承，因此获取注解数量为0
		annotations = AnnotationUtil.getAnnotations(a, false);
		assertNotNull(annotations);
		assertEquals(0, annotations.length);
	}

	@Test
	public void testGetCombinationAnnotations() {
		final MetaAnnotationForTest[] annotations = AnnotationUtil.getCombinationAnnotations(ClassForTest.class, MetaAnnotationForTest.class);
		assertEquals(1, annotations.length);
	}

	@Test
	public void testAnnotations() {
		MetaAnnotationForTest[] annotations1 = AnnotationUtil.getAnnotations(ClassForTest.class, false, MetaAnnotationForTest.class);
		assertEquals(0, annotations1.length);
		annotations1 = AnnotationUtil.getAnnotations(ClassForTest.class, true, MetaAnnotationForTest.class);
		assertEquals(1, annotations1.length);

		Annotation[] annotations2 = AnnotationUtil.getAnnotations(
			ClassForTest.class, false, t -> ObjUtil.equals(t.annotationType(), MetaAnnotationForTest.class)
		);
		assertEquals(0, annotations2.length);
		annotations2 = AnnotationUtil.getAnnotations(
			ClassForTest.class, true, t -> ObjUtil.equals(t.annotationType(), MetaAnnotationForTest.class)
		);
		assertEquals(1, annotations2.length);
	}

	@Test
	public void testGetAnnotation() {
		final MetaAnnotationForTest annotation = AnnotationUtil.getAnnotation(ClassForTest.class, MetaAnnotationForTest.class);
		assertNotNull(annotation);
	}

	@Test
	public void testHasAnnotation() {
		Assertions.assertTrue(AnnotationUtil.hasAnnotation(ClassForTest.class, MetaAnnotationForTest.class));
	}

	@Test
	public void testGetAnnotationValue() {
		final AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		assertEquals(annotation.value(), AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class));
		assertEquals(annotation.value(), AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class, "value"));
		Assertions.assertNull(AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class, "property"));
	}

	@Test
	public void testGetAnnotationValueMap() {
		final AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		final Map<String, Object> valueMap = AnnotationUtil.getAnnotationValueMap(ClassForTest.class, AnnotationForTest.class);
		assertNotNull(valueMap);
		assertEquals(2, valueMap.size());
		assertEquals(annotation.value(), valueMap.get("value"));
	}

	@Test
	public void testGetRetentionPolicy() {
		final RetentionPolicy policy = AnnotationForTest.class.getAnnotation(Retention.class).value();
		assertEquals(policy, AnnotationUtil.getRetentionPolicy(AnnotationForTest.class));
	}

	@Test
	public void testGetTargetType() {
		final ElementType[] types = AnnotationForTest.class.getAnnotation(Target.class).value();
		Assertions.assertArrayEquals(types, AnnotationUtil.getTargetType(AnnotationForTest.class));
	}

	@Test
	public void testIsDocumented() {
		Assertions.assertFalse(AnnotationUtil.isDocumented(AnnotationForTest.class));
	}

	@Test
	public void testIsInherited() {
		Assertions.assertFalse(AnnotationUtil.isInherited(AnnotationForTest.class));
	}

	@Test
	@EnabledForJreRange(max = JRE.JAVA_8)
	public void testSetValue() {
		// jdk9+中抛出异常，须添加`--add-opens=java.base/java.lang=ALL-UNNAMED`启动参数
		final AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		final String newValue = "is a new value";
		Assertions.assertNotEquals(newValue, annotation.value());
		AnnotationUtil.setValue(annotation, "value", newValue);
		assertEquals(newValue, annotation.value());
	}

	@Test
	public void testGetAnnotationAlias() {
		final MetaAnnotationForTest annotation = AnnotationUtil.getAnnotation(AnnotationForTest.class, MetaAnnotationForTest.class);
		Assertions.assertEquals("foo", annotation.value());
		final MetaAnnotationForTest annotationAlias = AnnotationUtil.getAnnotationAlias(AnnotationForTest.class, MetaAnnotationForTest.class);
		assertNotNull(annotationAlias);
		// value的别名是alias，因此获取value值时，实际返回的是alias的值
		assertEquals("", annotationAlias.value());
		assertEquals(annotationAlias.value(), annotationAlias.alias());
		assertEquals(MetaAnnotationForTest.class, annotationAlias.annotationType());
	}

	@Test
	public void testGetAnnotationAttributes() {
		final Method[] methods = AnnotationUtil.getAnnotationAttributes(AnnotationForTest.class);
		Assertions.assertArrayEquals(methods, AnnotationUtil.getAnnotationAttributes(AnnotationForTest.class));
		assertEquals(2, methods.length);
		Assertions.assertArrayEquals(AnnotationForTest.class.getDeclaredMethods(), methods);
	}

	@SneakyThrows
	@Test
	public void testIsAnnotationAttribute() {
		Assertions.assertFalse(AnnotationUtil.isAnnotationAttribute(AnnotationForTest.class.getMethod("equals", Object.class)));
		Assertions.assertTrue(AnnotationUtil.isAnnotationAttribute(AnnotationForTest.class.getMethod("value")));
	}

	@Test
	public void getAnnotationValueTest2() {
		final String[] names = AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest::names);
		Assertions.assertTrue(ArrayUtil.equals(names, new String[]{"测试1", "测试2"}));
	}

	@Test
	void isMetaAnnotationTest() {
		Assertions.assertTrue(AnnotationUtil.isMetaAnnotation(Deprecated.class));
		Assertions.assertFalse(AnnotationUtil.isMetaAnnotation(AnnotationForTest.class));
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface MetaAnnotationForTest{
		@Alias(value = "alias")
		String value() default "";
		String alias() default "";
	}

	@MetaAnnotationForTest("foo")
	@Target({ElementType.TYPE_USE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	private @interface AnnotationForTest{
		String value() default "";
		String[] names() default "";
	}

	@AnnotationForTest(value = "foo", names = {"测试1", "测试2"})
	private static class ClassForTest{

		@AnnotationForTest
		private int a;

		@AnnotationForTest
		public int doSomeThing(){
			return 0;
		}
	}

	private static class SubClassForTest extends ClassForTest{

		private int a;

		@Override
		public int doSomeThing() {
			return super.doSomeThing();
		}
	}
}
