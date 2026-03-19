package cn.hutool.v7.core.annotation.scanner;

import cn.hutool.v7.core.reflect.ClassUtil;
import cn.hutool.v7.core.reflect.FieldUtil;
import cn.hutool.v7.core.reflect.method.MethodUtil;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TypeAnnotationScannerTest {

	@Test
	public void supportTest() {
		final AnnotationScanner scanner = new TypeAnnotationScanner();
		assertTrue(scanner.support(Example.class));
		assertFalse(scanner.support(FieldUtil.getField(Example.class, "id")));
		assertFalse(scanner.support(MethodUtil.getMethod(Example.class, "getId")));
		assertFalse(scanner.support(null));
	}

	@Test
	public void getAnnotationsTest() {
		AnnotationScanner scanner = new TypeAnnotationScanner();
		List<Annotation> annotations = scanner.getAnnotations(Example.class);
		assertEquals(3, annotations.size());
		annotations.forEach(a -> assertEquals(AnnotationForScannerTest.class, a.annotationType()));

		// 不查找父接口
		scanner = new TypeAnnotationScanner().setIncludeInterfaces(false);
		annotations = scanner.getAnnotations(Example.class);
		assertEquals(2, annotations.size());
		annotations.forEach(a -> assertEquals(AnnotationForScannerTest.class, a.annotationType()));

		// 不查找父类
		scanner = new TypeAnnotationScanner().setIncludeSuperClass(false);
		annotations = scanner.getAnnotations(Example.class);
		assertEquals(1, annotations.size());
		annotations.forEach(a -> assertEquals(AnnotationForScannerTest.class, a.annotationType()));

		// 不查找ExampleSupplerClass.class
		scanner = new TypeAnnotationScanner().addExcludeTypes(ExampleSupplerClass.class);
		annotations = scanner.getAnnotations(Example.class);
		assertEquals(1, annotations.size());
		annotations.forEach(a -> assertEquals(AnnotationForScannerTest.class, a.annotationType()));

		// 只查找ExampleSupplerClass.class
		scanner = new TypeAnnotationScanner().setFilter(t -> ClassUtil.isAssignable(ExampleSupplerClass.class, t));
		annotations = scanner.getAnnotations(Example.class);
		assertEquals(2, annotations.size());
		annotations.forEach(a -> assertEquals(AnnotationForScannerTest.class, a.annotationType()));
	}

	@Test
	public void scanTest() {
		final Map<Integer, List<Annotation>> map = new HashMap<>();

		// 查找父类与父接口
		new TypeAnnotationScanner().scan(
			(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
			Example.class, null
		);
		assertEquals(3, map.size());
		assertEquals(1, map.get(0).size());
		assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		assertEquals(1, map.get(1).size());
		assertEquals("ExampleSupplerClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
		assertEquals(1, map.get(2).size());
		assertEquals("ExampleInterface", ((AnnotationForScannerTest) map.get(2).get(0)).value());

		// 不查找父接口
		map.clear();
		new TypeAnnotationScanner()
			.setIncludeInterfaces(false)
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				Example.class, null
			);
		assertEquals(2, map.size());
		assertEquals(1, map.get(0).size());
		assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		assertEquals(1, map.get(1).size());
		assertEquals("ExampleSupplerClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());

		// 不查找父类
		map.clear();
		new TypeAnnotationScanner()
			.setIncludeSuperClass(false)
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				Example.class, null
			);
		assertEquals(1, map.size());
		assertEquals(1, map.get(0).size());
		assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());

		// 不查找ExampleSupplerClass.class
		map.clear();
		new TypeAnnotationScanner()
			.addExcludeTypes(ExampleSupplerClass.class)
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				Example.class, null
			);
		assertEquals(1, map.size());
		assertEquals(1, map.get(0).size());
		assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());

		// 只查找ExampleSupplerClass.class
		map.clear();
		new TypeAnnotationScanner()
			.setFilter(t -> ClassUtil.isAssignable(ExampleSupplerClass.class, t))
			.scan(
				(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
				Example.class, null
			);
		assertEquals(2, map.size());
		assertEquals(1, map.get(0).size());
		assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
		assertEquals(1, map.get(1).size());
		assertEquals("ExampleSupplerClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
	}

	@AnnotationForScannerTest("ExampleSupplerClass")
	static class ExampleSupplerClass implements ExampleInterface {}

	@AnnotationForScannerTest("ExampleInterface")
	interface ExampleInterface {}

	@AnnotationForScannerTest("Example")
	static class Example extends ExampleSupplerClass {
		private Integer id;
		public Integer getId() {
			return id;
		}
	}

}
