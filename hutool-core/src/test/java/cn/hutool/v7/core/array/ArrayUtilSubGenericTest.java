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

package cn.hutool.v7.core.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link ArrayUtil} sub方法泛型版本单元测试
 * 专门测试泛型版本的 sub 方法：
 * - sub(A array, int beginInclude, int endExclude)
 * - sub(A array, int beginInclude, int endExclude, int step)
 */
public class ArrayUtilSubGenericTest {

	// region === 测试 sub(A array, int beginInclude, int endExclude) ===

	@Test
	public void testSubGeneric_withStringArray() {
		// 测试正常截取
		final String[] array = {"a", "b", "c", "d", "e"};
		final String[] result = ArrayUtil.sub(array, 1, 4);
		assertArrayEquals(new String[]{"b", "c", "d"}, result);

		// 测试空数组
		final String[] emptyArray = {};
		final String[] emptyResult = ArrayUtil.sub(emptyArray, 0, 0);
		assertArrayEquals(new String[]{}, emptyResult);

		// 测试边界情况
		final String[] fullCopy = ArrayUtil.sub(array, 0, 5);
		assertArrayEquals(new String[]{"a", "b", "c", "d", "e"}, fullCopy);

		// 测试超出范围
		final String[] beyondEnd = ArrayUtil.sub(array, 3, 10);
		assertArrayEquals(new String[]{"d", "e"}, beyondEnd);

		// 测试负索引
		final String[] negativeStart = ArrayUtil.sub(array, -4, 4);
		assertArrayEquals(new String[]{"b", "c", "d"}, negativeStart);

		final String[] negativeEnd = ArrayUtil.sub(array, 1, -1);
		assertArrayEquals(new String[]{"b", "c", "d"}, negativeEnd);
	}

	@Test
	public void testSubGeneric_withIntegerArray() {
		// 测试整型数组
		final Integer[] array = {1, 2, 3, 4, 5};
		final Integer[] result = ArrayUtil.sub(array, 1, 4);
		assertArrayEquals(new Integer[]{2, 3, 4}, result);

		// 测试单个元素
		final Integer[] singleResult = ArrayUtil.sub(array, 0, 1);
		assertArrayEquals(new Integer[]{1}, singleResult);

		// 测试反转索引
		final Integer[] reversed = ArrayUtil.sub(array, 4, 1);
		assertArrayEquals(new Integer[]{2, 3, 4}, reversed);

		// 测试负索引
		final Integer[] negativeResult = ArrayUtil.sub(array, -3, -1);
		assertArrayEquals(new Integer[]{3, 4}, negativeResult);
	}

	@Test
	public void testSubGeneric_withObjectArray() {
		// 测试混合类型对象数组
		final Object[] array = {"string", 123, 45.67, true, 'c'};
		final Object[] result = ArrayUtil.sub(array, 1, 4);
		assertArrayEquals(new Object[]{123, 45.67, true}, result);

		// 测试单元素截取
		final Object[] singleResult = ArrayUtil.sub(array, 2, 3);
		assertArrayEquals(new Object[]{45.67}, singleResult);
	}

	@Test
	public void testSubGeneric_withPrimitiveArrays() {
		// 测试 int 数组
		final int[] intArray = {1, 2, 3, 4, 5};
		final int[] intResult = ArrayUtil.sub(intArray, 1, 4);
		assertArrayEquals(new int[]{2, 3, 4}, intResult);

		// 测试 double 数组
		final double[] doubleArray = {1.1, 2.2, 3.3, 4.4};
		final double[] doubleResult = ArrayUtil.sub(doubleArray, 0, 2);
		assertArrayEquals(new double[]{1.1, 2.2}, doubleResult, 0.001);

		// 测试 boolean 数组
		final boolean[] boolArray = {true, false, true, false};
		final boolean[] boolResult = ArrayUtil.sub(boolArray, 1, 3);
		assertArrayEquals(new boolean[]{false, true}, boolResult);

		// 测试 char 数组
		final char[] charArray = {'a', 'b', 'c', 'd'};
		final char[] charResult = ArrayUtil.sub(charArray, 2, 4);
		assertArrayEquals(new char[]{'c', 'd'}, charResult);
	}

