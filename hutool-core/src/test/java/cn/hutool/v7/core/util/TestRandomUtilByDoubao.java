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

package cn.hutool.v7.core.util;

import cn.hutool.v7.core.date.DateField;
import cn.hutool.v7.core.date.DateTime;
import cn.hutool.v7.core.date.DateUnit;
import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.lang.selector.WeightObj;
import cn.hutool.v7.core.lang.selector.WeightRandomSelector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RandomUtil 单元测试类
 * 基于 JUnit 5 实现，覆盖所有核心方法的功能验证
 */
public class TestRandomUtilByDoubao {

	// ===================== 基础随机数生成测试 =====================
	@Test
	void testGetRandom() {
		// 测试普通随机数生成器
		final ThreadLocalRandom random = RandomUtil.getRandom();
		assertNotNull(random);

		// 测试安全随机数生成器
		final Random secureRandom = RandomUtil.getRandom(true);
		assertInstanceOf(SecureRandom.class, secureRandom);

		// 测试非安全随机数生成器
		final Random normalRandom = RandomUtil.getRandom(false);
		assertInstanceOf(ThreadLocalRandom.class, normalRandom);
	}

	@Test
	void testRandomBoolean() {
		// 验证生成的布尔值在多次调用中会出现 true 和 false
		boolean hasTrue = false;
		boolean hasFalse = false;
		for (int i = 0; i < 100; i++) {
			final boolean result = RandomUtil.randomBoolean();
			if (result) {
				hasTrue = true;
			} else {
				hasFalse = true;
			}
			if (hasTrue && hasFalse) {
				break;
			}
		}
		assertTrue(hasTrue && hasFalse);
	}

	@Test
	void testRandomBytes() {
		// 测试随机字节数组长度
		final int length = 10;
		final byte[] bytes = RandomUtil.randomBytes(length);
		assertEquals(length, bytes.length);

		// 测试指定随机数生成器的字节数组
		final byte[] bytesWithRandom = RandomUtil.randomBytes(length, new Random());
		assertEquals(length, bytesWithRandom.length);
	}

	@SuppressWarnings("UnnecessaryUnicodeEscape")
	@Test
	void testRandomChinese() {
		// 验证生成的字符是汉字（Unicode 范围：\u4E00 - \u9FFF）
		final char chineseChar = RandomUtil.randomChinese();
		assertTrue(chineseChar >= '\u4E00' && chineseChar <= '\u9FFF');
	}

	// ===================== 整数随机数测试 =====================
	@SuppressWarnings("ConstantValue")
	@Test
	void testRandomInt() {
		// 测试无参随机整数
		final int randomInt = RandomUtil.randomInt();
		assertTrue(randomInt >= Integer.MIN_VALUE && randomInt <= Integer.MAX_VALUE);

		// 测试 [0, limit) 范围
		final int limit = 100;
		final int intWithLimit = RandomUtil.randomInt(limit);
		assertTrue(intWithLimit >= 0 && intWithLimit < limit);

		// 测试 [min, max) 范围
		final int min = 10;
		final int max = 20;
		final int intInRange = RandomUtil.randomInt(min, max);
		assertTrue(intInRange >= min && intInRange < max);

		// 测试包含最大值的情况
		final int intWithMax = RandomUtil.randomInt(min, max, true, true);
		assertTrue(intWithMax >= min && intWithMax <= max);

		// 测试不包含最小值的情况
		final int intWithoutMin = RandomUtil.randomInt(min, max, false, false);
		assertTrue(intWithoutMin > min && intWithoutMin < max);

		// 测试随机索引数组
		final int[] randomInts = RandomUtil.randomInts(5);
		assertEquals(5, randomInts.length);
		// 验证索引是 0-4 的乱序排列
		final Set<Integer> indexSet = new HashSet<>();
		for (final int num : randomInts) {
			indexSet.add(num);
			assertTrue(num >= 0 && num < 5);
		}
		assertEquals(5, indexSet.size());
	}

