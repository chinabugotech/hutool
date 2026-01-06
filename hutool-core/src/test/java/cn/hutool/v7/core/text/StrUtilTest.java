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

import cn.hutool.v7.core.map.Dict;
import cn.hutool.v7.core.text.split.SplitUtil;
import cn.hutool.v7.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * 字符串工具类单元测试
 *
 * @author Looly
 */
public class StrUtilTest {
	@Test
	public void isBlankTest() {
		final String blank = "	  　";
		Assertions.assertTrue(StrUtil.isBlank(blank));
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
		Assertions.assertArrayEquals(new String[]{"aaab", "bbcc", "cddd", "aadf", "dfsd", "fsdf", "0"}, cut);
	}

	@Test
	public void splitTest2() {
		final String str = "a.b.";
		final List<String> split = SplitUtil.split(str, ".");
		assertEquals(3, split.size());
		assertEquals("b", split.get(1));
		assertEquals("", split.get(2));
	}

	@Test
	public void splitNullTest() {
		assertEquals(0, SplitUtil.split(null, ".").size());
	}

	@Test
	public void formatTest() {
		final String template = "你好，我是{name}，我的电话是：{phone}";
		final String result = StrUtil.formatByMap(template, Dict.of().set("name", "张三").set("phone", "13888881111"));
		assertEquals("你好，我是张三，我的电话是：13888881111", result);

		final String result2 = StrUtil.formatByMap(template, Dict.of().set("name", "张三").set("phone", null));
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

		final String emoji = StrUtil.replaceByCodePoint("\uD83D\uDE00aabb\uD83D\uDE00ccdd", 2, 6, '*');
		assertEquals("\uD83D\uDE00a****ccdd", emoji);
	}

