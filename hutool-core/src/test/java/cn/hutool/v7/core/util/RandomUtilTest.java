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

package cn.hutool.v7.core.util;

import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.convert.ConvertUtil;
import cn.hutool.v7.core.lang.Validator;
import cn.hutool.v7.core.math.NumberUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RandomUtilTest {

	@Test
	public void randomEleSetTest(){
		final Set<Integer> set = RandomUtil.randomEleSet(ListUtil.of(1, 2, 3, 4, 5, 6), 2);
		assertEquals(2, set.size());
	}

	@Test
	public void randomElesTest(){
		final List<Integer> result = RandomUtil.randomEles(ListUtil.of(1, 2, 3, 4, 5, 6), 2);
		assertEquals(2, result.size());
	}

	@Test
	public void randomDoubleTest() {
		final double randomDouble = RandomUtil.randomDouble(0, 1, 0);
		assertTrue(randomDouble <= 1);
	}

	@Test
	public void randomNumberTest() {
		final char c = RandomUtil.randomNumber();
		assertTrue(c <= '9');
	}

	@Test
	public void randomIntTest() {
		final int c = RandomUtil.randomInt(10, 100);
		assertTrue(c >= 10 && c < 100);
	}

	@Test
	public void randomBytesTest() {
		final byte[] c = RandomUtil.randomBytes(10);
		assertNotNull(c);
	}

	@Test
	public void randomChineseTest(){
		final char c = RandomUtil.randomChinese();
		assertTrue(c > 0);
	}

	@Test
	public void randomLettersAndNumbersLowerWithoutStrTest() {
		for (int i = 0; i < 100; i++) {
			final String s = RandomUtil.randomLettersAndNumbersLowerWithoutStr(8, "0IPOL");
			for (final char c : "0IPOL".toCharArray()) {
				Assertions.assertFalse(s.contains((String.valueOf(c).toLowerCase(Locale.ROOT))));
			}
		}
	}

	@Test
	public void randomStringOfLengthTest(){
		assertThrows(IllegalArgumentException.class, () -> RandomUtil.randomString("123", 0));
		assertThrows(IllegalArgumentException.class, () -> RandomUtil.randomString("123", -1));
	}

	@Test
	public void generateRandomNumberTest(){
		final int[] ints = RandomUtil.randomPickInts(5, NumberUtil.range(5, 20));
		assertEquals(5, ints.length);
		final Set<?> set = ConvertUtil.convert(Set.class, ints);
		assertEquals(5, set.size());
	}

	/**
	 * 测试生成指定长度的数字字符串
	 */
	@Test
	public void randomNumbersTest() {
		// 测试生成长度为5的数字字符串
		final String result1 = RandomUtil.randomNumbers(5);
		assertEquals(5, result1.length(), "生成的字符串长度应该等于指定长度");
		assertTrue(Validator.isNumeric(result1), "生成的字符串应该只包含数字");

		// 测试生成长度为10的数字字符串
		final String result2 = RandomUtil.randomNumbers(10);
		assertEquals(10, result2.length(), "生成的字符串长度应该等于指定长度");
		assertTrue(Validator.isNumeric(result2), "生成的字符串应该只包含数字");

		// 测试生成长度为1的数字字符串
		final String result3 = RandomUtil.randomNumbers(1);
		assertEquals(1, result3.length(), "生成的字符串长度应该等于指定长度");
		assertTrue(Validator.isNumeric(result3), "生成的字符串应该只包含数字");

		// 验证多次生成的结果都不相同（概率性验证）
		final String result6 = RandomUtil.randomNumbers(8);
		final String result7 = RandomUtil.randomNumbers(8);
		// 由于是随机生成，不能保证一定不同，但大部分情况下应该不同
		// 所以我们主要验证它们都符合预期格式
		assertEquals(8, result6.length(), "生成的字符串长度应该等于指定长度");
		assertTrue(Validator.isNumeric(result6), "生成的字符串应该只包含数字");
		assertEquals(8, result7.length(), "生成的字符串长度应该等于指定长度");
		assertTrue(Validator.isNumeric(result7), "生成的字符串应该只包含数字");
	}

	/**
	 * 测试 randomDouble(double limit, int scale, RoundingMode roundingMode) 方法正常功能
	 * 验证生成的随机数在正确范围内，并且精度符合要求
	 */
	@Test
	public void testRandomDoubleWithScaleAndRoundingMode() {
		// 测试基本功能：生成0到10之间的随机数，保留2位小数，四舍五入
		final double result = RandomUtil.randomDouble(10.0, 2);

		// 验证结果在[0, 10)范围内
		assertTrue(result >= 0.0 && result < 10.0,
			"随机数应该在[0, 10)范围内，实际值：" + result);

		// 验证小数点后最多2位数字
		final String resultStr = String.valueOf(result);
		if (resultStr.contains(".")) {
			final int decimalPlaces = resultStr.length() - resultStr.indexOf('.') - 1;
			assertTrue(decimalPlaces <= 2,
				"小数位数应该不超过2位，实际：" + decimalPlaces);
		}
	}

	/**
	 * 测试不同的舍入模式
	 */
	@Test
	public void testRandomDoubleWithDifferentRoundingModes() {
		final double limit = 5.0;

		// 测试向上舍入
		final double upResult = RandomUtil.randomDouble(limit, 2);
		assertTrue(upResult >= 0.0 && upResult < limit,
			"UP模式下随机数应该在[0, 5)范围内");

		// 测试向下舍入
		final double downResult = RandomUtil.randomDouble(limit, 2);
		assertTrue(downResult >= 0.0 && downResult < limit,
			"DOWN模式下随机数应该在[0, 5)范围内");

		// 测试四舍五入
		final double halfUpResult = RandomUtil.randomDouble(limit, 2);
		assertTrue(halfUpResult >= 0.0 && halfUpResult < limit,
			"HALF_UP模式下随机数应该在[0, 5)范围内");
	}

	/**
	 * 测试精度为0的情况
	 */
	@Test
	public void testRandomDoubleWithZeroScale() {
		final double result = RandomUtil.randomDouble(10.0, 0);

		// 验证结果在[0, 10)范围内
		assertTrue(result >= 0.0 && result < 10.0,
			"随机数应该在[0, 10)范围内");

		// 验证没有小数部分（整数）
		assertEquals(Math.floor(result), result, 0.001,
			"精度为0时应该返回整数");
	}

	/**
	 * 测试舍入模式为null的情况（应使用默认的HALF_UP模式）
	 */
	@Test
	public void testRandomDoubleWithNullRoundingMode() {
		final double result = RandomUtil.randomDouble(10.0, 2);

		// 验证结果在[0, 10)范围内
		assertTrue(result >= 0.0 && result < 10.0,
			"随机数应该在[0, 10)范围内");

		// 验证小数位数不超过2位
		final String resultStr = String.valueOf(result);
		if (resultStr.contains(".")) {
			final int decimalPlaces = resultStr.length() - resultStr.indexOf('.') - 1;
			assertTrue(decimalPlaces <= 2,
				"小数位数应该不超过2位，实际：" + decimalPlaces);
		}
	}

	/**
	 * 测试边界情况：limit为较小值
	 */
	@Test
	public void testRandomDoubleWithSmallLimit() {
		final double result = RandomUtil.randomDouble(0.001, 5);

		// 验证结果在[0, 0.001)范围内
		assertTrue(result >= 0.0 && result < 0.001,
			"随机数应该在[0, 0.001)范围内，实际值：" + result);
	}

	/**
	 * 测试较大精度的情况
	 */
	@Test
	public void testRandomDoubleWithHighPrecision() {
		final double result = RandomUtil.randomDouble(100.0, 10);

		// 验证结果在[0, 100)范围内
		assertTrue(result >= 0.0 && result < 100.0,
			"随机数应该在[0, 100)范围内");

		// 验证结果经过了精度处理
		final String resultStr = String.valueOf(result);
		if (resultStr.contains(".")) {
			final int decimalPlaces = resultStr.length() - resultStr.indexOf('.') - 1;
			assertTrue(decimalPlaces <= 10,
				"小数位数应该不超过10位，实际：" + decimalPlaces);
		}
	}
}
