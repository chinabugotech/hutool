package cn.hutool.core.util;

import cn.hutool.core.lang.Dict;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 字符串工具类单元测试
 *
 * @author Looly
 */
public class StrUtilTest {

	@Test
	public void isBlankTest() {
		final String blank = "	  　";
		assertTrue(StrUtil.isBlank(blank));
	}

	@Test
	public void trimTest() {
		final String blank = "	 哈哈 　";
		final String trim = StrUtil.trim(blank);
		assertEquals("哈哈", trim);
	}

	@Test
	public void trimNewLineTest() {
		String str = "\r\naaa";
		assertEquals("aaa", StrUtil.trim(str));
		str = "\raaa";
		assertEquals("aaa", StrUtil.trim(str));
		str = "\naaa";
		assertEquals("aaa", StrUtil.trim(str));
		str = "\r\n\r\naaa";
		assertEquals("aaa", StrUtil.trim(str));
	}

	@Test
	public void trimTabTest() {
		final String str = "\taaa";
		assertEquals("aaa", StrUtil.trim(str));
	}

	@Test
	public void cleanBlankTest() {
		// 包含：制表符、英文空格、不间断空白符、全角空格
		final String str = "	 你 好　";
		final String cleanBlank = StrUtil.cleanBlank(str);
		assertEquals("你好", cleanBlank);
	}

	@Test
	public void cutTest() {
		final String str = "aaabbbcccdddaadfdfsdfsdf0";
		final String[] cut = StrUtil.cut(str, 4);
		assertArrayEquals(new String[]{"aaab", "bbcc", "cddd", "aadf", "dfsd", "fsdf", "0"}, cut);
	}

	@Test
	public void splitTest() {
		final String str = "a,b ,c,d,,e";
		final List<String> split = StrUtil.split(str, ',', -1, true, true);
		// 测试空是否被去掉
		assertEquals(5, split.size());
		// 测试去掉两边空白符是否生效
		assertEquals("b", split.get(1));

		final String[] strings = StrUtil.splitToArray("abc/", '/');
		assertEquals(2, strings.length);

		// issue:I6FKSI
		assertThrows(IllegalArgumentException.class, () -> StrUtil.split("test length 0", 0));
	}

	@Test
	public void splitEmptyTest() {
		final String str = "";
		final List<String> split = StrUtil.split(str, ',', -1, true, true);
		// 测试空是否被去掉
		assertEquals(0, split.size());
	}

	@Test
	public void splitTest2() {
		final String str = "a.b.";
		final List<String> split = StrUtil.split(str, '.');
		assertEquals(3, split.size());
		assertEquals("b", split.get(1));
		assertEquals("", split.get(2));
	}

	@Test
	public void splitNullTest() {
		assertEquals(0, StrUtil.split(null, '.').size());
	}