	// ===================== 长整数随机数测试 =====================
	@SuppressWarnings("ConstantValue")
	@Test
	void testRandomLong() {
		// 测试无参随机长整数
		final long randomLong = RandomUtil.randomLong();
		assertTrue(randomLong >= Long.MIN_VALUE && randomLong <= Long.MAX_VALUE);

		// 测试 [0, limit) 范围
		final long limit = 1000L;
		final long longWithLimit = RandomUtil.randomLong(limit);
		assertTrue(longWithLimit >= 0 && longWithLimit < limit);

		// 测试 [min, max) 范围
		final long min = 100L;
		final long max = 200L;
		final long longInRange = RandomUtil.randomLong(min, max);
		assertTrue(longInRange >= min && longInRange < max);

		// 测试包含最大值的情况
		final long longWithMax = RandomUtil.randomLong(min, max, true, true);
		assertTrue(longWithMax >= min && longWithMax <= max);
	}

	// ===================== 浮点数随机数测试 =====================
	@Test
	void testRandomFloat() {
		// 测试 [0, 1) 范围
		final float float01 = RandomUtil.randomFloat();
		assertTrue(float01 >= 0 && float01 < 1);

		// 测试 [0, limit) 范围
		final float limit = 100.0f;
		final float floatWithLimit = RandomUtil.randomFloat(limit);
		assertTrue(floatWithLimit >= 0 && floatWithLimit < limit);

		// 测试 [min, max) 范围
		final float min = 10.0f;
		final float max = 20.0f;
		final float floatInRange = RandomUtil.randomFloat(min, max);
		assertTrue(floatInRange >= min && floatInRange < max);

		// 测试最小值等于最大值的情况
		final float floatEqual = RandomUtil.randomFloat(min, min);
		assertEquals(min, floatEqual);
	}

	// ===================== 双精度浮点数随机数测试 =====================
	@Test
	void testRandomDouble() {
		// 测试 [0, 1) 范围
		final double double01 = RandomUtil.randomDouble();
		assertTrue(double01 >= 0 && double01 < 1);

		// 测试 [0, limit) 范围
		final double limit = 1000.0;
		final double doubleWithLimit = RandomUtil.randomDouble(limit);
		assertTrue(doubleWithLimit >= 0 && doubleWithLimit < limit);

		// 测试 [min, max) 范围
		final double min = 100.0;
		final double max = 200.0;
		final double doubleInRange = RandomUtil.randomDouble(min, max);
		assertTrue(doubleInRange >= min && doubleInRange < max);

		// 测试保留小数位数
		final double doubleWithScale = RandomUtil.randomDouble(min, max, 2);
		// 验证小数位数不超过 2 位
		final String doubleStr = String.valueOf(doubleWithScale);
		int dotIndex = doubleStr.indexOf('.');
		if (dotIndex != -1) {
			assertTrue(doubleStr.length() - dotIndex - 1 <= 2);
		}

		// 测试无范围保留小数
		final double doubleScaleOnly = RandomUtil.randomDouble(3);
		final String scaleStr = String.valueOf(doubleScaleOnly);
		dotIndex = scaleStr.indexOf('.');
		if (dotIndex != -1) {
			assertTrue(scaleStr.length() - dotIndex - 1 <= 3);
		}
	}

	// ===================== 高精度小数随机数测试 =====================
	@Test
	void testRandomBigDecimal() {
		// 测试 [0, 1) 范围
		final BigDecimal bd01 = RandomUtil.randomBigDecimal();
		assertTrue(bd01.compareTo(BigDecimal.ZERO) >= 0 && bd01.compareTo(BigDecimal.ONE) < 0);

		// 测试 [0, limit) 范围
		final BigDecimal limit = new BigDecimal("100.0");
		final BigDecimal bdWithLimit = RandomUtil.randomBigDecimal(limit);
		assertTrue(bdWithLimit.compareTo(BigDecimal.ZERO) >= 0 && bdWithLimit.compareTo(limit) < 0);

		// 测试 [min, max) 范围
		final BigDecimal min = new BigDecimal("10.0");
		final BigDecimal max = new BigDecimal("20.0");
		final BigDecimal bdInRange = RandomUtil.randomBigDecimal(min, max);
		assertTrue(bdInRange.compareTo(min) >= 0 && bdInRange.compareTo(max) < 0);
	}

