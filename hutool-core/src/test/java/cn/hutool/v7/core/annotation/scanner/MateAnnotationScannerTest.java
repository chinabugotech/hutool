package cn.hutool.v7.core.annotation.scanner;

import cn.hutool.v7.core.collection.iter.IterUtil;
import cn.hutool.v7.core.reflect.FieldUtil;
import cn.hutool.v7.core.reflect.method.MethodUtil;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MateAnnotationScannerTest {

	@Test
	public void supportTest() {
		final AnnotationScanner scanner = new MetaAnnotationScanner();
		assertTrue(scanner.support(AnnotationForScannerTest.class));
		assertFalse(scanner.support(FieldUtil.getField(Example.class, "id")));
		assertFalse(scanner.support(MethodUtil.getMethod(Example.class, "getId")));
		assertFalse(scanner.support(null));
		assertFalse(scanner.support(Example.class));
	}

	@Test
	public void getAnnotationsTest() {
		AnnotationScanner scanner = new MetaAnnotationScanner();
		assertTrue(scanner.support(AnnotationForScannerTest3.class));
		Map<Class<? extends Annotation>, Annotation> annotations = IterUtil.toMap(scanner.getAnnotations(AnnotationForScannerTest3.class), Annotation::annotationType);
		assertEquals(3, annotations.size());
		assertTrue(annotations.containsKey(AnnotationForScannerTest.class));
		assertTrue(annotations.containsKey(AnnotationForScannerTest1.class));
		assertTrue(annotations.containsKey(AnnotationForScannerTest2.class));
		assertFalse(annotations.containsKey(AnnotationForScannerTest3.class));

		scanner = new MetaAnnotationScanner(false);
		assertTrue(scanner.support(AnnotationForScannerTest3.class));
		annotations = IterUtil.toMap(scanner.getAnnotations(AnnotationForScannerTest3.class), Annotation::annotationType);
		assertEquals(1, annotations.size());
		assertTrue(annotations.containsKey(AnnotationForScannerTest2.class));
		assertFalse(annotations.containsKey(AnnotationForScannerTest.class));
		assertFalse(annotations.containsKey(AnnotationForScannerTest1.class));
		assertFalse(annotations.containsKey(AnnotationForScannerTest3.class));
	}

	@Test
	public void scanTest() {
		final AnnotationScanner scanner = new MetaAnnotationScanner();
		final Map<Integer, List<Annotation>> map = new HashMap<>();
		scanner.scan(
			(index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
			AnnotationForScannerTest3.class, null
		);

		assertEquals(3, map.size());
		assertEquals(1, map.get(0).size());
		assertEquals(AnnotationForScannerTest2.class, map.get(0).get(0).annotationType());

		assertEquals(1, map.get(1).size());
		assertEquals(AnnotationForScannerTest1.class, map.get(1).get(0).annotationType());

		assertEquals(1, map.get(2).size());
		assertEquals(AnnotationForScannerTest.class, map.get(2).get(0).annotationType());
	}

	static class Example {
		private Integer id;
		public Integer getId() {
			return id;
		}
	}

	@AnnotationForScannerTest
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
	@interface AnnotationForScannerTest1 {}

	@AnnotationForScannerTest1
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
	@interface AnnotationForScannerTest2 {}

	@AnnotationForScannerTest2
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
	@interface AnnotationForScannerTest3 {}
}
