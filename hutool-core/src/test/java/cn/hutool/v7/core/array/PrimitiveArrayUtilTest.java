/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.core.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PrimitiveArrayUtilTest {
	/**
	 * 测试当原始数组为null时的情况
	 */
	@Test
	public void testRemoveElementsWithNullArray() {
		final char[] result = PrimitiveArrayUtil.removeElements((char[]) null, 'a', 'b');
		assertNull(result, "当原始数组为null时，应返回null");
	}

	/**
	 * 测试当原始数组为空数组时的情况
	 */
	@Test
	public void testRemoveElementsWithEmptyArray() {
		final char[] original = {};
		final char[] elementsToRemove = {'a', 'b'};

		final char[] result = PrimitiveArrayUtil.removeElements(original, elementsToRemove);

		assertArrayEquals(original, result, "当原始数组为空数组时，应返回原数组");
	}

	/**
	 * 测试当要移除的元素数组为null时的情况
	 */
	@Test
	public void testRemoveElementsWithNullElements() {
		final char[] original = {'a', 'b', 'c'};
		final char[] result = PrimitiveArrayUtil.removeElements(original, (char[]) null);

		assertArrayEquals(original, result, "当要移除的元素数组为null时，应返回原数组");
	}

	/**
	 * 测试当要移除的元素数组为空时的情况
	 */
	@Test
	public void testRemoveElementsWithEmptyElements() {
		final char[] original = {'a', 'b', 'c'};
		final char[] elementsToRemove = {};

		final char[] result = PrimitiveArrayUtil.removeElements(original, elementsToRemove);

		assertArrayEquals(original, result, "当要移除的元素数组为空时，应返回原数组");
	}

	/**
	 * 测试移除存在的字符元素
	 */
	@Test
	public void testRemoveElementsExistingChars() {
		final char[] original = {'a', 'b', 'c', 'd', 'e'};
		final char[] elementsToRemove = {'b', 'd'};
		final char[] expected = {'a', 'c', 'e'};

		final char[] result = PrimitiveArrayUtil.removeElements(original, elementsToRemove);

		assertArrayEquals(expected, result, "应正确移除指定的字符元素");
	}

	/**
	 * 测试移除不存在的字符元素
	 */
	@Test
	public void testRemoveElementsNonExistingChars() {
		final char[] original = {'a', 'b', 'c'};
		final char[] elementsToRemove = {'x', 'y'};
		final char[] expected = {'a', 'b', 'c'};

		final char[] result = PrimitiveArrayUtil.removeElements(original, elementsToRemove);

		assertArrayEquals(expected, result, "当要移除的字符不存在时，应返回原数组");
	}

	/**
	 * 测试移除所有元素的情况
	 */
	@Test
	public void testRemoveElementsAllChars() {
		final char[] original = {'a', 'b', 'c'};
		final char[] elementsToRemove = {'a', 'b', 'c'};
		final char[] expected = {};

		final char[] result = PrimitiveArrayUtil.removeElements(original, elementsToRemove);

		assertArrayEquals(expected, result, "当移除所有元素时，应返回空数组");
	}

	/**
	 * 测试包含重复字符的情况
	 */
	@Test
	public void testRemoveElementsWithDuplicates() {
		final char[] original = {'a', 'b', 'a', 'c', 'b', 'd'};
		final char[] elementsToRemove = {'a', 'b'};
		final char[] expected = {'c', 'd'};

		final char[] result = PrimitiveArrayUtil.removeElements(original, elementsToRemove);

		assertArrayEquals(expected, result, "应正确处理重复字符的移除");
	}

	/**
	 * 测试包含特殊字符的情况
	 */
	@Test
	public void testRemoveElementsWithSpecialChars() {
		final char[] original = {' ', 'a', '\n', 'b', '\t', 'c'};
		final char[] elementsToRemove = {' ', '\n'};
		final char[] expected = {'a', 'b', '\t', 'c'};

		final char[] result = PrimitiveArrayUtil.removeElements(original, elementsToRemove);

		assertArrayEquals(expected, result, "应正确处理特殊字符的移除");
	}

	/**
	 * 测试移除单个字符的情况
	 */
	@Test
	public void testRemoveElementsSingleChar() {
		final char[] original = {'a', 'b', 'c', 'd'};
		final char[] elementsToRemove = {'c'};
		final char[] expected = {'a', 'b', 'd'};

		final char[] result = PrimitiveArrayUtil.removeElements(original, elementsToRemove);

		assertArrayEquals(expected, result, "应正确移除单个字符");
	}

	/**
	 * 测试移除一个不存在的单个字符
	 */
	@Test
	public void testRemoveElementsSingleNonExistingChar() {
		final char[] original = {'a', 'b', 'c'};
		final char[] elementsToRemove = {'x'};
		final char[] expected = {'a', 'b', 'c'};

		final char[] result = PrimitiveArrayUtil.removeElements(original, elementsToRemove);

		assertArrayEquals(expected, result, "当移除不存在的单个字符时，应返回原数组");
	}
}
