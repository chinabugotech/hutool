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

package cn.hutool.v7.core.array;

import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.math.NumberUtil;
import cn.hutool.v7.core.util.ObjUtil;
import cn.hutool.v7.core.util.RandomUtil;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 原始类型数组工具类，原始类型数据包括：
 * <ol>
 *     <li>{@code int[]}</li>
 *     <li>{@code long[]}</li>
 *     <li>{@code double[]}</li>
 *     <li>{@code float[]}</li>
 *     <li>{@code short[]}</li>
 *     <li>{@code char[]}</li>
 *     <li>{@code byte[]}</li>
 *     <li>{@code boolean[]}</li>
 * </ol>
 *
 * @author Looly
 * @since 5.5.2
 */
public class PrimitiveArrayUtil {
	/**
	 * 数组中元素未找到的下标，值为-1
	 */
	public static final int INDEX_NOT_FOUND = -1;

	// region ----- isEmpty

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final long[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final int[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final short[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final char[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final byte[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final double[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final float[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final boolean[] array) {
		return array == null || array.length == 0;
	}
	// endregion

	// region ----- isNotEmpty

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final long[] array) {
		return !isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final int[] array) {
		return !isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final short[] array) {
		return !isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final char[] array) {
		return !isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final byte[] array) {
		return !isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final double[] array) {
		return !isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final float[] array) {
		return !isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final boolean[] array) {
		return !isEmpty(array);
	}
	// endregion

	// region ----- resize

	/**
	 * 生成一个新的重新设置大小的数组<br>
	 * 调整大小后拷贝原数组到新数组下。扩大则占位前N个位置，其它位置补充0，缩小则截断
	 *
	 * @param bytes   原数组
	 * @param newSize 新的数组大小
	 * @return 调整后的新数组
	 * @since 4.6.7
	 */
	public static byte[] resize(final byte[] bytes, final int newSize) {
		if (newSize < 0) {
			return bytes;
		}
		final byte[] newArray = new byte[newSize];
		if (newSize > 0 && isNotEmpty(bytes)) {
			System.arraycopy(bytes, 0, newArray, 0, Math.min(bytes.length, newSize));
		}
		return newArray;
	}
	// endregion

	// region ----- addAll

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static byte[] addAll(final byte[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final byte[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}

		final byte[] result = new byte[length];
		length = 0;
		for (final byte[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static int[] addAll(final int[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final int[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}

		final int[] result = new int[length];
		length = 0;
		for (final int[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static long[] addAll(final long[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final long[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}

		final long[] result = new long[length];
		length = 0;
		for (final long[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static double[] addAll(final double[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final double[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}

		final double[] result = new double[length];
		length = 0;
		for (final double[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static float[] addAll(final float[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final float[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}

		final float[] result = new float[length];
		length = 0;
		for (final float[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static char[] addAll(final char[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final char[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}

		final char[] result = new char[length];
		length = 0;
		for (final char[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static boolean[] addAll(final boolean[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final boolean[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}

		final boolean[] result = new boolean[length];
		length = 0;
		for (final boolean[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static short[] addAll(final short[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final short[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}

		final short[] result = new short[length];
		length = 0;
		for (final short[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}
	// endregion

	// region ----- split

	/**
	 * 拆分byte数组为几个等份（最后一份按照剩余长度分配空间）
	 *
	 * @param array 数组
	 * @param len   每个小节的长度
	 * @return 拆分后的数组
	 */
	public static byte[][] split(final byte[] array, final int len) {
		final int amount = array.length / len;
		final int remainder = array.length % len;
		// 兼容切片长度大于原数组长度的情况
		final boolean hasRemainder = remainder > 0;
		final byte[][] arrays = new byte[hasRemainder ? (amount + 1) : amount][];
		byte[] arr;
		int start = 0;
		for (int i = 0; i < amount; i++) {
			arr = new byte[len];
			System.arraycopy(array, start, arr, 0, len);
			arrays[i] = arr;
			start += len;
		}
		if (hasRemainder) {
			// 有剩余，按照实际长度创建
			arr = new byte[remainder];
			System.arraycopy(array, start, arr, 0, remainder);
			arrays[amount] = arr;
		}
		return arrays;
	}
	// endregion

	// region ----- indexOf、LastIndexOf、contains

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(final long[] array, final long value) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(final long[] array, final long value) {
		if (isNotEmpty(array)) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(final long[] array, final long value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(final int[] array, final int value) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(final int[] array, final int value) {
		if (isNotEmpty(array)) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(final int[] array, final int value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(final short[] array, final short value) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(final short[] array, final short value) {
		if (isNotEmpty(array)) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(final short[] array, final short value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(final char[] array, final char value) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(final char[] array, final char value) {
		if (isNotEmpty(array)) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(final char[] array, final char value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(final byte[] array, final byte value) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(final byte[] array, final byte value) {
		if (isNotEmpty(array)) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(final byte[] array, final byte value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(final double[] array, final double value) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (NumberUtil.equals(value, array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(final double[] array, final double value) {
		if (isNotEmpty(array)) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (NumberUtil.equals(value, array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(final double[] array, final double value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(final float[] array, final float value) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (NumberUtil.equals(value, array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(final float[] array, final float value) {
		if (isNotEmpty(array)) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (NumberUtil.equals(value, array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(final float[] array, final float value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(final boolean[] array, final boolean value) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(final boolean[] array, final boolean value) {
		if (isNotEmpty(array)) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(final boolean[] array, final boolean value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}
	// endregion

	// region ----- Wrap and unwrap

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Integer[] wrap(final int... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Integer[0];
		}

		final Integer[] array = new Integer[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组，null转为0
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static int[] unWrap(final Integer... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new int[0];
		}

		final int[] array = new int[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], 0);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Long[] wrap(final long... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Long[0];
		}

		final Long[] array = new Long[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static long[] unWrap(final Long... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new long[0];
		}

		final long[] array = new long[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], 0L);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Character[] wrap(final char... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Character[0];
		}

		final Character[] array = new Character[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static char[] unWrap(final Character... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new char[0];
		}

		final char[] array = new char[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], Character.MIN_VALUE);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Byte[] wrap(final byte... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Byte[0];
		}

		final Byte[] array = new Byte[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static byte[] unWrap(final Byte... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new byte[0];
		}

		final byte[] array = new byte[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], (byte) 0);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Short[] wrap(final short... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Short[0];
		}

		final Short[] array = new Short[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static short[] unWrap(final Short... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new short[0];
		}

		final short[] array = new short[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], (short) 0);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Float[] wrap(final float... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Float[0];
		}

		final Float[] array = new Float[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static float[] unWrap(final Float... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new float[0];
		}

		final float[] array = new float[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], 0F);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Double[] wrap(final double... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Double[0];
		}

		final Double[] array = new Double[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static double[] unWrap(final Double... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new double[0];
		}

		final double[] array = new double[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], 0D);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Boolean[] wrap(final boolean... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Boolean[0];
		}

		final Boolean[] array = new Boolean[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组<br>
	 * {@code null} 按照 {@code false} 对待
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static boolean[] unWrap(final Boolean... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new boolean[0];
		}

		final boolean[] array = new boolean[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], false);
		}
		return array;
	}
	// endregion

	// region ----- sub

	/**
	 * 获取子数组
	 * <ul>
	 *     <li>位置可以为负数，例如 -1 代表 数组最后一个元素的位置</li>
	 *     <li>如果 开始位置 大于 结束位置，会自动交换</li>
	 *     <li>如果 结束位置 大于 数组长度，会变为数组长度</li>
	 * </ul>
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static byte[] sub(final byte[] array, int start, int end) {
		Assert.notNull(array, "array must be not null !");
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (start >= length) {
			return new byte[0];
		}
		if (end > length) {
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 * <ul>
	 *     <li>位置可以为负数，例如 -1 代表 数组最后一个元素的位置</li>
	 *     <li>如果 开始位置 大于 结束位置，会自动交换</li>
	 *     <li>如果 结束位置 大于 数组长度，会变为数组长度</li>
	 * </ul>
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static int[] sub(final int[] array, int start, int end) {
		Assert.notNull(array, "array must be not null !");
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (start >= length) {
			return new int[0];
		}
		if (end > length) {
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 * <ul>
	 *     <li>位置可以为负数，例如 -1 代表 数组最后一个元素的位置</li>
	 *     <li>如果 开始位置 大于 结束位置，会自动交换</li>
	 *     <li>如果 结束位置 大于 数组长度，会变为数组长度</li>
	 * </ul>
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static long[] sub(final long[] array, int start, int end) {
		Assert.notNull(array, "array must be not null !");
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (start >= length) {
			return new long[0];
		}
		if (end > length) {
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 * <ul>
	 *     <li>位置可以为负数，例如 -1 代表 数组最后一个元素的位置</li>
	 *     <li>如果 开始位置 大于 结束位置，会自动交换</li>
	 *     <li>如果 结束位置 大于 数组长度，会变为数组长度</li>
	 * </ul>
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static short[] sub(final short[] array, int start, int end) {
		Assert.notNull(array, "array must be not null !");
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (start >= length) {
			return new short[0];
		}
		if (end > length) {
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 * <ul>
	 *     <li>位置可以为负数，例如 -1 代表 数组最后一个元素的位置</li>
	 *     <li>如果 开始位置 大于 结束位置，会自动交换</li>
	 *     <li>如果 结束位置 大于 数组长度，会变为数组长度</li>
	 * </ul>
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static char[] sub(final char[] array, int start, int end) {
		Assert.notNull(array, "array must be not null !");
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (start >= length) {
			return new char[0];
		}
		if (end > length) {
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 * <ul>
	 *     <li>位置可以为负数，例如 -1 代表 数组最后一个元素的位置</li>
	 *     <li>如果 开始位置 大于 结束位置，会自动交换</li>
	 *     <li>如果 结束位置 大于 数组长度，会变为数组长度</li>
	 * </ul>
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static double[] sub(final double[] array, int start, int end) {
		Assert.notNull(array, "array must be not null !");
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (start >= length) {
			return new double[0];
		}
		if (end > length) {
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 * <ul>
	 *     <li>位置可以为负数，例如 -1 代表 数组最后一个元素的位置</li>
	 *     <li>如果 开始位置 大于 结束位置，会自动交换</li>
	 *     <li>如果 结束位置 大于 数组长度，会变为数组长度</li>
	 * </ul>
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static float[] sub(final float[] array, int start, int end) {
		Assert.notNull(array, "array must be not null !");
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (start >= length) {
			return new float[0];
		}
		if (end > length) {
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 * <ul>
	 *     <li>位置可以为负数，例如 -1 代表 数组最后一个元素的位置</li>
	 *     <li>如果 开始位置 大于 结束位置，会自动交换</li>
	 *     <li>如果 结束位置 大于 数组长度，会变为数组长度</li>
	 * </ul>
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static boolean[] sub(final boolean[] array, int start, int end) {
		Assert.notNull(array, "array must be not null !");
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (start >= length) {
			return new boolean[0];
		}
		if (end > length) {
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}
	// endregion

	// region ----- remove

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param array 数组对象
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static long[] remove(final long[] array, final int index) throws IllegalArgumentException {
		return (long[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param array 数组对象
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static int[] remove(final int[] array, final int index) throws IllegalArgumentException {
		return (int[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param array 数组对象
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static short[] remove(final short[] array, final int index) throws IllegalArgumentException {
		return (short[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param array 数组对象
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static char[] remove(final char[] array, final int index) throws IllegalArgumentException {
		return (char[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param array 数组对象
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static byte[] remove(final byte[] array, final int index) throws IllegalArgumentException {
		return (byte[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param array 数组对象
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static double[] remove(final double[] array, final int index) throws IllegalArgumentException {
		return (double[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param array 数组对象
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static float[] remove(final float[] array, final int index) throws IllegalArgumentException {
		return (float[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param array 数组对象
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static boolean[] remove(final boolean[] array, final int index) throws IllegalArgumentException {
		return (boolean[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	@SuppressWarnings("SuspiciousSystemArraycopy")
	public static Object remove(final Object array, final int index) throws IllegalArgumentException {
		if (null == array) {
			return null;
		}
		final int length = Array.getLength(array);
		if (index < 0 || index >= length) {
			return array;
		}

		final Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
		System.arraycopy(array, 0, result, 0, index);
		if (index < length - 1) {
			// 后半部分
			System.arraycopy(array, index + 1, result, index, length - index - 1);
		}

		return result;
	}
	// endregion

	// region ----- removeEle

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素<br>
	 * copy from commons-lang
	 *
	 * @param array   数组对象
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static long[] removeEle(final long[] array, final long element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素<br>
	 * copy from commons-lang
	 *
	 * @param array   数组对象
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static int[] removeEle(final int[] array, final int element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素<br>
	 * copy from commons-lang
	 *
	 * @param array   数组对象
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static short[] removeEle(final short[] array, final short element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素<br>
	 * copy from commons-lang
	 *
	 * @param array   数组对象
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static char[] removeEle(final char[] array, final char element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素<br>
	 * copy from commons-lang
	 *
	 * @param array   数组对象
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static byte[] removeEle(final byte[] array, final byte element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素<br>
	 * copy from commons-lang
	 *
	 * @param array   数组对象
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static double[] removeEle(final double[] array, final double element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素<br>
	 * copy from commons-lang
	 *
	 * @param array   数组对象
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static float[] removeEle(final float[] array, final float element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素<br>
	 * copy from commons-lang
	 *
	 * @param array   数组对象
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static boolean[] removeEle(final boolean[] array, final boolean element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}
	// endregion

	// region ----- removeElements

	/**
	 * 从字符数组中移除指定的多个字符
	 *
	 * @param array    原始字符数组
	 * @param elements 要移除的字符数组
	 * @return 移除指定字符后的新数组
	 * @since 7.0.0
	 */
	public static char[] removeElements(final char[] array, final char... elements) {
		if (isEmpty(array) || isEmpty(elements)) {
			return array;
		}

		return filter(array, t -> !ArrayUtil.contains(elements, t));
	}

	// ==================== byte 类型 ====================

	/**
	 * 从byte数组中移除指定的多个byte元素
	 *
	 * @param array    原始byte数组
	 * @param elements 要移除的byte数组
	 * @return 移除指定元素后的新数组
	 * @since 7.0.0
	 */
	public static byte[] removeElements(final byte[] array, final byte... elements) {
		if (isEmpty(array) || isEmpty(elements)) {
			return array;
		}

		return filter(array, t -> !contains(elements, t));
	}

	// ==================== short 类型 ====================

	/**
	 * 从short数组中移除指定的多个short元素
	 *
	 * @param array    原始short数组
	 * @param elements 要移除的short数组
	 * @return 移除指定元素后的新数组
	 * @since 7.0.0
	 */
	public static short[] removeElements(final short[] array, final short... elements) {
		if (isEmpty(array) || isEmpty(elements)) {
			return array;
		}

		return filter(array, t -> !contains(elements, t));
	}

	/**
	 * 从int数组中移除指定的多个int元素
	 *
	 * @param array    原始int数组
	 * @param elements 要移除的int数组
	 * @return 移除指定元素后的新数组
	 * @since 7.0.0
	 */
	public static int[] removeElements(final int[] array, final int... elements) {
		if (isEmpty(array) || isEmpty(elements)) {
			return array;
		}

		return filter(array, t -> !contains(elements, t));
	}

	/**
	 * 从long数组中移除指定的多个long元素
	 *
	 * @param array    原始long数组
	 * @param elements 要移除的long数组
	 * @return 移除指定元素后的新数组
	 * @since 7.0.0
	 */
	public static long[] removeElements(final long[] array, final long... elements) {
		if (isEmpty(array) || isEmpty(elements)) {
			return array;
		}

		return filter(array, t -> !contains(elements, t));
	}

	/**
	 * 从float数组中移除指定的多个float元素
	 *
	 * @param array    原始float数组
	 * @param elements 要移除的float数组
	 * @return 移除指定元素后的新数组
	 * @since 7.0.0
	 */
	public static float[] removeElements(final float[] array, final float... elements) {
		if (isEmpty(array) || isEmpty(elements)) {
			return array;
		}

		return filter(array, t -> !contains(elements, t));
	}

	/**
	 * 从double数组中移除指定的多个double元素
	 *
	 * @param array    原始double数组
	 * @param elements 要移除的double数组
	 * @return 移除指定元素后的新数组
	 * @since 7.0.0
	 */
	public static double[] removeElements(final double[] array, final double... elements) {
		if (isEmpty(array) || isEmpty(elements)) {
			return array;
		}

		return filter(array, t -> !contains(elements, t));
	}

	/**
	 * 从boolean数组中移除指定的多个boolean元素
	 *
	 * @param array    原始boolean数组
	 * @param elements 要移除的boolean数组
	 * @return 移除指定元素后的新数组
	 * @since 7.0.0
	 */
	public static boolean[] removeElements(final boolean[] array, final boolean... elements) {
		if (isEmpty(array) || isEmpty(elements)) {
			return array;
		}

		return filter(array, t -> !contains(elements, t));
	}
	// endregion

	// region ----- edit and filter

	/**
	 * 对每个数组元素执行指定操作，返回操作后的元素<br>
	 * 这个Editor实现可以实现以下功能：
	 * <ol>
	 *     <li>过滤出需要的对象，如果返回{@code null}则抛弃这个元素对象</li>
	 *     <li>修改元素对象，返回修改后的对象</li>
	 * </ol>
	 *
	 * @param array  数组
	 * @param editor 编辑器接口，为 {@code null}则返回原数组
	 * @return 编辑后的数组
	 * @since 7.0.0
	 */
	public static char[] edit(final char[] array, final UnaryOperator<Character> editor) {
		if (null == array || null == editor) {
			return array;
		}

		final List<Character> resultList = new ArrayList<>(array.length);
		Character modified;
		for (final Character t : array) {
			modified = editor.apply(t);
			if (null != modified) {
				resultList.add(modified);
			}
		}

		// 将List转换为char[]数组
		final int size = resultList.size();
		final char[] resultArray = new char[size];
		for (int i = 0; i < size; i++) {
			resultArray[i] = resultList.get(i);
		}

		return resultArray;
	}

	/**
	 * 对每个byte数组元素执行指定操作，返回操作后的元素<br>
	 * 这个Editor实现可以实现以下功能：
	 * <ol>
	 *     <li>过滤出需要的对象，如果返回{@code null}则抛弃这个元素对象</li>
	 *     <li>修改元素对象，返回修改后的对象</li>
	 * </ol>
	 *
	 * @param array  数组
	 * @param editor 编辑器接口，为 {@code null}则返回原数组
	 * @return 编辑后的数组
	 * @since 7.0.0
	 */
	public static byte[] edit(final byte[] array, final UnaryOperator<Byte> editor) {
		if (null == array || null == editor) {
			return array;
		}

		final List<Byte> resultList = new ArrayList<>(array.length);
		Byte modified;
		for (final Byte t : array) {
			modified = editor.apply(t);
			if (null != modified) {
				resultList.add(modified);
			}
		}

		// 将List转换为byte[]数组
		final int size = resultList.size();
		final byte[] resultArray = new byte[size];
		for (int i = 0; i < size; i++) {
			resultArray[i] = resultList.get(i);
		}

		return resultArray;
	}

	/**
	 * 对每个short数组元素执行指定操作，返回操作后的元素<br>
	 * 这个Editor实现可以实现以下功能：
	 * <ol>
	 *     <li>过滤出需要的对象，如果返回{@code null}则抛弃这个元素对象</li>
	 *     <li>修改元素对象，返回修改后的对象</li>
	 * </ol>
	 *
	 * @param array  数组
	 * @param editor 编辑器接口，为 {@code null}则返回原数组
	 * @return 编辑后的数组
	 * @since 7.0.0
	 */
	public static short[] edit(final short[] array, final UnaryOperator<Short> editor) {
		if (null == array || null == editor) {
			return array;
		}

		final List<Short> resultList = new ArrayList<>(array.length);
		Short modified;
		for (final Short t : array) {
			modified = editor.apply(t);
			if (null != modified) {
				resultList.add(modified);
			}
		}

		// 将List转换为short[]数组
		final int size = resultList.size();
		final short[] resultArray = new short[size];
		for (int i = 0; i < size; i++) {
			resultArray[i] = resultList.get(i);
		}

		return resultArray;
	}

	/**
	 * 对每个int数组元素执行指定操作，返回操作后的元素<br>
	 * 这个Editor实现可以实现以下功能：
	 * <ol>
	 *     <li>过滤出需要的对象，如果返回{@code null}则抛弃这个元素对象</li>
	 *     <li>修改元素对象，返回修改后的对象</li>
	 * </ol>
	 *
	 * @param array  数组
	 * @param editor 编辑器接口，为 {@code null}则返回原数组
	 * @return 编辑后的数组
	 * @since 7.0.0
	 */
	public static int[] edit(final int[] array, final UnaryOperator<Integer> editor) {
		if (null == array || null == editor) {
			return array;
		}

		final List<Integer> resultList = new ArrayList<>(array.length);
		Integer modified;
		for (final Integer t : array) {
			modified = editor.apply(t);
			if (null != modified) {
				resultList.add(modified);
			}
		}

		// 将List转换为int[]数组
		final int size = resultList.size();
		final int[] resultArray = new int[size];
		for (int i = 0; i < size; i++) {
			resultArray[i] = resultList.get(i);
		}

		return resultArray;
	}

	/**
	 * 对每个long数组元素执行指定操作，返回操作后的元素<br>
	 * 这个Editor实现可以实现以下功能：
	 * <ol>
	 *     <li>过滤出需要的对象，如果返回{@code null}则抛弃这个元素对象</li>
	 *     <li>修改元素对象，返回修改后的对象</li>
	 * </ol>
	 *
	 * @param array  数组
	 * @param editor 编辑器接口，为 {@code null}则返回原数组
	 * @return 编辑后的数组
	 * @since 7.0.0
	 */
	public static long[] edit(final long[] array, final UnaryOperator<Long> editor) {
		if (null == array || null == editor) {
			return array;
		}

		final List<Long> resultList = new ArrayList<>(array.length);
		Long modified;
		for (final Long t : array) {
			modified = editor.apply(t);
			if (null != modified) {
				resultList.add(modified);
			}
		}

		// 将List转换为long[]数组
		final int size = resultList.size();
		final long[] resultArray = new long[size];
		for (int i = 0; i < size; i++) {
			resultArray[i] = resultList.get(i);
		}

		return resultArray;
	}

	/**
	 * 对每个float数组元素执行指定操作，返回操作后的元素<br>
	 * 这个Editor实现可以实现以下功能：
	 * <ol>
	 *     <li>过滤出需要的对象，如果返回{@code null}则抛弃这个元素对象</li>
	 *     <li>修改元素对象，返回修改后的对象</li>
	 * </ol>
	 *
	 * @param array  数组
	 * @param editor 编辑器接口，为 {@code null}则返回原数组
	 * @return 编辑后的数组
	 * @since 7.0.0
	 */
	public static float[] edit(final float[] array, final UnaryOperator<Float> editor) {
		if (null == array || null == editor) {
			return array;
		}

		final List<Float> resultList = new ArrayList<>(array.length);
		Float modified;
		for (final Float t : array) {
			modified = editor.apply(t);
			if (null != modified) {
				resultList.add(modified);
			}
		}

		// 将List转换为float[]数组
		final int size = resultList.size();
		final float[] resultArray = new float[size];
		for (int i = 0; i < size; i++) {
			resultArray[i] = resultList.get(i);
		}

		return resultArray;
	}

	/**
	 * 对每个double数组元素执行指定操作，返回操作后的元素<br>
	 * 这个Editor实现可以实现以下功能：
	 * <ol>
	 *     <li>过滤出需要的对象，如果返回{@code null}则抛弃这个元素对象</li>
	 *     <li>修改元素对象，返回修改后的对象</li>
	 * </ol>
	 *
	 * @param array  数组
	 * @param editor 编辑器接口，为 {@code null}则返回原数组
	 * @return 编辑后的数组
	 * @since 7.0.0
	 */
	public static double[] edit(final double[] array, final UnaryOperator<Double> editor) {
		if (null == array || null == editor) {
			return array;
		}

		final List<Double> resultList = new ArrayList<>(array.length);
		Double modified;
		for (final Double t : array) {
			modified = editor.apply(t);
			if (null != modified) {
				resultList.add(modified);
			}
		}

		// 将List转换为double[]数组
		final int size = resultList.size();
		final double[] resultArray = new double[size];
		for (int i = 0; i < size; i++) {
			resultArray[i] = resultList.get(i);
		}

		return resultArray;
	}

	/**
	 * 对每个boolean数组元素执行指定操作，返回操作后的元素<br>
	 * 这个Editor实现可以实现以下功能：
	 * <ol>
	 *     <li>过滤出需要的对象，如果返回{@code null}则抛弃这个元素对象</li>
	 *     <li>修改元素对象，返回修改后的对象</li>
	 * </ol>
	 *
	 * @param array  数组
	 * @param editor 编辑器接口，为 {@code null}则返回原数组
	 * @return 编辑后的数组
	 * @since 7.0.0
	 */
	public static boolean[] edit(final boolean[] array, final UnaryOperator<Boolean> editor) {
		if (null == array || null == editor) {
			return array;
		}

		final List<Boolean> resultList = new ArrayList<>(array.length);
		Boolean modified;
		for (final Boolean t : array) {
			modified = editor.apply(t);
			if (null != modified) {
				resultList.add(modified);
			}
		}

		// 将List转换为boolean[]数组
		final int size = resultList.size();
		final boolean[] resultArray = new boolean[size];
		for (int i = 0; i < size; i++) {
			resultArray[i] = resultList.get(i);
		}

		return resultArray;
	}

	/**
	 * 过滤数组元素<br>
	 * 保留 {@link Predicate#test(Object)}为{@code true}的元素
	 *
	 * @param array     数组
	 * @param predicate 过滤器接口，用于定义过滤规则，为{@code null}则返回原数组
	 * @return 过滤后的数组
	 * @since 7.0.0
	 */
	public static char[] filter(final char[] array, final Predicate<Character> predicate) {
		if (null == array || null == predicate) {
			return array;
		}
		return edit(array, t -> predicate.test(t) ? t : null);
	}

	/**
	 * 过滤byte数组元素<br>
	 * 保留 {@link Predicate#test(Object)}为{@code true}的元素
	 *
	 * @param array     数组
	 * @param predicate 过滤器接口，用于定义过滤规则，为{@code null}则返回原数组
	 * @return 过滤后的数组
	 * @since 7.0.0
	 */
	public static byte[] filter(final byte[] array, final Predicate<Byte> predicate) {
		if (null == array || null == predicate) {
			return array;
		}
		return edit(array, t -> predicate.test(t) ? t : null);
	}

	/**
	 * 过滤short数组元素<br>
	 * 保留 {@link Predicate#test(Object)}为{@code true}的元素
	 *
	 * @param array     数组
	 * @param predicate 过滤器接口，用于定义过滤规则，为{@code null}则返回原数组
	 * @return 过滤后的数组
	 * @since 7.0.0
	 */
	public static short[] filter(final short[] array, final Predicate<Short> predicate) {
		if (null == array || null == predicate) {
			return array;
		}
		return edit(array, t -> predicate.test(t) ? t : null);
	}

	/**
	 * 过滤int数组元素<br>
	 * 保留 {@link Predicate#test(Object)}为{@code true}的元素
	 *
	 * @param array     数组
	 * @param predicate 过滤器接口，用于定义过滤规则，为{@code null}则返回原数组
	 * @return 过滤后的数组
	 * @since 7.0.0
	 */
	public static int[] filter(final int[] array, final Predicate<Integer> predicate) {
		if (null == array || null == predicate) {
			return array;
		}
		return edit(array, t -> predicate.test(t) ? t : null);
	}

	/**
	 * 过滤long数组元素<br>
	 * 保留 {@link Predicate#test(Object)}为{@code true}的元素
	 *
	 * @param array     数组
	 * @param predicate 过滤器接口，用于定义过滤规则，为{@code null}则返回原数组
	 * @return 过滤后的数组
	 * @since 7.0.0
	 */
	public static long[] filter(final long[] array, final Predicate<Long> predicate) {
		if (null == array || null == predicate) {
			return array;
		}
		return edit(array, t -> predicate.test(t) ? t : null);
	}

	/**
	 * 过滤float数组元素<br>
	 * 保留 {@link Predicate#test(Object)}为{@code true}的元素
	 *
	 * @param array     数组
	 * @param predicate 过滤器接口，用于定义过滤规则，为{@code null}则返回原数组
	 * @return 过滤后的数组
	 * @since 7.0.0
	 */
	public static float[] filter(final float[] array, final Predicate<Float> predicate) {
		if (null == array || null == predicate) {
			return array;
		}
		return edit(array, t -> predicate.test(t) ? t : null);
	}

	/**
	 * 过滤double数组元素<br>
	 * 保留 {@link Predicate#test(Object)}为{@code true}的元素
	 *
	 * @param array     数组
	 * @param predicate 过滤器接口，用于定义过滤规则，为{@code null}则返回原数组
	 * @return 过滤后的数组
	 * @since 7.0.0
	 */
	public static double[] filter(final double[] array, final Predicate<Double> predicate) {
		if (null == array || null == predicate) {
			return array;
		}
		return edit(array, t -> predicate.test(t) ? t : null);
	}

	/**
	 * 过滤boolean数组元素<br>
	 * 保留 {@link Predicate#test(Object)}为{@code true}的元素
	 *
	 * @param array     数组
	 * @param predicate 过滤器接口，用于定义过滤规则，为{@code null}则返回原数组
	 * @return 过滤后的数组
	 * @since 7.0.0
	 */
	public static boolean[] filter(final boolean[] array, final Predicate<Boolean> predicate) {
		if (null == array || null == predicate) {
			return array;
		}
		return edit(array, t -> predicate.test(t) ? t : null);
	}
	// endregion

	// region ----- reverse

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array               数组，会变更
	 * @param startIndexInclusive 起始位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static long[] reverse(final long[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static long[] reverse(final long[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array               数组，会变更
	 * @param startIndexInclusive 起始位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static int[] reverse(final int[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static int[] reverse(final int[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array               数组，会变更
	 * @param startIndexInclusive 起始位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static short[] reverse(final short[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static short[] reverse(final short[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array               数组，会变更
	 * @param startIndexInclusive 起始位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static char[] reverse(final char[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static char[] reverse(final char[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array               数组，会变更
	 * @param startIndexInclusive 起始位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static byte[] reverse(final byte[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static byte[] reverse(final byte[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array               数组，会变更
	 * @param startIndexInclusive 起始位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static double[] reverse(final double[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static double[] reverse(final double[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array               数组，会变更
	 * @param startIndexInclusive 起始位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static float[] reverse(final float[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static float[] reverse(final float[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array               数组，会变更
	 * @param startIndexInclusive 起始位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static boolean[] reverse(final boolean[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static boolean[] reverse(final boolean[] array) {
		return reverse(array, 0, array.length);
	}
	// endregion

	// region ----- min and max

	/**
	 * 取最小值
	 *
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static long min(final long... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		long min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 *
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static int min(final int... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		int min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 *
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static short min(final short... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		short min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 *
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static char min(final char... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		char min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 *
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static byte min(final byte... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		byte min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 *
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static double min(final double... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		double min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 *
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static float min(final float... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		float min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最大值
	 *
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static long max(final long... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		long max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 *
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static int max(final int... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		int max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 *
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static short max(final short... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		short max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 *
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static char max(final char... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		char max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 *
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static byte max(final byte... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		byte max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 *
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static double max(final double... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		double max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 *
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static float max(final float... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		float max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}
	// endregion

	// region ----- shuffle

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static int[] shuffle(final int[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static int[] shuffle(final int[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static long[] shuffle(final long[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static long[] shuffle(final long[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static double[] shuffle(final double[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static double[] shuffle(final double[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static float[] shuffle(final float[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static float[] shuffle(final float[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean[] shuffle(final boolean[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean[] shuffle(final boolean[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static byte[] shuffle(final byte[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static byte[] shuffle(final byte[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static char[] shuffle(final char[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static char[] shuffle(final char[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static short[] shuffle(final short[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static short[] shuffle(final short[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}
	// endregion

	// region ----- swap

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static int[] swap(final int[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final int tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static long[] swap(final long[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final long tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static double[] swap(final double[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final double tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static float[] swap(final float[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final float tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static boolean[] swap(final boolean[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final boolean tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static byte[] swap(final byte[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final byte tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static char[] swap(final char[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final char tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static short[] swap(final short[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final short tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	// region ----- asc and desc

	/**
	 * 检查数组是否升序，即{@code array[i] <= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final byte[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即{@code array[i] >= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final byte[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即{@code array[i] <= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final short[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即{@code array[i] >= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final short[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即{@code array[i] <= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final char[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即{@code array[i] >= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final char[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即{@code array[i] <= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final int[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即{@code array[i] >= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final int[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即{@code array[i] <= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final long[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即{@code array[i] >= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final long[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即{@code array[i] <= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final double[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即{@code array[i] >= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final double[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即{@code array[i] <= array[i+1]}<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final float[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即array[i] &gt;= array[i+1]<br>
	 * 若传入空数组，则返回{@code false}<br>
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final float[] array) {
		if (isEmpty(array)) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}
		return true;
	}
	// endregion

	// region ----- startWith

	/**
	 * array数组是否以prefix开头
	 * <ul>
	 *     <li>array和prefix为同一个数组（即array == prefix），返回{@code true}</li>
	 *     <li>array或prefix为空数组（null或length为0的数组），返回{@code true}</li>
	 *     <li>prefix长度大于array，返回{@code false}</li>
	 * </ul>
	 *
	 * @param array  数组
	 * @param prefix 前缀
	 * @return 是否开头
	 */
	public static boolean startWith(final boolean[] array, final boolean... prefix) {
		if (array == prefix) {
			return true;
		}
		if (isEmpty(array)) {
			return isEmpty(prefix);
		}
		if (prefix.length > array.length) {
			return false;
		}

		for (int i = 0; i < prefix.length; i++) {
			if (array[i] != prefix[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * array数组是否以prefix开头
	 * <ul>
	 *     <li>array和prefix为同一个数组（即array == prefix），返回{@code true}</li>
	 *     <li>array或prefix为空数组（null或length为0的数组），返回{@code true}</li>
	 *     <li>prefix长度大于array，返回{@code false}</li>
	 * </ul>
	 *
	 * @param array  数组
	 * @param prefix 前缀
	 * @return 是否开头
	 */
	public static boolean startWith(final byte[] array, final byte... prefix) {
		if (array == prefix) {
			return true;
		}
		if (isEmpty(array)) {
			return isEmpty(prefix);
		}
		if (prefix.length > array.length) {
			return false;
		}

		return isSubEquals(array, 0, prefix);
	}

	/**
	 * array数组是否以prefix开头
	 * <ul>
	 *     <li>array和prefix为同一个数组（即array == prefix），返回{@code true}</li>
	 *     <li>array或prefix为空数组（null或length为0的数组），返回{@code true}</li>
	 *     <li>prefix长度大于array，返回{@code false}</li>
	 * </ul>
	 *
	 * @param array  数组
	 * @param prefix 前缀
	 * @return 是否开头
	 */
	public static boolean startWith(final char[] array, final char... prefix) {
		if (array == prefix) {
			return true;
		}
		if (isEmpty(array)) {
			return isEmpty(prefix);
		}
		if (prefix.length > array.length) {
			return false;
		}

		for (int i = 0; i < prefix.length; i++) {
			if (array[i] != prefix[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * array数组是否以prefix开头
	 * <ul>
	 *     <li>array和prefix为同一个数组（即array == prefix），返回{@code true}</li>
	 *     <li>array或prefix为空数组（null或length为0的数组），返回{@code true}</li>
	 *     <li>prefix长度大于array，返回{@code false}</li>
	 * </ul>
	 *
	 * @param array  数组
	 * @param prefix 前缀
	 * @return 是否开头
	 */
	public static boolean startWith(final double[] array, final double... prefix) {
		if (array == prefix) {
			return true;
		}
		if (isEmpty(array)) {
			return isEmpty(prefix);
		}
		if (prefix.length > array.length) {
			return false;
		}

		for (int i = 0; i < prefix.length; i++) {
			if (array[i] != prefix[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * array数组是否以prefix开头
	 * <ul>
	 *     <li>array和prefix为同一个数组（即array == prefix），返回{@code true}</li>
	 *     <li>array或prefix为空数组（null或length为0的数组），返回{@code true}</li>
	 *     <li>prefix长度大于array，返回{@code false}</li>
	 * </ul>
	 *
	 * @param array  数组
	 * @param prefix 前缀
	 * @return 是否开头
	 */
	public static boolean startWith(final float[] array, final float... prefix) {
		if (array == prefix) {
			return true;
		}
		if (isEmpty(array)) {
			return isEmpty(prefix);
		}
		if (prefix.length > array.length) {
			return false;
		}

		for (int i = 0; i < prefix.length; i++) {
			if (array[i] != prefix[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * array数组是否以prefix开头
	 * <ul>
	 *     <li>array和prefix为同一个数组（即array == prefix），返回{@code true}</li>
	 *     <li>array或prefix为空数组（null或length为0的数组），返回{@code true}</li>
	 *     <li>prefix长度大于array，返回{@code false}</li>
	 * </ul>
	 *
	 * @param array  数组
	 * @param prefix 前缀
	 * @return 是否开头
	 */
	public static boolean startWith(final int[] array, final int... prefix) {
		if (array == prefix) {
			return true;
		}
		if (isEmpty(array)) {
			return isEmpty(prefix);
		}
		if (prefix.length > array.length) {
			return false;
		}

		for (int i = 0; i < prefix.length; i++) {
			if (array[i] != prefix[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * array数组是否以prefix开头
	 * <ul>
	 *     <li>array和prefix为同一个数组（即array == prefix），返回{@code true}</li>
	 *     <li>array或prefix为空数组（null或length为0的数组），返回{@code true}</li>
	 *     <li>prefix长度大于array，返回{@code false}</li>
	 * </ul>
	 *
	 * @param array  数组
	 * @param prefix 前缀
	 * @return 是否开头
	 */
	public static boolean startWith(final long[] array, final long... prefix) {
		if (array == prefix) {
			return true;
		}
		if (isEmpty(array)) {
			return isEmpty(prefix);
		}
		if (prefix.length > array.length) {
			return false;
		}

		for (int i = 0; i < prefix.length; i++) {
			if (array[i] != prefix[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * array数组是否以prefix开头
	 * <ul>
	 *     <li>array和prefix为同一个数组（即array == prefix），返回{@code true}</li>
	 *     <li>array或prefix为空数组（null或length为0的数组），返回{@code true}</li>
	 *     <li>prefix长度大于array，返回{@code false}</li>
	 * </ul>
	 *
	 * @param array  数组
	 * @param prefix 前缀
	 * @return 是否开头
	 */
	public static boolean startWith(final short[] array, final short... prefix) {
		if (array == prefix) {
			return true;
		}
		if (isEmpty(array)) {
			return isEmpty(prefix);
		}
		if (prefix.length > array.length) {
			return false;
		}

		for (int i = 0; i < prefix.length; i++) {
			if (array[i] != prefix[i]) {
				return false;
			}
		}
		return true;
	}
	// endregion

	// region rangeMatches

	/**
	 * 是否局部匹配，相当于对比以下子串是否相等
	 * <pre>
	 *     array1[offset1, subArray.length]
	 *                  ||
	 *               subArray
	 * </pre>
	 *
	 * @param array    数组
	 * @param offset   开始位置
	 * @param subArray 子数组
	 * @return 是否局部匹配
	 */
	public static boolean isSubEquals(final byte[] array, final int offset, final byte... subArray) {
		if (array == subArray) {
			return true;
		}
		if (array.length < subArray.length) {
			return false;
		}
		return regionMatches(array, offset, subArray, 0, subArray.length);
	}

	/**
	 * 是否局部匹配，相当于对比以下子串是否相等
	 * <pre>
	 *     array1[offset1 : offset1 + length]
	 *                  ||
	 *     array2[offset2 : offset2 + length]
	 * </pre>
	 *
	 * @param array1  第一个数组
	 * @param offset1 第一个数组开始位置
	 * @param array2  第二个数组
	 * @param offset2 第二个数组开始位置
	 * @param length  检查长度
	 * @return 是否局部匹配
	 */
	public static boolean regionMatches(final byte[] array1, final int offset1,
										final byte[] array2, final int offset2, final int length) {
		if (array1.length < offset1 + length) {
			throw new IndexOutOfBoundsException("[byte1] length must be >= [offset1 + length]");
		}
		if (array2.length < offset2 + length) {
			throw new IndexOutOfBoundsException("[byte2] length must be >= [offset2 + length]");
		}

		for (int i = 0; i < length; i++) {
			if (array1[i + offset1] != array2[i + offset2]) {
				return false;
			}
		}
		return true;
	}
}
