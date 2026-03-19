package cn.hutool.v7.core.annotation.scanner;

import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.reflect.FieldUtil;
import cn.hutool.v7.core.reflect.method.MethodUtil;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ElementAnnotationScannerTest {

	@Test
	public void supportTest() {
		final ElementAnnotationScanner scanner = new ElementAnnotationScanner();
		assertTrue(scanner.support(FieldUtil.getField(FieldAnnotationScannerTest.Example.class, "id")));
		assertTrue(scanner.support(MethodUtil.getMethod(FieldAnnotationScannerTest.Example.class, "getId")));
		assertFalse(scanner.support(null));
		assertTrue(scanner.support(FieldAnnotationScannerTest.Example.class));
	}

	@Test
	public void getAnnotationsTest() {
		final ElementAnnotationScanner scanner = new ElementAnnotationScanner();
		final Field field = FieldUtil.getField(FieldAnnotationScannerTest.Example.class, "id");
		assertNotNull(field);
		assertTrue(scanner.support(field));
		final List<Annotation> annotations = scanner.getAnnotations(field);
		assertEquals(1, annotations.size());
		assertEquals(AnnotationForScannerTest.class, Objects.requireNonNull(CollUtil.getFirst(annotations)).annotationType());
	}

	@Test
	public void scanTest() {
		final ElementAnnotationScanner scanner = new ElementAnnotationScanner();
		final Field field = FieldUtil.getField(FieldAnnotationScannerTest.Example.class, "id");
		final Map<Integer, List<Annotation>> map = new HashMap<>();
		scanner.scan(
			(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
			Objects.requireNonNull(field), null
		);
		assertEquals(1, map.size());
		assertEquals(1, map.get(0).size());
		assertEquals(AnnotationForScannerTest.class, map.get(0).get(0).annotationType());
	}

	public static class Example {
		@AnnotationForScannerTest
		private Integer id;

		public Integer getId() {
			return id;
		}
	}

}
