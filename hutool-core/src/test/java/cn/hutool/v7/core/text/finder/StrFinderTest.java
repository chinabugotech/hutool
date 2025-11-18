
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
		String text = "Hello Hutool World";
		StrFinder finder = StrFinder.of("Hutool", false);
		finder.setText(text);

		Assertions.assertEquals(6, finder.start(0));
		Assertions.assertEquals(-1, finder.start(7));
	}

	@Test
	public void testForwardIgnoreCase() {
		// 正向查找，忽略大小写
		String text = "Hello HUTOOL World";
		StrFinder finder = StrFinder.of("hutool", true);
		finder.setText(text);

		Assertions.assertEquals(6, finder.start(0));
	}

	@Test
	public void testReverseBasic() {
		// 反向查找
		String text = "abc abc abc";
		StrFinder finder = StrFinder.of("abc", false);
		finder.setNegative(true);
		finder.setText(text);

		Assertions.assertEquals(8, finder.start(text.length() - 1));
		Assertions.assertEquals(4, finder.start(7));
		Assertions.assertEquals(0, finder.start(2));
	}

	@Test
	public void testReverseIgnoreCase() {
		// 反向查找，忽略大小写
		String text = "ABC abc Abc";
		StrFinder finder = StrFinder.of("abc", true);
		finder.setNegative(true);
		finder.setText(text);

		Assertions.assertEquals(8, finder.start(text.length() - 1));
	}

//	@Test
//	public void testAlgorithmEdgeCase() {
//		// 5. 算法边界测试：验证 Sunday 算法偏移表构建是否正确
//		// 场景：模式串首尾字符重复 "aba"
//		// 正向表应存靠右的 'a'，反向表应存靠左的 'a'
//
//		String text = "ababa";
//		// 索引: 01234
//
//		// --- 正向 ---
//		StrFinder forward = StrFinder.of("aba", false);
//		forward.setText(text);
//		Assertions.assertEquals(0, forward.start(0));
//		// 从1开始找，应该跳过第一个a，匹配到索引2的aba
//		Assertions.assertEquals(2, forward.start(1));
//
//		// --- 反向 ---
//		StrFinder reverse = StrFinder.of("aba", false);
//		reverse.setNegative(true);
//		reverse.setText(text);
//		Assertions.assertEquals(2, reverse.start(4));
//		Assertions.assertEquals(0, reverse.start(1));
//	}

	@Test
	public void testZeroCopy() {
		// 验证toString()优化
		StringBuilder bigText = new StringBuilder();
		bigText.append("ignore-".repeat(1000));
		bigText.append("TARGET");
		bigText.append("-ignore");

		StrFinder finder = StrFinder.of("TARGET", false);
		finder.setText(bigText);

		Assertions.assertEquals(7000, finder.start(0));
	}

	@Test
	public void testChinese() {
		// 中文测试
		String text = "希望pr能够通过";
		StrFinder finder = StrFinder.of("通过", false);
		finder.setText(text);

		Assertions.assertEquals(6, finder.start(0));
	}

	@Test
	public void testNotFound() {
		// 不包含字符串
		StrFinder finder = StrFinder.of("NotExists", false);
		finder.setText("Hello World");
		Assertions.assertEquals(-1, finder.start(0));
	}

	@Test
	public void benchmark() {
		System.out.println("正在生成测试数据...");
		StringBuilder sb = new StringBuilder();
		String base = "abcdefghijklmnopqrstuvwxyz0123456789-";
		sb.append(base.repeat(500));
		String target = "HUTOOL_TARGET";
		sb.append(target);

		String textStr = sb.toString();

		int loop = 100;
		for (int i = 0; i < 100; i++) {
			StrFinder.of(target, false).setText(textStr).start(0);
		}

		long start = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			int index = textStr.indexOf(target);
			if (index == -1) throw new RuntimeException("Bug!");
		}
		long end = System.currentTimeMillis();
		System.out.println("1. JDK String.indexOf耗时: " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			int index = StrFinder.of(target, false).setText(textStr).start(0);
			if (index == -1) throw new RuntimeException("Bug!");
		}
		end = System.currentTimeMillis();
		System.out.println("2. StrFinder (String) 耗时: " + (end - start) + "ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			int index = StrFinder.of(target, false).setText(sb).start(0);
			if (index == -1) throw new RuntimeException("Bug!");
		}
		end = System.currentTimeMillis();
		System.out.println("3. StrFinder (Builder)耗时: " + (end - start) + "ms");
	}
}