	@Test
	public void replaceTest2() {
		final String result = StrUtil.replace("123", "2", "3");
		assertEquals("133", result);
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
	public void subPreTest() {
		Assertions.assertNull(StrUtil.subPre(null, 3));
		assertEquals("ab", StrUtil.subPre("ab", 3));
		assertEquals("abc", StrUtil.subPre("abc", 3));
		assertEquals("abc", StrUtil.subPre("abcd", 3));
		assertEquals("a", StrUtil.subPre("abcd", -3));
		assertEquals("ab", StrUtil.subPre("ab", 3));
	}

	@Test
	public void subByCodePointTest() {
		// 🤔👍🍓🤔
		final String test = "\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53\uD83E\uDD14";

		// 不正确的子字符串
		final String wrongAnswer = StrUtil.sub(test, 0, 3);
		Assertions.assertNotEquals("\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53", wrongAnswer);

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
	public void testMoveLocalCyclicShift() {
		//Case 1:"12"右移动4位
		final String result1 = StrUtil.move("12345", 0, 2, 4);
		assertEquals("12345", result1);
		//Case 2:"12"左移1位
		final String result2 = StrUtil.move("12345", 0, 2, -1);
		assertEquals("34512", result2);
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
	public void limitLengthTest() {
		final String text = "我是一段正文，很长的正文，需要截取的正文";
		String str = StrUtil.limitLength(text, 5);
		assertEquals("我是一段正...", str);
		str = StrUtil.limitLength(text, 21);
		assertEquals(text, str);
		str = StrUtil.limitLength(text, 50);
		assertEquals(text, str);
	}

	@Test
	public void containsAnyTest() {
		//字符
		boolean containsAny = StrUtil.containsAny("aaabbbccc", 'a', 'd');
		Assertions.assertTrue(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", 'e', 'd');
		Assertions.assertFalse(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", 'd', 'c');
		Assertions.assertTrue(containsAny);

		//字符串
		containsAny = StrUtil.containsAny("aaabbbccc", "a", "d");
		Assertions.assertTrue(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", "e", "d");
		Assertions.assertFalse(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", "d", "c");
		Assertions.assertTrue(containsAny);

		// https://gitee.com/chinabugotech/hutool/issues/I7WSYD
		containsAny = StrUtil.containsAny("你好啊", "嗯", null);
		Assertions.assertFalse(containsAny);
	}

	@Test
	public void centerTest() {
		Assertions.assertNull(StrUtil.center(null, 10));
		assertEquals("    ", StrUtil.center("", 4));
		assertEquals("ab", StrUtil.center("ab", -1));
		assertEquals(" ab ", StrUtil.center("ab", 4));
		assertEquals("abcd", StrUtil.center("abcd", 2));
		assertEquals(" a  ", StrUtil.center("a", 4));
	}

	@Test
	public void padPreTest() {
		Assertions.assertNull(StrUtil.padPre(null, 10, ' '));
		assertEquals("001", StrUtil.padPre("1", 3, '0'));
		assertEquals("12", StrUtil.padPre("123", 2, '0'));

		Assertions.assertNull(StrUtil.padPre(null, 10, "AA"));
		assertEquals("AB1", StrUtil.padPre("1", 3, "ABC"));
		assertEquals("12", StrUtil.padPre("123", 2, "ABC"));
	}

	@Test
	public void padAfterTest() {
		Assertions.assertNull(StrUtil.padAfter(null, 10, ' '));
		assertEquals("100", StrUtil.padAfter("1", 3, '0'));
		assertEquals("23", StrUtil.padAfter("123", 2, '0'));
		assertEquals("", StrUtil.padAfter("123", -1, '0'));

		Assertions.assertNull(StrUtil.padAfter(null, 10, "ABC"));
		assertEquals("1AB", StrUtil.padAfter("1", 3, "ABC"));
		assertEquals("23", StrUtil.padAfter("123", 2, "ABC"));
	}

	@Test
	public void subBetweenAllTest() {
		Assertions.assertArrayEquals(new String[]{"yz", "abc"}, StrUtil.subBetweenAll("saho[yz]fdsadp[abc]a", "[", "]"));
		Assertions.assertArrayEquals(new String[]{"abc"}, StrUtil.subBetweenAll("saho[yzfdsadp[abc]a]", "[", "]"));
		Assertions.assertArrayEquals(new String[]{"abc", "abc"}, StrUtil.subBetweenAll("yabczyabcz", "y", "z"));
		Assertions.assertArrayEquals(new String[0], StrUtil.subBetweenAll(null, "y", "z"));
		Assertions.assertArrayEquals(new String[0], StrUtil.subBetweenAll("", "y", "z"));
		Assertions.assertArrayEquals(new String[0], StrUtil.subBetweenAll("abc", null, "z"));
		Assertions.assertArrayEquals(new String[0], StrUtil.subBetweenAll("abc", "y", null));
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
		final String str = RandomUtil.randomStringLower(RandomUtil.randomInt(1, 100));
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
		String[] strings = StrUtil.wrapAll("`", "`", SplitUtil.splitToArray("1,2,3,4", ","));
		assertEquals("[`1`, `2`, `3`, `4`]", StrUtil.utf8Str(strings));

		strings = StrUtil.wrapAllWithPair("`", SplitUtil.splitToArray("1,2,3,4", ","));
		assertEquals("[`1`, `2`, `3`, `4`]", StrUtil.utf8Str(strings));
	}

	@Test
	public void startWithTest() {
		final String a = "123";
		final String b = "123";

		Assertions.assertTrue(StrUtil.startWith(a, b));
		Assertions.assertFalse(StrUtil.startWithIgnoreEquals(a, b));
	}

	@Test
	public void indexedFormatTest() {
		final String ret = StrUtil.indexedFormat("this is {0} for {1}", "a", 1000);
		assertEquals("this is a for 1,000", ret);
	}

	@Test
	public void hideTest() {
		Assertions.assertNull(StrUtil.hide(null, 1, 1));
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
		Assertions.assertTrue(StrUtil.isCharEquals(a));
	}

	@Test
	public void isNumericTest() {
		final String a = "2142342422423423";
		Assertions.assertTrue(StrUtil.isNumeric(a));
	}

	@Test
	public void testReplace2() {
		// https://gitee.com/chinabugotech/hutool/issues/I4M16G
		final String replace = "#{A}";
		final String result = StrUtil.replace(replace, "#{AAAAAAA}", "1");
		assertEquals(replace, result);
	}

	@Test
	public void testReplaceByStr() {
		final String replace = "SSM15930297701BeryAllen";
		final String result = StrUtil.replaceByCodePoint(replace, 5, 12, "***");
		assertEquals("SSM15***01BeryAllen", result);

		final String emoji = StrUtil.replaceByCodePoint("\uD83D\uDE00aabb\uD83D\uDE00ccdd", 2, 6, "***");
		assertEquals("\uD83D\uDE00a***ccdd", emoji);
	}

	@Test
	public void testAddPrefixIfNot() {
		final String str = "hutool";
		String result = StrUtil.addPrefixIfNot(str, "hu");
		assertEquals(str, result);

		result = StrUtil.addPrefixIfNot(str, "Good");
		assertEquals("Good" + str, result);
	}

	@Test
	public void testAddSuffixIfNot() {
		final String str = "hutool";
		String result = StrUtil.addSuffixIfNot(str, "tool");
		assertEquals(str, result);

		result = StrUtil.addSuffixIfNot(str, " is Good");
		assertEquals(str + " is Good", result);

		result = StrUtil.addSuffixIfNot("", "/");
		assertEquals("/", result);
	}

	@Test
	public void issue2628Test(){
		final String s = StrUtil.indexedFormat("a{0,number,#}", 1234567);
		assertEquals("a1234567", s);
	}

	@Test
	void pr1308Test(){
		final String concat = StrUtil.concat(true,
			"start",
			StrUtil.subPre("123", 2),
			"middle",
			StrUtil.subSuf("123", 2),
			"end");

		assertEquals("start12middle3end", concat);
	}

	@Test
	public void testByteBufferSideEffect() {
		final String originalText = "Hello";
		final ByteBuffer buffer = ByteBuffer.wrap(originalText.getBytes(StandardCharsets.UTF_8));
		// 此时 buffer.remaining() == 5
		assertEquals(5, buffer.remaining());

		// 调用工具类转换，打印buffer内容
		final String result = StrUtil.str(buffer, StandardCharsets.UTF_8);
		assertEquals(originalText, result);

		// 预期：
		// 工具类不应该修改原 buffer 的指针，remaining 应该依然为 5
		// 再次调用工具类转换，输出结果应该不变
		assertEquals(originalText, StrUtil.str(buffer, StandardCharsets.UTF_8));
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
}