	// ===================== 随机元素选取测试 =====================
	@Test
	void testRandomEle() {
		// 测试列表随机元素
		final List<String> list = Arrays.asList("A", "B", "C", "D");
		final String randomEle = RandomUtil.randomEle(list);
		assertTrue(list.contains(randomEle));

		// 测试列表前 N 项随机元素
		final String randomEleLimit = RandomUtil.randomEle(list, 2);
		assertTrue(Arrays.asList("A", "B").contains(randomEleLimit));

		// 测试数组随机元素
		final String[] array = {"X", "Y", "Z"};
		final String randomArrayEle = RandomUtil.randomEle(array);
		assertTrue(Arrays.asList(array).contains(randomArrayEle));

		// 测试数组前 N 项随机元素
		final String randomArrayEleLimit = RandomUtil.randomEle(array, 2);
		assertTrue(Arrays.asList("X", "Y").contains(randomArrayEleLimit));
	}

	@Test
	void testRandomEles() {
		// 测试随机获取多个元素（允许重复）
		final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		final List<Integer> randomEles = RandomUtil.randomEles(list, 3);
		assertEquals(3, randomEles.size());
		for (final Integer num : randomEles) {
			assertTrue(list.contains(num));
		}
	}

	@Test
	void testRandomPick() {
		// 测试随机选取不重复位置的元素
		final List<String> list = Arrays.asList("a", "b", "c", "d", "e");
		final List<String> picked = RandomUtil.randomPick(list, 3);
		assertEquals(3, picked.size());
		// 验证选取的元素都是原列表中的
		for (final String s : picked) {
			assertTrue(list.contains(s));
		}

		// 测试选取数量大于等于列表长度的情况
		final List<String> pickedAll = RandomUtil.randomPick(list, 10);
		assertEquals(list.size(), pickedAll.size());
		assertEquals(new HashSet<>(list), new HashSet<>(pickedAll));
	}

