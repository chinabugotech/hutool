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

package cn.hutool.v7.core.text.split;

import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.text.finder.PatternFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link SplitUtil} 单元测试
 * @author Looly
 *
 */
public class SplitUtilTest {

	@Test
	public void issueI6FKSITest(){
		// issue:I6FKSI
		Assertions.assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitByLength("test length 0", 0));
	}

	@Test
	public void splitToLongTest() {
		final String str = "1,2,3,4, 5";
		long[] longArray = SplitUtil.splitTo(str, ",", long[].class);
		Assertions.assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);

		longArray = SplitUtil.splitTo(str, ",", long[].class);
		Assertions.assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);
	}

	@Test
	public void splitToIntTest() {
		final String str = "1,2,3,4, 5";
		int[] intArray = SplitUtil.splitTo(str, ",", int[].class);
		Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);

		intArray = SplitUtil.splitTo(str, ",", int[].class);
		Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);
	}

	@Test
	public void splitTest() {
		final String str = "a,b ,c,d,,e";
		final List<String> split = SplitUtil.split(str, ",", -1, true, true);
		// 测试空是否被去掉
		Assertions.assertEquals(5, split.size());
		// 测试去掉两边空白符是否生效
		Assertions.assertEquals("b", split.get(1));

		final String[] strings = SplitUtil.splitToArray("abc/", StrUtil.SLASH);
		Assertions.assertEquals(2, strings.length);
	}

	@Test
	public void splitToArrayNullTest() {
		final String[] strings = SplitUtil.splitToArray(null, ".");
		Assertions.assertNotNull(strings);
		Assertions.assertEquals(0, strings.length);
	}

	@Test
	public void splitByCharTest(){
		final String str1 = "a, ,efedsfs,   ddf";
		final List<String> split = SplitUtil.split(str1, ",", 0, true, true);

		Assertions.assertEquals("ddf", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitByStrTest(){
		final String str1 = "aabbccaaddaaee";
		final List<String> split = SplitUtil.split(str1, "aa", 0, true, true);
		Assertions.assertEquals("ee", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitByBlankTest(){
		final String str1 = "aa bbccaa     ddaaee";
		final List<String> split = SplitUtil.splitByBlank(str1);
		Assertions.assertEquals("ddaaee", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitPathTest(){
		final String str1 = "/use/local\\bin";
		final List<String> split = SplitUtil.splitPath(str1);
		Assertions.assertEquals("bin", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitMappingTest() {
		final String str = "1.2.";
		final List<Long> split = SplitUtil.split(str, ".", 0, true, true, Long::parseLong);
		Assertions.assertEquals(2, split.size());
		Assertions.assertEquals(Long.valueOf(1L), split.get(0));
		Assertions.assertEquals(Long.valueOf(2L), split.get(1));
	}

	@SuppressWarnings("MismatchedReadAndWriteOfArray")
	@Test
	public void splitEmptyTest(){
		final String str = "";
		final String[] split = str.split(",");
		final String[] strings = SplitUtil.split(str, ",", -1, false, false)
				.toArray(new String[0]);

		Assertions.assertNotNull(strings);
		Assertions.assertArrayEquals(split, strings);

		final String[] strings2 = SplitUtil.split(str, ",", -1, false, true)
				.toArray(new String[0]);
		Assertions.assertEquals(0, strings2.length);

		final List<String> strings3 = SplitUtil.split(str, ",", -1, true, true);
		// 测试空是否被去掉
		Assertions.assertEquals(0, strings3.size());
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void splitNullTest(){
		final String str = null;
		final String[] strings = SplitUtil.split(str, ",", -1, false, false)
				.toArray(new String[0]);
		Assertions.assertNotNull(strings);
		Assertions.assertEquals(0, strings.length);

		final String[] strings2 = SplitUtil.split(str, ",", -1, false, true)
				.toArray(new String[0]);
		Assertions.assertNotNull(strings2);
		Assertions.assertEquals(0, strings2.length);
	}

	/**
	 * https://github.com/chinabugotech/hutool/issues/2099
	 */
	@Test
	public void splitByRegexTest(){
		final String text = "01  821   34567890182345617821";
		List<String> strings = SplitUtil.splitByRegex(text, "21", 0, false, true);
		Assertions.assertEquals(2, strings.size());
		Assertions.assertEquals("01  8", strings.get(0));
		Assertions.assertEquals("   345678901823456178", strings.get(1));

		strings = SplitUtil.splitByRegex(text, "21", 0, false, false);
		Assertions.assertEquals(3, strings.size());
		Assertions.assertEquals("01  8", strings.get(0));
		Assertions.assertEquals("   345678901823456178", strings.get(1));
		Assertions.assertEquals("", strings.get(2));
	}

	@Test
	void issue3421Test() {
		List<String> strings = SplitUtil.splitByRegex("", "", 0, false, false);
		Assertions.assertEquals(ListUtil.of(""), strings);

		strings = SplitUtil.splitByRegex("aaa", "", 0, false, false);
		Assertions.assertEquals(ListUtil.of("aaa"), strings);

		strings = SplitUtil.splitByRegex("", "aaa", 0, false, false);
		Assertions.assertEquals(ListUtil.of(""), strings);

		strings = SplitUtil.splitByRegex("", "", 0, false, true);
		Assertions.assertEquals(ListUtil.of(), strings);
	}

	@Test
	void issue3421Test2() {
		// 测试在无前置判断时，是否死循环
		final SplitIter splitIter = new SplitIter("", new PatternFinder(Pattern.compile("")), -1, false);
		final List<String> list = splitIter.toList(false);
		Assertions.assertEquals(ListUtil.of(""), list);
	}
}
