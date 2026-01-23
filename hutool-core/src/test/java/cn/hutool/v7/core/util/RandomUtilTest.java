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
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.math.NumberUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

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
		final double randomDouble = RandomUtil.randomDouble(0, 1, 0, RoundingMode.HALF_UP);
		assertTrue(randomDouble <= 1);
	}

	@Test
	@Disabled
	public void randomBooleanTest() {
		Console.log(RandomUtil.randomBoolean());
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
		final String s = RandomUtil.randomString("123", -1);
		assertNotNull(s);
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
		assertTrue(isNumeric(result1), "生成的字符串应该只包含数字");

		// 测试生成长度为10的数字字符串
		final String result2 = RandomUtil.randomNumbers(10);
		assertEquals(10, result2.length(), "生成的字符串长度应该等于指定长度");
		assertTrue(isNumeric(result2), "生成的字符串应该只包含数字");

		// 测试生成长度为1的数字字符串
		final String result3 = RandomUtil.randomNumbers(1);
		assertEquals(1, result3.length(), "生成的字符串长度应该等于指定长度");
		assertTrue(isNumeric(result3), "生成的字符串应该只包含数字");

		// 测试生成长度为0的数字字符串（会被调整为1）
		final String result4 = RandomUtil.randomNumbers(0);
		assertEquals(1, result4.length(), "当长度为0时，应生成长度为1的字符串");
		assertTrue(isNumeric(result4), "生成的字符串应该只包含数字");

		// 测试生成长度为负数的数字字符串（会被调整为1）
		final String result5 = RandomUtil.randomNumbers(-5);
		assertEquals(1, result5.length(), "当长度为负数时，应生成长度为1的字符串");
		assertTrue(isNumeric(result5), "生成的字符串应该只包含数字");

		// 验证多次生成的结果都不相同（概率性验证）
		final String result6 = RandomUtil.randomNumbers(8);
		final String result7 = RandomUtil.randomNumbers(8);
		// 由于是随机生成，不能保证一定不同，但大部分情况下应该不同
		// 所以我们主要验证它们都符合预期格式
		assertEquals(8, result6.length(), "生成的字符串长度应该等于指定长度");
		assertTrue(isNumeric(result6), "生成的字符串应该只包含数字");
		assertEquals(8, result7.length(), "生成的字符串长度应该等于指定长度");
		assertTrue(isNumeric(result7), "生成的字符串应该只包含数字");
	}

	/**
	 * 辅助方法：检查字符串是否只包含数字
	 */
	private boolean isNumeric(final String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		final Pattern pattern = Pattern.compile("\\d+");
		return pattern.matcher(str).matches();
	}
}
