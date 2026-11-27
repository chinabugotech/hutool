package cn.hutool.core.text;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 文本相似度计算工具类单元测试
 * @author looly
 *
 */
public class TextSimilarityTest {

	@Test
	public void similarDegreeTest() {
		String a = "我是一个文本，独一无二的文本";
		String b = "一个文本，独一无二的文本";

		double degree = TextSimilarity.similar(a, b);
		assertEquals(0.8461538462D, degree, 0.01);

		String similarPercent = TextSimilarity.similar(a, b, 2);
		assertEquals("84.62%", similarPercent);
	}

	@Test
	public void similarDegreeTest2() {
		String a = "我是一个文本，独一无二的文本";
		String b = "一个文本，独一无二的文本,#,>>?#$%^%$&^&^%";

		double degree = TextSimilarity.similar(a, b);
		assertEquals(0.8461538462D, degree, 0.01);

		String similarPercent = TextSimilarity.similar(a, b, 2);
		assertEquals("84.62%", similarPercent);
	}

	@Test
	public void similarTest(){
		final double abd = TextSimilarity.similar("abd", "1111");
		assertEquals(0, abd, 0);
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
			assertEquals(expected, TextSimilarity.similar(a.toString(), b.toString()), 1e-9);
			assertEquals(expected, TextSimilarity.similar(b.toString(), a.toString()), 1e-9);
		}
	}


	@Test
	void sharedEndsDoNotOverlapOrHideMatchesInTheMiddle() {
		assertEquals(1D, TextSimilarity.similar("aaaa", "aaaa"));
		assertEquals(0.75D, TextSimilarity.similar("aaa", "aaaa"));
		assertEquals(0.75D, TextSimilarity.similar("xaay", "xaby"));
		assertEquals(0.8D, TextSimilarity.similar("abca", "axbca"));
	}
}
