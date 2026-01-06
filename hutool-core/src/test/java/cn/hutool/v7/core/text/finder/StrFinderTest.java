
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

package cn.hutool.v7.core.text.finder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Lettuceleaves
 */
public class StrFinderTest {

	@Test
	public void testForward() {
		// 正向查找
		final String text = "Hello Hutool World";
		final StrFinder finder = StrFinder.of("Hutool", false);
		finder.setText(text);

		Assertions.assertEquals(6, finder.start(0));
		Assertions.assertEquals(-1, finder.start(7));
	}

	@Test
	public void testForwardIgnoreCase() {
		// 正向查找，忽略大小写
		final String text = "Hello HUTOOL World";
		final StrFinder finder = StrFinder.of("hutool", true);
		finder.setText(text);

		Assertions.assertEquals(6, finder.start(0));
	}

	@Test
	public void testReverseBasic() {
		// 反向查找
		final String text = "abc abc abc";
		final StrFinder finder = StrFinder.of("abc", false);
		finder.setNegative(true);
		finder.setText(text);

		Assertions.assertEquals(8, finder.start(text.length() - 1));
		Assertions.assertEquals(4, finder.start(7));
		Assertions.assertEquals(0, finder.start(2));
	}

	@Test
	public void testReverseIgnoreCase() {
		// 反向查找，忽略大小写
		final String text = "ABC abc Abc";
		final StrFinder finder = StrFinder.of("abc", true);
		finder.setNegative(true);
		finder.setText(text);

		Assertions.assertEquals(8, finder.start(text.length() - 1));
	}

	@Test
	public void testAlgorithmEdgeCase() {

		final String text = "ababa";

		final StrFinder forward = StrFinder.of("aba", false);
		forward.setText(text);
		Assertions.assertEquals(0, forward.start(0));
		Assertions.assertEquals(2, forward.start(1));

		final StrFinder reverse = StrFinder.of("aba", false);
		reverse.setNegative(true);
		reverse.setText(text);
		Assertions.assertEquals(2, reverse.start(4));
		Assertions.assertEquals(0, reverse.start(1));
	}

	@Test
	public void testZeroCopy() {
		// 验证toString()优化
		final StringBuilder bigText = new StringBuilder();
		bigText.append("ignore-".repeat(1000));
		bigText.append("TARGET");
		bigText.append("-ignore");

		final StrFinder finder = StrFinder.of("TARGET", false);
		finder.setText(bigText);

		Assertions.assertEquals(7000, finder.start(0));
	}

	@Test
	public void testChinese() {
		// 中文测试
		final String text = "希望pr能够通过";
		final StrFinder finder = StrFinder.of("通过", false);
		finder.setText(text);

		Assertions.assertEquals(6, finder.start(0));
	}

	@Test
	public void testNotFound() {
		// 不包含字符串
		final StrFinder finder = StrFinder.of("NotExists", false);
		finder.setText("Hello World");
		Assertions.assertEquals(-1, finder.start(0));
	}

	@Test
	public void benchmark() {
		System.out.println("正在生成测试数据...");
		final StringBuilder sb = new StringBuilder();
		final String base = "abcdefghijklmnopqrstuvwxyz0123456789-";
		sb.append(base.repeat(500));
		final String target = "HUTOOL_TARGET";
		sb.append(target);

		final String textStr = sb.toString();

		final int loop = 100;
		for (int i = 0; i < 100; i++) {
			StrFinder.of(target, false).setText(textStr).start(0);
		}

		long start = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			final int index = textStr.indexOf(target);
			if (index == -1) throw new RuntimeException("Bug!");
		}
		long end = System.currentTimeMillis();
		System.out.println("1. JDK String.indexOf耗时: " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			final int index = StrFinder.of(target, false).setText(textStr).start(0);
			if (index == -1) throw new RuntimeException("Bug!");
		}
		end = System.currentTimeMillis();
		System.out.println("2. StrFinder (String) 耗时: " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			final int index = StrFinder.of(target, false).setText(sb).start(0);
			if (index == -1) throw new RuntimeException("Bug!");
		}
		end = System.currentTimeMillis();
		System.out.println("3. StrFinder (Builder)耗时: " + (end - start) + "ms");
	}
}
