package cn.hutool.core.text;

import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI96LWHTest {
	@Test
	public void replaceTest() {
		String str = "\uD83D\uDC46最上方点击蓝字";
		Assertions.assertArrayEquals(new int[]{128070, 26368, 19978, 26041, 28857, 20987, 34013, 23383}, str.codePoints().toArray());
		// 这个方法里\uD83D\uDC46表示一个emoji表情，使用codePoint之后，一个表情表示一个字符，因此按照一个字符对
		Assertions.assertEquals("\uD83D\uDC46最上下点击蓝字", StrUtil.replaceByCodePoint(str, 3, 4, "下"));
		Assertions.assertEquals("\uD83D\uDC46最下方点击蓝字", new StringBuilder(str).replace(3, 4, "下").toString());
	}
}