	@Test
	public void testSubGeneric_edgeCases() {
		// 测试空数组边界
		final String[] emptyArray = {};
		final String[] result1 = ArrayUtil.sub(emptyArray, 0, 0);
		assertArrayEquals(new String[]{}, result1);

		final String[] result2 = ArrayUtil.sub(emptyArray, 1, 3);
		assertArrayEquals(new String[]{}, result2);

		// 测试单元素数组
		final String[] singleArray = {"only"};
		final String[] singleResult = ArrayUtil.sub(singleArray, 0, 1);
		assertArrayEquals(new String[]{"only"}, singleResult);

		final String[] beyondSingle = ArrayUtil.sub(singleArray, 0, 5);
		assertArrayEquals(new String[]{"only"}, beyondSingle);

		final String[] emptyFromSingle = ArrayUtil.sub(singleArray, 1, 1);
		assertArrayEquals(new String[]{}, emptyFromSingle);

		// 测试索引翻转
		final Integer[] array = {1, 2, 3, 4};
		final Integer[] reversed = ArrayUtil.sub(array, 3, 0);
		assertArrayEquals(new Integer[]{1, 2, 3}, reversed);
	}

	@Test
	public void testSubGeneric_outOfBounds() {
		// 测试索引超出范围的情况
		final String[] array = {"a", "b", "c"};

		// 起始位置超出数组长度
		final String[] result1 = ArrayUtil.sub(array, 5, 10);
		assertArrayEquals(new String[]{}, result1);

		// 结束位置超过数组长度但起始位置有效
		final String[] result2 = ArrayUtil.sub(array, 1, 10);
		assertArrayEquals(new String[]{"b", "c"}, result2);
	}

	// endregion

	// region === 测试 sub(A array, int beginInclude, int endExclude, int step) ===

	@Test
	public void testSubGenericWithStep_basicFunctionality() {
		// 测试基本步进功能
		final String[] array = {"a", "b", "c", "d", "e", "f", "g"};

		// 步进为1（正常截取）
		final String[] step1 = ArrayUtil.sub(array, 1, 6, 1);
		assertArrayEquals(new String[]{"b", "c", "d", "e", "f"}, step1);

		// 步进为2（隔一个取一个）
		final String[] step2 = ArrayUtil.sub(array, 0, 7, 2);
		assertArrayEquals(new String[]{"a", "c", "e", "g"}, step2);

		// 步进为3（隔两个取一个）
		final String[] step3 = ArrayUtil.sub(array, 0, 7, 3);
		assertArrayEquals(new String[]{"a", "d", "g"}, step3);
	}

	@Test
	public void testSubGenericWithStep_variousSteps() {
		// 测试不同的步进值
		final Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

		// 步进为2
		final Integer[] step2 = ArrayUtil.sub(array, 0, 10, 2);
		assertArrayEquals(new Integer[]{1, 3, 5, 7, 9}, step2);

		// 步进为3
		final Integer[] step3 = ArrayUtil.sub(array, 1, 10, 3);
		assertArrayEquals(new Integer[]{2, 5, 8}, step3);

		// 步进为4
		final Integer[] step4 = ArrayUtil.sub(array, 2, 9, 4);
		assertArrayEquals(new Integer[]{3, 7}, step4);

		// 步进为5
		final Integer[] step5 = ArrayUtil.sub(array, 0, 10, 5);
		assertArrayEquals(new Integer[]{1, 6}, step5);
	}

	@Test
	public void testSubGenericWithStep_edgeCases() {
		// 测试边界情况
		final String[] array = {"a", "b", "c", "d"};

		// 步进大于数组长度
		final String[] largeStep = ArrayUtil.sub(array, 0, 4, 10);
		assertArrayEquals(new String[]{"a"}, largeStep);

		// 空结果（起始位置等于结束位置）
		final String[] empty = ArrayUtil.sub(array, 2, 2, 1);
		assertArrayEquals(new String[]{}, empty);

		// 单元素结果
		final String[] single = ArrayUtil.sub(array, 1, 4, 3);
		assertArrayEquals(new String[]{"b"}, single);

		// 负索引
		final Integer[] intArray = {1, 2, 3, 4, 5};
		final Integer[] negativeIndex = ArrayUtil.sub(intArray, -3, -1, 1);
		assertArrayEquals(new Integer[]{3, 4}, negativeIndex);
	}

