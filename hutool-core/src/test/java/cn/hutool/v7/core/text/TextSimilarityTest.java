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

package cn.hutool.v7.core.text;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * 文本相似度计算工具类单元测试
 *
 * @author Looly
 */
public class TextSimilarityTest {

	@Test
	public void similarDegreeTest() {
		final String a = "我是一个文本，独一无二的文本";
		final String b = "一个文本，独一无二的文本";

		final double degree = TextSimilarity.similar(a, b);
		Assertions.assertEquals(0.8461538462D, degree, 0.0000000001);

		final String similarPercent = TextSimilarity.similar(a, b, 2);
		Assertions.assertEquals("84.62%", similarPercent);
	}

	@Test
	public void similarDegreeTest2() {
		final String a = "我是一个文本，独一无二的文本";
		final String b = "一个文本，独一无二的文本,#,>>?#$%^%$&^&^%";

		final double degree = TextSimilarity.similar(a, b);
		Assertions.assertEquals(0.8461538462D, degree, 0.0001);

		final String similarPercent = TextSimilarity.similar(a, b, 2);
		Assertions.assertEquals("84.62%", similarPercent);
	}

	@Test
	public void similarTest() {
		final double abd = TextSimilarity.similar("abd", "1111");
		Assertions.assertEquals(0, abd, 1);
	}

	@Test
	@Disabled
	void longestCommonSubstringLengthTest() {
		// https://github.com/chinabugotech/hutool/issues/3045
		final String strCommon = RandomUtil.randomLettersAndNumbersLower(1024 * 32);
		final String strA = RandomUtil.randomLettersAndNumbersLower(1024 * 32) + strCommon;
		final String strB = RandomUtil.randomLettersAndNumbersLower(1024 * 32) + strCommon;

		final int i = TextSimilarity.longestCommonSubstringLength(strA, strB);
		Console.log(i);
	}

	@Test
	void rollingLengthMatchesReconstructedSubsequenceInBothDirections() {
		final Random random = new Random(31);
		for (int n = 0; n < 100; n++) {
			final StringBuilder a = new StringBuilder();
			final StringBuilder b = new StringBuilder();
			for (int i = 0; i < n % 19; i++) {
				a.append("中ab".charAt(random.nextInt(3)));
			}
			for (int i = 0; i < n % 13; i++) {
				b.append("中ab".charAt(random.nextInt(3)));
			}
			final int length = TextSimilarity.longestCommonSubstring(a.toString(), b.toString()).length();
			final int max = Math.max(a.length(), b.length());
			final double expected = max == 0 ? 1D : (double) length / max;
			Assertions.assertEquals(expected, TextSimilarity.similar(a.toString(), b.toString()), 1e-9);
			Assertions.assertEquals(expected, TextSimilarity.similar(b.toString(), a.toString()), 1e-9);
		}
	}


	@Test
	void sharedEndsDoNotOverlapOrHideMatchesInTheMiddle() {
		Assertions.assertEquals(1D, TextSimilarity.similar("aaaa", "aaaa"));
		Assertions.assertEquals(0.75D, TextSimilarity.similar("aaa", "aaaa"));
		Assertions.assertEquals(0.75D, TextSimilarity.similar("xaay", "xaby"));
		Assertions.assertEquals(0.8D, TextSimilarity.similar("abca", "axbca"));
	}
}