	@Test
	void testRandomPickInts() {
		// 测试随机选取整数数组元素
		final int[] seed = {1, 2, 3, 4, 5};
		final int[] pickedInts = RandomUtil.randomPickInts(3, seed.clone());
		assertEquals(3, pickedInts.length);
		// 验证选取的元素都是原数组中的
		final Set<Integer> seedSet = new HashSet<>();
		for (final int num : seed) {
			seedSet.add(num);
		}
		for (final int num : pickedInts) {
			assertTrue(seedSet.contains(num));
		}

		// 测试选取数量超过种子长度的异常
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			RandomUtil.randomPickInts(10, seed);
		});
	}

	@Test
	void testRandomEleSet() {
		// 测试随机获取不重复元素集合
		final List<String> list = Arrays.asList("1", "2", "3", "4", "5", "1", "2");
		final Set<String> randomSet = RandomUtil.randomEleSet(list, 3);
		assertEquals(3, randomSet.size());
		// 验证元素都是原列表中的去重元素
		final Set<String> distinctSet = new HashSet<>(list);
		for (final String s : randomSet) {
			assertTrue(distinctSet.contains(s));
		}

		// 测试选取数量超过去重后长度的异常
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			RandomUtil.randomEleSet(list, 10);
		});
	}

	// ===================== 随机字符串生成测试 =====================
	@Test
	void testRandomString() {
		final int length = 8;

		// 测试数字+大小写字母
		final String lettersAndNumbers = RandomUtil.randomLettersAndNumbers(length);
		assertEquals(length, lettersAndNumbers.length());
		assertTrue(lettersAndNumbers.matches("[0-9A-Za-z]+"));

		// 测试数字+小写字母
		final String lower = RandomUtil.randomLettersAndNumbersLower(length);
		assertEquals(length, lower.length());
		assertTrue(lower.matches("[0-9a-z]+"));

		// 测试数字+大写字母
		final String upper = RandomUtil.randomLettersAndNumbersUpper(length);
		assertEquals(length, upper.length());
		assertTrue(upper.matches("[0-9A-Z]+"));

		// 测试排除指定字符
		final String withoutStr = RandomUtil.randomLettersAndNumbersWithoutStr(length, "0OlL1");
		assertEquals(length, withoutStr.length());
		Assertions.assertFalse(withoutStr.matches(".*[0OlL1].*"));

		// 测试小写字母+数字排除指定字符
		final String lowerWithout = RandomUtil.randomLettersAndNumbersLowerWithoutStr(length, "0OlL1");
		assertEquals(length, lowerWithout.length());
		Assertions.assertFalse(lowerWithout.matches(".*[0ol1].*"));

		// 测试纯数字
		final String numbers = RandomUtil.randomNumbers(length);
		assertEquals(length, numbers.length());
		assertTrue(numbers.matches("[0-9]+"));

		// 测试纯字母
		final String letters = RandomUtil.randomLetters(length);
		assertEquals(length, letters.length());
		assertTrue(letters.matches("[A-Za-z]+"));

		// 测试纯小写字母
		final String lettersLower = RandomUtil.randomLettersLower(length);
		assertEquals(length, lettersLower.length());
		assertTrue(lettersLower.matches("[a-z]+"));

		// 测试纯大写字母
		final String lettersUpper = RandomUtil.randomLettersUpper(length);
		assertEquals(length, lettersUpper.length());
		assertTrue(lettersUpper.matches("[A-Z]+"));

		// 测试自定义字符集
		final String custom = RandomUtil.randomString("abc123", length);
		assertEquals(length, custom.length());
		assertTrue(custom.matches("[abc123]+"));

		// 测试空字符集异常
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			RandomUtil.randomString("", length);
		});
	}

	// ===================== 随机字符生成测试 =====================
	@Test
	void testRandomChar() {
		// 测试随机数字字符
		final char numberChar = RandomUtil.randomNumber();
		assertTrue(Character.isDigit(numberChar));

		// 测试随机小写字母+数字字符
		final char charLower = RandomUtil.randomChar();
		assertTrue(Character.isDigit(charLower) || (charLower >= 'a' && charLower <= 'z'));

		// 测试自定义字符集字符
		final char customChar = RandomUtil.randomChar("ABC123");
		assertTrue("ABC123".indexOf(customChar) != -1);

		// 测试字符数组随机字符
		final char[] chars = {'X', 'Y', 'Z'};
		final char arrayChar = RandomUtil.randomChar(chars);
		assertTrue(Arrays.asList('X', 'Y', 'Z').contains(arrayChar));

		// 测试随机字符数组生成
		final char[] randomChars = RandomUtil.randomChars(chars, 5);
		assertEquals(5, randomChars.length);
		for (final char c : randomChars) {
			assertTrue(Arrays.asList('X', 'Y', 'Z').contains(c));
		}
	}

	// ===================== 权重随机测试 =====================
	@SuppressWarnings("unchecked")
	@Test
	void testWeightRandom() {
		// 构建权重对象列表
		final WeightObj<String> obj1 = new WeightObj<>("A", 1);
		final WeightObj<String> obj2 = new WeightObj<>("B", 9);
		final List<WeightObj<String>> weightObjs = Arrays.asList(obj1, obj2);

		// 测试数组形式的权重随机
		final WeightRandomSelector<String> selector1 = RandomUtil.weightRandom(weightObjs.toArray(new WeightObj[0]));
		// 测试迭代器形式的权重随机
		final WeightRandomSelector<String> selector2 = RandomUtil.weightRandom(weightObjs);

		// 验证多次随机后，权重高的元素出现概率更高（概率性验证）
		int countA = 0;
		int countB = 0;
		final int total = 10000;
		for (int i = 0; i < total; i++) {
			final String result = selector1.select();
			if ("A".equals(result)) {
				countA++;
			} else if ("B".equals(result)) {
				countB++;
			}
		}
		// 验证 A 的出现概率约 10%，B 约 90%（允许 ±2% 误差）
		assertTrue(countA >= 800 && countA <= 1200);
		assertTrue(countB >= 8800 && countB <= 9200);
	}

	// ===================== 随机日期测试 =====================
	@Test
	void testRandomDate() {
		// 测试基于当天的随机天数
		final DateTime randomDay = RandomUtil.randomDay(-5, 5);
		// 验证日期在 [-5, 4] 天范围内（因为 max 不包含）
		final long diff = DateUtil.betweenDay(DateUtil.now(), randomDay, true);
		assertTrue(diff >= -5 && diff <= 4);

		// 测试基于指定日期的随机时间
		final DateTime baseDate = DateUtil.parse("2024-01-01 12:00:00");
		final DateTime randomDate = RandomUtil.randomDate(baseDate, DateField.HOUR_OF_DAY, -2, 3);
		// 验证小时偏移在 [-2, 2] 范围内
		final long hourDiff = DateUtil.between(baseDate, randomDate, DateUnit.HOUR);
		assertTrue(hourDiff >= -2 && hourDiff <= 2);

		// 测试空基准日期
		final DateTime randomDateWithNull = RandomUtil.randomDate(null, DateField.MINUTE, -10, 10);
		assertNotNull(randomDateWithNull);
	}
}