	@Test
	public void testSubGenericWithStep_primitiveArrays() {
		// 测试基本类型数组
		final int[] intArray = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

		// 基本步进测试
		final int[] step2 = ArrayUtil.sub(intArray, 0, 10, 2);
		assertArrayEquals(new int[]{10, 30, 50, 70, 90}, step2);

		final int[] step3 = ArrayUtil.sub(intArray, 1, 8, 3);
		assertArrayEquals(new int[]{20, 50, 80}, step3);

		// 测试 double 数组
		final double[] doubleArray = {1.1, 2.2, 3.3, 4.4, 5.5, 6.6};
		final double[] doubleStep = ArrayUtil.sub(doubleArray, 1, 6, 2);
		assertArrayEquals(new double[]{2.2, 4.4, 6.6}, doubleStep, 0.001);
	}

	@Test
	public void testSubGenericWithStep_invalidStep() {
		// 测试无效步进值 - 根据 ArrayWrapper.getSub 实现，step <= 1 会被设为 1
		final String[] array = {"a", "b", "c"};

		// 步进为0 - 应该被设为1
		final String[] step0 = ArrayUtil.sub(array, 0, 3, 0);
		assertArrayEquals(new String[]{"a", "b", "c"}, step0);

		// 负步进 - 应该被设为1
		final String[] negativeStep = ArrayUtil.sub(array, 0, 3, -1);
		assertArrayEquals(new String[]{"a", "b", "c"}, negativeStep);

		// 步进为1 - 正常情况
		final String[] step1 = ArrayUtil.sub(array, 0, 3, 1);
		assertArrayEquals(new String[]{"a", "b", "c"}, step1);
	}

	@Test
	public void testSubGenericWithStep_reversedIndexes() {
		// 测试索引顺序颠倒的情况
		final Integer[] array = {1, 2, 3, 4, 5};

		// 正常顺序
		final Integer[] normal = ArrayUtil.sub(array, 1, 4, 1);
		assertArrayEquals(new Integer[]{2, 3, 4}, normal);

		// 索引颠倒 - 应该自动纠正为升序
		final Integer[] reversed = ArrayUtil.sub(array, 4, 1, 1);
		assertArrayEquals(new Integer[]{2, 3, 4}, reversed);

		// 步进为2的颠倒索引
		final Integer[] reversedStep2 = ArrayUtil.sub(array, 4, 1, 2);
		assertArrayEquals(new Integer[]{2, 4}, reversedStep2);
	}

	@Test
	public void testSubGenericWithStep_emptyAndSmallArrays() {
		// 测试空数组和小数组
		final String[] emptyArray = {};

		// 空数组的各种步进
		final String[] emptyResult1 = ArrayUtil.sub(emptyArray, 0, 0, 1);
		assertArrayEquals(new String[]{}, emptyResult1);

		final String[] emptyResult2 = ArrayUtil.sub(emptyArray, 0, 5, 2);
		assertArrayEquals(new String[]{}, emptyResult2);

		// 单元素数组
		final String[] singleArray = {"only"};
		final String[] singleResult1 = ArrayUtil.sub(singleArray, 0, 1, 1);
		assertArrayEquals(new String[]{"only"}, singleResult1);

		final String[] singleResult2 = ArrayUtil.sub(singleArray, 0, 1, 2);
		assertArrayEquals(new String[]{"only"}, singleResult2);

		// 双元素数组
		final String[] doubleArray = {"first", "second"};
		final String[] doubleResult1 = ArrayUtil.sub(doubleArray, 0, 2, 1);
		assertArrayEquals(new String[]{"first", "second"}, doubleResult1);

		final String[] doubleResult2 = ArrayUtil.sub(doubleArray, 0, 2, 2);
		assertArrayEquals(new String[]{"first"}, doubleResult2);
	}

	@Test
	public void testSubGeneric_nullInput() {
		// 测试空指针输入
		assertThrows(IllegalArgumentException.class, () -> {
			ArrayUtil.sub((String[]) null, 0, 1);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			ArrayUtil.sub((String[]) null, 0, 1, 1);
		});
	}

	// endregion
}