	@Test
	public void splitToArrayNullTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			StrUtil.splitToArray(null, '.');
		});
	}

	@Test
	public void splitToLongTest() {
		final String str = "1,2,3,4, 5";
		long[] longArray = StrUtil.splitToLong(str, ',');
		assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);

		longArray = StrUtil.splitToLong(str, ",");
		assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);
	}

	@Test
	public void splitToIntTest() {
		final String str = "1,2,3,4, 5";
		int[] intArray = StrUtil.splitToInt(str, ',');
		assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);

		intArray = StrUtil.splitToInt(str, ",");
		assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);
	}

	@Test
	public void formatTest() {
		final String template = "你好，我是{name}，我的电话是：{phone}";
		final String result = StrUtil.format(template, Dict.create().set("name", "张三").set("phone", "13888881111"));
		assertEquals("你好，我是张三，我的电话是：13888881111", result);

		final String result2 = StrUtil.format(template, Dict.create().set("name", "张三").set("phone", null));
		assertEquals("你好，我是张三，我的电话是：{phone}", result2);
	}

	@Test
	public void stripTest() {
		String str = "abcd123";
		String strip = StrUtil.strip(str, "ab", "23");
		assertEquals("cd1", strip);

		str = "abcd123";
		strip = StrUtil.strip(str, "ab", "");
		assertEquals("cd123", strip);

		str = "abcd123";
		strip = StrUtil.strip(str, null, "");
		assertEquals("abcd123", strip);

		str = "abcd123";
		strip = StrUtil.strip(str, null, "567");
		assertEquals("abcd123", strip);

		assertEquals("", StrUtil.strip("a", "a"));
		assertEquals("", StrUtil.strip("a", "a", "b"));
	}

	@Test
	public void stripIgnoreCaseTest() {
		String str = "abcd123";
		String strip = StrUtil.stripIgnoreCase(str, "Ab", "23");
		assertEquals("cd1", strip);

		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, "AB", "");
		assertEquals("cd123", strip);

		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, "ab", "");
		assertEquals("cd123", strip);

		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, null, "");
		assertEquals("abcd123", strip);

		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, null, "567");
		assertEquals("abcd123", strip);
	}

	@Test
	public void indexOfIgnoreCaseTest() {
		assertEquals(-1, StrUtil.indexOfIgnoreCase(null, "balabala", 0));
		assertEquals(-1, StrUtil.indexOfIgnoreCase("balabala", null, 0));
		assertEquals(0, StrUtil.indexOfIgnoreCase("", "", 0));
		assertEquals(0, StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0));
		assertEquals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0));
		assertEquals(1, StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0));
		assertEquals(5, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3));
		assertEquals(-1, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9));
		assertEquals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1));
		assertEquals(-1, StrUtil.indexOfIgnoreCase("aabaabaa", "", 2));
		assertEquals(-1, StrUtil.indexOfIgnoreCase("abc", "", 9));
	}

	@Test
	public void lastIndexOfTest() {
		final String a = "aabbccddcc";
		final int lastIndexOf = StrUtil.lastIndexOf(a, "c", 0, false);
		assertEquals(-1, lastIndexOf);
	}

	@Test
	public void lastIndexOfIgnoreCaseTest() {
		assertEquals(-1, StrUtil.lastIndexOfIgnoreCase(null, "balabala", 0));
		assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("balabala", null));
		assertEquals(0, StrUtil.lastIndexOfIgnoreCase("", ""));
		assertEquals(7, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "A"));
		assertEquals(5, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B"));
		assertEquals(4, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "AB"));
		assertEquals(2, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 3));
		assertEquals(5, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 9));
		assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", -1));
		assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "", 2));
		assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("abc", "", 9));
		assertEquals(0, StrUtil.lastIndexOfIgnoreCase("AAAcsd", "aaa"));
	}

	@Test
	public void replaceTest() {
		String string = StrUtil.replaceByCodePoint("aabbccdd", 2, 6, '*');
		assertEquals("aa****dd", string);
		string = StrUtil.replaceByCodePoint("aabbccdd", 2, 12, '*');
		assertEquals("aa******", string);
	}

	@Test
	public void replaceTest2() {
		final String result = StrUtil.replace("123", "2", "3");
		assertEquals("133", result);
	}

	@Test
	public void replaceTest3() {
		final String result = StrUtil.replace(",abcdef,", ",", "|");
		assertEquals("|abcdef|", result);
	}

	@Test
	public void replaceTest4() {
		final String a = "1039";
		final String result = StrUtil.padPre(a, 8, "0"); //在字符串1039前补4个0
		assertEquals("00001039", result);

		final String aa = "1039";
		final String result1 = StrUtil.padPre(aa, -1, "0"); //在字符串1039前补4个0
		assertEquals("103", result1);
	}

	@Test
	public void replaceTest5() {
		final String a = "\uD853\uDC09秀秀";
		final String result = StrUtil.replaceByCodePoint(a, 1, a.length(), '*');
		assertEquals("\uD853\uDC09**", result);

		final String aa = "规划大师";
		final String result1 = StrUtil.replaceByCodePoint(aa, 2, a.length(), '*');
		assertEquals("规划**", result1);
	}

	@Test
	public void upperFirstTest() {
		final StringBuilder sb = new StringBuilder("KEY");
		final String s = StrUtil.upperFirst(sb);
		assertEquals(s, sb.toString());
	}

	@Test
	public void lowerFirstTest() {
		final StringBuilder sb = new StringBuilder("KEY");
		final String s = StrUtil.lowerFirst(sb);
		assertEquals("kEY", s);
	}

	@Test
	public void subTest() {
		final String a = "abcderghigh";
		final String pre = StrUtil.sub(a, -5, a.length());
		assertEquals("ghigh", pre);
	}

	@Test
	public void subByCodePointTest() {
		// 🤔👍🍓🤔
		final String test = "\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53\uD83E\uDD14";

		// 不正确的子字符串
		final String wrongAnswer = StrUtil.sub(test, 0, 3);
		assertNotEquals("\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53", wrongAnswer);

		// 正确的子字符串
		final String rightAnswer = StrUtil.subByCodePoint(test, 0, 3);
		assertEquals("\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53", rightAnswer);
	}

	@Test
	public void subBeforeTest() {
		final String a = "abcderghigh";
		String pre = StrUtil.subBefore(a, "d", false);
		assertEquals("abc", pre);
		pre = StrUtil.subBefore(a, 'd', false);
		assertEquals("abc", pre);
		pre = StrUtil.subBefore(a, 'a', false);
		assertEquals("", pre);

		//找不到返回原串
		pre = StrUtil.subBefore(a, 'k', false);
		assertEquals(a, pre);
		pre = StrUtil.subBefore(a, 'k', true);
		assertEquals(a, pre);
	}

	/**
	 * 测试字符串反转功能，特别是对特殊字符的处理
	 * 验证普通字符、中文字符以及Unicode代理对字符的反转行为
	 */
	@Test
	public void reverseByCodePointSpecialCharactersTest() {
		//普通情况-英文字符
		assertEquals("dcba", StrUtil.reverseByCodePoint("abcd"));

		//普通情况-中文字符
		assertEquals("界世好你", StrUtil.reverseByCodePoint("你好世界"));

		//保证Unicode字符语义正确，类似emoji、组合字符
		//A😊B
		String emojiStr = "A\uD83D\uDE0AB";
		String reversedEmoji = StrUtil.reverseByCodePoint(emojiStr);
		//B😊A
		assertEquals("B\uD83D\uDE0AA", reversedEmoji);

		//A🇨🇳B
		String surrogate = "A\uD83C\uDDE8\uD83C\uDDF3B";
		String reversedSurrogate = StrUtil.reverseByCodePoint(surrogate);
		//B🇨🇳A
		assertNotEquals("B\uD83C\uDDE8\uD83C\uDDF3A", reversedSurrogate);
	}

	@Test
	public void subAfterTest() {
		final String a = "abcderghigh";
		String pre = StrUtil.subAfter(a, "d", false);
		assertEquals("erghigh", pre);
		pre = StrUtil.subAfter(a, 'd', false);
		assertEquals("erghigh", pre);
		pre = StrUtil.subAfter(a, 'h', true);
		assertEquals("", pre);

		//找不到字符返回空串
		pre = StrUtil.subAfter(a, 'k', false);
		assertEquals("", pre);
		pre = StrUtil.subAfter(a, 'k', true);
		assertEquals("", pre);
	}

	@Test
	public void subSufByLengthTest() {
		assertEquals("cde", StrUtil.subSufByLength("abcde", 3));
		assertEquals("", StrUtil.subSufByLength("abcde", -1));
		assertEquals("", StrUtil.subSufByLength("abcde", 0));
		assertEquals("abcde", StrUtil.subSufByLength("abcde", 5));
		assertEquals("abcde", StrUtil.subSufByLength("abcde", 10));
	}

	@Test
	public void repeatAndJoinTest() {
		String repeatAndJoin = StrUtil.repeatAndJoin("?", 5, ",");
		assertEquals("?,?,?,?,?", repeatAndJoin);

		repeatAndJoin = StrUtil.repeatAndJoin("?", 0, ",");
		assertEquals("", repeatAndJoin);

		repeatAndJoin = StrUtil.repeatAndJoin("?", 5, null);
		assertEquals("?????", repeatAndJoin);
	}

	@Test
	public void moveTest() {
		final String str = "aaaaaaa22222bbbbbbb";
		String result = StrUtil.move(str, 7, 12, -3);
		assertEquals("aaaa22222aaabbbbbbb", result);
		result = StrUtil.move(str, 7, 12, -4);
		assertEquals("aaa22222aaaabbbbbbb", result);
		result = StrUtil.move(str, 7, 12, -7);
		assertEquals("22222aaaaaaabbbbbbb", result);
		result = StrUtil.move(str, 7, 12, -20);
		assertEquals("aaaaaa22222abbbbbbb", result);

		result = StrUtil.move(str, 7, 12, 3);
		assertEquals("aaaaaaabbb22222bbbb", result);
		result = StrUtil.move(str, 7, 12, 7);
		assertEquals("aaaaaaabbbbbbb22222", result);
		result = StrUtil.move(str, 7, 12, 20);
		assertEquals("aaaaaaab22222bbbbbb", result);

		result = StrUtil.move(str, 7, 12, 0);
		assertEquals("aaaaaaa22222bbbbbbb", result);
	}

	@Test
	public void removePrefixIgnorecaseTest() {
		final String a = "aaabbb";
		String prefix = "aaa";
		assertEquals("bbb", StrUtil.removePrefixIgnoreCase(a, prefix));

		prefix = "AAA";
		assertEquals("bbb", StrUtil.removePrefixIgnoreCase(a, prefix));

		prefix = "AAABBB";
		assertEquals("", StrUtil.removePrefixIgnoreCase(a, prefix));
	}

	@Test
	public void maxLengthTest() {
		final String text = "我是一段正文，很长的正文，需要截取的正文";
		String str = StrUtil.maxLength(text, 5);
		assertEquals("我是一段正...", str);
		str = StrUtil.maxLength(text, 21);
		assertEquals(text, str);
		str = StrUtil.maxLength(text, 50);
		assertEquals(text, str);
	}

	@Test
	public void containsAnyTest() {
		//字符
		boolean containsAny = StrUtil.containsAny("aaabbbccc", 'a', 'd');
		assertTrue(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", 'e', 'd');
		assertFalse(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", 'd', 'c');
		assertTrue(containsAny);

		//字符串
		containsAny = StrUtil.containsAny("aaabbbccc", "a", "d");
		assertTrue(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", "e", "d");
		assertFalse(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", "d", "c");
		assertTrue(containsAny);

		// https://gitee.com/chinabugotech/hutool/issues/I7WSYD
		containsAny = StrUtil.containsAny("你好啊", "嗯", null);
		assertFalse(containsAny);
	}

	@Test
	public void centerTest() {
		assertNull(StrUtil.center(null, 10));
		assertEquals("    ", StrUtil.center("", 4));
		assertEquals("ab", StrUtil.center("ab", -1));
		assertEquals(" ab ", StrUtil.center("ab", 4));
		assertEquals("abcd", StrUtil.center("abcd", 2));
		assertEquals(" a  ", StrUtil.center("a", 4));
	}

	@Test
	public void padPreTest() {
		assertNull(StrUtil.padPre(null, 10, ' '));
		assertEquals("001", StrUtil.padPre("1", 3, '0'));
		assertEquals("12", StrUtil.padPre("123", 2, '0'));

		assertNull(StrUtil.padPre(null, 10, "AA"));
		assertEquals("AB1", StrUtil.padPre("1", 3, "ABC"));
		assertEquals("12", StrUtil.padPre("123", 2, "ABC"));
	}

	@Test
	public void padAfterTest() {
		assertNull(StrUtil.padAfter(null, 10, ' '));
		assertEquals("100", StrUtil.padAfter("1", 3, '0'));
		assertEquals("23", StrUtil.padAfter("123", 2, '0'));
		assertEquals("", StrUtil.padAfter("123", -1, '0'));

		assertNull(StrUtil.padAfter(null, 10, "ABC"));
		assertEquals("1AB", StrUtil.padAfter("1", 3, "ABC"));
		assertEquals("23", StrUtil.padAfter("123", 2, "ABC"));
	}

	@Test
	public void subBetweenAllTest() {
		assertArrayEquals(new String[]{"yz", "abc"}, StrUtil.subBetweenAll("saho[yz]fdsadp[abc]a", "[", "]"));
		assertArrayEquals(new String[]{"abc"}, StrUtil.subBetweenAll("saho[yzfdsadp[abc]a]", "[", "]"));
		assertArrayEquals(new String[]{"abc", "abc"}, StrUtil.subBetweenAll("yabczyabcz", "y", "z"));
		assertArrayEquals(new String[0], StrUtil.subBetweenAll(null, "y", "z"));
		assertArrayEquals(new String[0], StrUtil.subBetweenAll("", "y", "z"));
		assertArrayEquals(new String[0], StrUtil.subBetweenAll("abc", null, "z"));
		assertArrayEquals(new String[0], StrUtil.subBetweenAll("abc", "y", null));
	}

	@Test
	public void subBetweenAllTest2() {
		//issue#861@Github，起始不匹配的时候，应该直接空
		final String src1 = "/* \n* hutool  */  asdas  /* \n* hutool  */";
		final String src2 = "/ * hutool  */  asdas  / * hutool  */";

		final String[] results1 = StrUtil.subBetweenAll(src1, "/**", "*/");
		assertEquals(0, results1.length);

		final String[] results2 = StrUtil.subBetweenAll(src2, "/*", "*/");
		assertEquals(0, results2.length);
	}

	@Test
	public void subBetweenAllTest3() {
		final String src1 = "'abc'and'123'";
		String[] strings = StrUtil.subBetweenAll(src1, "'", "'");
		assertEquals(2, strings.length);
		assertEquals("abc", strings[0]);
		assertEquals("123", strings[1]);

		final String src2 = "'abc''123'";
		strings = StrUtil.subBetweenAll(src2, "'", "'");
		assertEquals(2, strings.length);
		assertEquals("abc", strings[0]);
		assertEquals("123", strings[1]);

		final String src3 = "'abc'123'";
		strings = StrUtil.subBetweenAll(src3, "'", "'");
		assertEquals(1, strings.length);
		assertEquals("abc", strings[0]);
	}

	@Test
	public void subBetweenAllTest4() {
		final String str = "你好:1388681xxxx用户已开通,1877275xxxx用户已开通,无法发送业务开通短信";
		final String[] strings = StrUtil.subBetweenAll(str, "1877275xxxx", ",");
		assertEquals(1, strings.length);
		assertEquals("用户已开通", strings[0]);
	}

	@Test
	public void briefTest() {
		// case: 1 至 str.length - 1
		final String str = RandomUtil.randomString(RandomUtil.randomInt(1, 100));
		for (int maxLength = 1; maxLength < str.length(); maxLength++) {
			final String brief = StrUtil.brief(str, maxLength);
			assertEquals(brief.length(), maxLength);
		}

		// case: 不会格式化的值
		assertEquals(str, StrUtil.brief(str, 0));
		assertEquals(str, StrUtil.brief(str, -1));
		assertEquals(str, StrUtil.brief(str, str.length()));
		assertEquals(str, StrUtil.brief(str, str.length() + 1));
	}

	@Test
	public void briefTest2() {
		final String str = "123";
		int maxLength = 3;
		String brief = StrUtil.brief(str, maxLength);
		assertEquals("123", brief);

		maxLength = 2;
		brief = StrUtil.brief(str, maxLength);
		assertEquals("1.", brief);

		maxLength = 1;
		brief = StrUtil.brief(str, maxLength);
		assertEquals("1", brief);
	}

	@Test
	public void briefTest3() {
		final String str = "123abc";

		int maxLength = 6;
		String brief = StrUtil.brief(str, maxLength);
		assertEquals(str, brief);

		maxLength = 5;
		brief = StrUtil.brief(str, maxLength);
		assertEquals("1...c", brief);

		maxLength = 4;
		brief = StrUtil.brief(str, maxLength);
		assertEquals("1..c", brief);

		maxLength = 3;
		brief = StrUtil.brief(str, maxLength);
		assertEquals("1.c", brief);

		maxLength = 2;
		brief = StrUtil.brief(str, maxLength);
		assertEquals("1.", brief);

		maxLength = 1;
		brief = StrUtil.brief(str, maxLength);
		assertEquals("1", brief);
	}

	@Test
	public void filterTest() {
		final String filterNumber = StrUtil.filter("hutool678", CharUtil::isNumber);
		assertEquals("678", filterNumber);
		final String cleanBlank = StrUtil.filter("	 你 好　", c -> !CharUtil.isBlankChar(c));
		assertEquals("你好", cleanBlank);
	}

	@Test
	public void wrapAllTest() {
		String[] strings = StrUtil.wrapAll("`", "`", StrUtil.splitToArray("1,2,3,4", ','));
		assertEquals("[`1`, `2`, `3`, `4`]", StrUtil.utf8Str(strings));

		strings = StrUtil.wrapAllWithPair("`", StrUtil.splitToArray("1,2,3,4", ','));
		assertEquals("[`1`, `2`, `3`, `4`]", StrUtil.utf8Str(strings));
	}

	@Test
	public void startWithTest() {
		final String a = "123";
		final String b = "123";

		assertTrue(StrUtil.startWith(a, b));
		assertFalse(StrUtil.startWithIgnoreEquals(a, b));
	}

	@Test
	public void indexedFormatTest() {
		final String ret = StrUtil.indexedFormat("this is {0} for {1}", "a", 1000);
		assertEquals("this is a for 1,000", ret);
	}

	@Test
	public void hideTest() {
		assertNull(StrUtil.hide(null, 1, 1));
		assertEquals("", StrUtil.hide("", 1, 1));
		assertEquals("****duan@163.com", StrUtil.hide("jackduan@163.com", -1, 4));
		assertEquals("ja*kduan@163.com", StrUtil.hide("jackduan@163.com", 2, 3));
		assertEquals("jackduan@163.com", StrUtil.hide("jackduan@163.com", 3, 2));
		assertEquals("jackduan@163.com", StrUtil.hide("jackduan@163.com", 16, 16));
		assertEquals("jackduan@163.com", StrUtil.hide("jackduan@163.com", 16, 17));
	}


	@Test
	public void isCharEqualsTest() {
		final String a = "aaaaaaaaa";
		assertTrue(StrUtil.isCharEquals(a));
	}

	@Test
	public void isNumericTest() {
		final String a = "2142342422423423";
		assertTrue(StrUtil.isNumeric(a));
	}

	@Test
	public void containsAllTest() {
		final String a = "2142342422423423";
		assertTrue(StrUtil.containsAll(a, "214", "234"));
	}

	@Test
	public void replaceLastTest() {
		final String str = "i am jackjack";
		final String result = StrUtil.replaceLast(str, "JACK", null, true);
		assertEquals("i am jack", result);
	}

	@Test
	public void replaceFirstTest() {
		final String str = "yesyes i do";
		final String result = StrUtil.replaceFirst(str, "YES", "", true);
		assertEquals("yes i do", result);
	}

	@Test
	public void replaceFirstWithSupplementaryCharTest() {
		// 增补字符（如emoji）占2个char，replaceFirst按码点定位，否则替换位置会偏移
		assertEquals("😀Xbc", StrUtil.replaceFirst("😀abc", "a", "X"));
		// 与兄弟方法保持一致
		assertEquals("😀Xbc", StrUtil.replace("😀abc", "a", "X"));
		assertEquals("😀Xbc", StrUtil.replaceLast("😀abc", "a", "X"));

		// 增补字符出现在中间
		assertEquals("ab😀Xd", StrUtil.replaceFirst("ab😀cd", "c", "X"));
		// 增补字符在末尾
		assertEquals("a😀X", StrUtil.replaceFirst("a😀b", "b", "X"));
		// 连续多个增补字符
		assertEquals("😀😀Y", StrUtil.replaceFirst("😀😀x", "x", "Y"));
		// 被查找的字符串本身含有增补字符
		assertEquals("Xbc", StrUtil.replaceFirst("😀abc", "😀a", "X"));

		// 不含增补字符时行为不变
		assertEquals("Xbc", StrUtil.replaceFirst("abc", "a", "X"));
		assertEquals("abc", StrUtil.replaceFirst("abc", "z", "X"));
	}

	@Test
	public void issueI5YN49Test() {
		final String str = "A5E6005700000000000000000000000000000000000000090D0100000000000001003830";
		assertEquals("38", StrUtil.subWithLength(str, -2, 2));
	}

	@Test
	public void issueI6KKFUTest() {
		// https://gitee.com/chinabugotech/hutool/issues/I6KKFU
		final String template = "I''m {0} years old.";
		final String result = StrUtil.indexedFormat(template, 10);
		assertEquals("I'm 10 years old.", result);
	}

	@Test
	public void truncateUtf8Test() {
		final String str = "这是This一段中英文";
		String ret = StrUtil.truncateUtf8(str, 12);
		assertEquals("这是Thi...", ret);

		ret = StrUtil.truncateUtf8(str, 13);
		assertEquals("这是This...", ret);

		ret = StrUtil.truncateUtf8(str, 14);
		assertEquals("这是This...", ret);

		ret = StrUtil.truncateUtf8(str, 999);
		assertEquals(str, ret);
	}

	@Test
	public void truncateUtf8Test2() {
		final String str = "这是This一";
		final String ret = StrUtil.truncateUtf8(str, 13);
		assertEquals("这是This一", ret);
	}

	@Test
	public void truncateUtf8Test3() {
		final String str = "一二三四";
		final String ret = StrUtil.truncateUtf8(str, 11);
		assertEquals("一二...", ret);
	}

	@Test
	public void truncateByByteLengthTest() {
		final String str = "This is English";
		final String ret = StrUtil.truncateByByteLength(str, StandardCharsets.ISO_8859_1, 10, 1, false);
		assertEquals("This is En", ret);
	}

	@Test
	public void issueTest() {
		final String s = "abc";
		final String r = StrUtil.truncateByByteLength(s, CharsetUtil.CHARSET_UTF_8, 2, 4, true);
		assertEquals("ab", r);
	}
}
