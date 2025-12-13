package cn.hutool.v7.core.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ArrayUtil.isSub 和 ArrayUtil.indexOfSub 方法单元测试
 */
class ArrayUtilIsSubTest {

	@Test
	void testIsSubWithNullArray() {
		// 测试空数组
		assertFalse(ArrayUtil.isSub(null, new Integer[]{1, 2}));
		assertFalse(ArrayUtil.isSub(new Integer[]{1, 2}, null));
		assertFalse(ArrayUtil.isSub(null, null));
	}

	@Test
	void testIsSubWithEmptyArray() {
		// 测试空数组
		assertFalse(ArrayUtil.isSub(new Integer[]{}, new Integer[]{1, 2}));
		assertFalse(ArrayUtil.isSub(new Integer[]{1, 2}, new Integer[]{}));
		assertFalse(ArrayUtil.isSub(new Integer[]{}, new Integer[]{}));
	}

	@Test
	void testIsSubWithExactMatch() {
		// 完全匹配的情况
		assertTrue(ArrayUtil.isSub(new Integer[]{1, 2, 3}, new Integer[]{1, 2, 3}));
		assertTrue(ArrayUtil.isSub(new String[]{"a", "b", "c"}, new String[]{"a", "b", "c"}));
	}

	@Test
	void testIsSubWithSubAtStart() {
		// 子数组在开头
		assertTrue(ArrayUtil.isSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{1, 2}));
		assertTrue(ArrayUtil.isSub(new String[]{"a", "b", "c", "d"}, new String[]{"a", "b"}));
	}

	@Test
	void testIsSubWithSubInMiddle() {
		// 子数组在中间
		assertTrue(ArrayUtil.isSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{2, 3}));
		assertTrue(ArrayUtil.isSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{3, 4}));
		assertTrue(ArrayUtil.isSub(new String[]{"a", "b", "c", "d"}, new String[]{"b", "c"}));
	}

	@Test
	void testIsSubWithSubAtEnd() {
		// 子数组在结尾
		assertTrue(ArrayUtil.isSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{4, 5}));
		assertTrue(ArrayUtil.isSub(new String[]{"a", "b", "c", "d"}, new String[]{"c", "d"}));
	}

	@Test
	void testIsSubWithSingleElement() {
		// 单个元素匹配
		assertTrue(ArrayUtil.isSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{3}));
		assertTrue(ArrayUtil.isSub(new String[]{"a", "b", "c"}, new String[]{"b"}));
	}

	@Test
	void testIsSubWithNoMatch() {
		// 不匹配的情况
		assertFalse(ArrayUtil.isSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{6, 7}));
		assertFalse(ArrayUtil.isSub(new Integer[]{1, 2, 3}, new Integer[]{2, 4}));
		assertFalse(ArrayUtil.isSub(new String[]{"a", "b", "c"}, new String[]{"d", "e"}));
	}

	@Test
	void testIsSubWithPartialMatch() {
		// 部分匹配但不是完整子数组
		assertFalse(ArrayUtil.isSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{2, 4}));
		assertFalse(ArrayUtil.isSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{1, 3}));
	}

	@Test
	void testIsSubWithLargerSubArray() {
		// 子数组比原数组大
		assertFalse(ArrayUtil.isSub(new Integer[]{1, 2, 3}, new Integer[]{1, 2, 3, 4}));
		assertFalse(ArrayUtil.isSub(new Integer[]{1, 2}, new Integer[]{1, 2, 3}));
	}

	@Test
	void testIsSubWithDuplicateValues() {
		// 包含重复值的情况
		assertTrue(ArrayUtil.isSub(new Integer[]{1, 2, 2, 3, 4}, new Integer[]{2, 2}));
		assertTrue(ArrayUtil.isSub(new Integer[]{1, 2, 2, 3, 4}, new Integer[]{2, 3}));
		assertFalse(ArrayUtil.isSub(new Integer[]{1, 2, 3, 4}, new Integer[]{2, 2}));
	}

	@Test
	void testIndexOfSubWithNullArray() {
		// 测试空数组
		assertEquals(-1, ArrayUtil.indexOfSub(null, new Integer[]{1, 2}));
		assertEquals(-1, ArrayUtil.indexOfSub(new Integer[]{1, 2}, null));
		assertEquals(-1, ArrayUtil.indexOfSub(null, null));
	}

	@Test
	void testIndexOfSubWithEmptyArray() {
		// 测试空数组
		assertEquals(-1, ArrayUtil.indexOfSub(new Integer[]{}, new Integer[]{1, 2}));
		assertEquals(-1, ArrayUtil.indexOfSub(new Integer[]{1, 2}, new Integer[]{}));
		assertEquals(-1, ArrayUtil.indexOfSub(new Integer[]{}, new Integer[]{}));
	}

	@Test
	void testIndexOfSubWithExactMatch() {
		// 完全匹配的情况
		assertEquals(0, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3}, new Integer[]{1, 2, 3}));
		assertEquals(0, ArrayUtil.indexOfSub(new String[]{"a", "b", "c"}, new String[]{"a", "b", "c"}));
	}

	@Test
	void testIndexOfSubWithSubAtStart() {
		// 子数组在开头
		assertEquals(0, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{1, 2}));
		assertEquals(0, ArrayUtil.indexOfSub(new String[]{"a", "b", "c", "d"}, new String[]{"a", "b"}));
	}

	@Test
	void testIndexOfSubWithSubInMiddle() {
		// 子数组在中间
		assertEquals(1, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{2, 3}));
		assertEquals(2, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{3, 4}));
		assertEquals(1, ArrayUtil.indexOfSub(new String[]{"a", "b", "c", "d"}, new String[]{"b", "c"}));
	}

	@Test
	void testIndexOfSubWithSubAtEnd() {
		// 子数组在结尾
		assertEquals(3, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{4, 5}));
		assertEquals(2, ArrayUtil.indexOfSub(new String[]{"a", "b", "c", "d"}, new String[]{"c", "d"}));
	}

	@Test
	void testIndexOfSubWithSingleElement() {
		// 单个元素匹配
		assertEquals(2, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{3}));
		assertEquals(1, ArrayUtil.indexOfSub(new String[]{"a", "b", "c"}, new String[]{"b"}));
	}

	@Test
	void testIndexOfSubWithNoMatch() {
		// 不匹配的情况
		assertEquals(-1, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{6, 7}));
		assertEquals(-1, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3}, new Integer[]{2, 4}));
		assertEquals(-1, ArrayUtil.indexOfSub(new String[]{"a", "b", "c"}, new String[]{"d", "e"}));
	}

	@Test
	void testIndexOfSubWithPartialMatch() {
		// 部分匹配但不是完整子数组
		assertEquals(-1, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{2, 4}));
		assertEquals(-1, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3, 4, 5}, new Integer[]{1, 3}));
	}

	@Test
	void testIndexOfSubWithLargerSubArray() {
		// 子数组比原数组大
		assertEquals(-1, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3}, new Integer[]{1, 2, 3, 4}));
		assertEquals(-1, ArrayUtil.indexOfSub(new Integer[]{1, 2}, new Integer[]{1, 2, 3}));
	}

	@Test
	void testIndexOfSubWithDuplicateValues() {
		// 包含重复值的情况
		assertEquals(1, ArrayUtil.indexOfSub(new Integer[]{1, 2, 2, 3, 4}, new Integer[]{2, 2}));
		assertEquals(2, ArrayUtil.indexOfSub(new Integer[]{1, 2, 2, 3, 4}, new Integer[]{2, 3}));
		assertEquals(-1, ArrayUtil.indexOfSub(new Integer[]{1, 2, 3, 4}, new Integer[]{2, 2}));
	}

	@Test
	void testIndexOfSubWithBeginInclude() {
		// 测试带开始位置的indexOfSub方法
		final Integer[] array = {1, 2, 3, 4, 5, 2, 3, 6};

		// 从开始位置查找
		assertEquals(1, ArrayUtil.indexOfSub(array, 0, new Integer[]{2, 3}));

		// 从中间位置查找
		assertEquals(5, ArrayUtil.indexOfSub(array, 2, new Integer[]{2, 3}));

		// 从超过匹配位置查找
		assertEquals(-1, ArrayUtil.indexOfSub(array, 6, new Integer[]{2, 3}));

		// 负索引转换为正索引
		assertEquals(5, ArrayUtil.indexOfSub(array, -3, new Integer[]{2, 3})); // -3 = array.length - 3 = 5
	}

	@Test
	void testIndexOfSubWithBeginIncludeEdgeCases() {
		final Integer[] array = {1, 2, 3, 4, 5};

		// 开始位置为负数且转换后仍然为负数
		assertEquals(-1, ArrayUtil.indexOfSub(array, -10, new Integer[]{1}));

		// 开始位置超出数组范围
		assertEquals(-1, ArrayUtil.indexOfSub(array, 10, new Integer[]{1}));

		// 开始位置等于数组长度
		assertEquals(-1, ArrayUtil.indexOfSub(array, 5, new Integer[]{1}));

		// 剩余长度不足
		assertEquals(-1, ArrayUtil.indexOfSub(array, 3, new Integer[]{2, 3}));
	}

	@Test
	void testIndexOfSubWithBeginIncludeNegativeIndex() {
		final Integer[] array = {1, 2, 3, 4, 5, 2, 3, 6};

		// 负索引转换为正索引的测试
		assertEquals(5, ArrayUtil.indexOfSub(array, -3, new Integer[]{2, 3})); // -3 -> 5
		assertEquals(6, ArrayUtil.indexOfSub(array, -2, new Integer[]{3, 6})); // -2 -> 6
		assertEquals(-1, ArrayUtil.indexOfSub(array, -1, new Integer[]{2, 3})); // -1 -> 7，超出范围
	}

	@Test
	void testIsSubWithDifferentObjectTypes() {
		// 测试不同类型的对象
		assertTrue(ArrayUtil.isSub(
			new Object[]{1, "hello", 3.14, true},
			new Object[]{"hello", 3.14}
		));

		assertFalse(ArrayUtil.isSub(
			new Object[]{1, "hello", 3.14, true},
			new Object[]{"hello", 3.14, false}
		));
	}

	@Test
	void testIsSubWithNullElements() {
		// 测试包含null元素的情况
		assertTrue(ArrayUtil.isSub(
			new String[]{"a", null, "c", "d"},
			new String[]{null, "c"}
		));

		assertFalse(ArrayUtil.isSub(
			new String[]{"a", "b", "c", "d"},
			new String[]{null, "c"}
		));

		assertTrue(ArrayUtil.isSub(
			new String[]{null, "b", "c"},
			new String[]{null}
		));
	}
}
