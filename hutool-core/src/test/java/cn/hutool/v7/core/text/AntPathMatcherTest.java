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

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class AntPathMatcherTest {

	@Test
	public void matchesTest() {
		final AntPathMatcher antPathMatcher = new AntPathMatcher();
		final boolean matched = antPathMatcher.match("/api/org/organization/{orgId}", "/api/org/organization/999");
		assertTrue(matched);
	}

	@Test
	public void matchesTest2() {
		final AntPathMatcher antPathMatcher = new AntPathMatcher();

		String pattern = "/**/*.xml*";
		String path = "/WEB-INF/web.xml";
		boolean isMatched = antPathMatcher.match(pattern, path);
		assertTrue(isMatched);

		pattern = "org/codelabor/*/**/*Service";
		path = "org/codelabor/example/HelloWorldService";
		isMatched = antPathMatcher.match(pattern, path);
		assertTrue(isMatched);

		pattern = "org/codelabor/*/**/*Service?";
		path = "org/codelabor/example/HelloWorldServices";
		isMatched = antPathMatcher.match(pattern, path);
		assertTrue(isMatched);
	}

	@Test
	public void matchesTest3(){
		final AntPathMatcher pathMatcher = new AntPathMatcher();
		pathMatcher.setCachePatterns(true);
		pathMatcher.setCaseSensitive(true);
		pathMatcher.setPathSeparator("/");
		pathMatcher.setTrimTokens(true);

		assertTrue(pathMatcher.match("a", "a"));
		assertTrue(pathMatcher.match("a*", "ab"));
		assertTrue(pathMatcher.match("a*/**/a", "ab/asdsa/a"));
		assertTrue(pathMatcher.match("a*/**/a", "ab/asdsa/asdasd/a"));

		assertTrue(pathMatcher.match("*", "a"));
		assertTrue(pathMatcher.match("*/*", "a/a"));
	}

	/**
	 * AntPathMatcher默认路径分隔符为“/”，而在匹配文件路径时，需要注意Windows下路径分隔符为“\”，Linux下为“/”。靠谱写法如下两种方式：
	 * AntPathMatcher matcher = new AntPathMatcher(File.separator);
	 * AntPathMatcher matcher = new AntPathMatcher(System.getProperty("file.separator"));
	 */
	@Test
	public void matchesTest4() {
		final AntPathMatcher pathMatcher = new AntPathMatcher();

		// 精确匹配
		assertTrue(pathMatcher.match("/test", "/test"));
		assertFalse(pathMatcher.match("test", "/test"));

		//测试通配符?
		assertTrue(pathMatcher.match("t?st", "test"));
		assertTrue(pathMatcher.match("te??", "test"));
		assertFalse(pathMatcher.match("tes?", "tes"));
		assertFalse(pathMatcher.match("tes?", "testt"));

		//测试通配符*
		assertTrue(pathMatcher.match("*", "test"));
		assertTrue(pathMatcher.match("test*", "test"));
		assertTrue(pathMatcher.match("test/*", "test/Test"));
		assertTrue(pathMatcher.match("*.*", "test."));
		assertTrue(pathMatcher.match("*.*", "test.test.test"));
		assertFalse(pathMatcher.match("test*", "test/")); //注意这里是false 因为路径不能用*匹配
		assertFalse(pathMatcher.match("test*", "test/t")); //这同理
		assertFalse(pathMatcher.match("test*aaa", "testblaaab")); //这个是false 因为最后一个b无法匹配了 前面都是能匹配成功的

		//测试通配符** 匹配多级URL
		assertTrue(pathMatcher.match("/*/**", "/testing/testing"));
		assertTrue(pathMatcher.match("/**/*", "/testing/testing"));
		assertTrue(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla/bla")); //这里也是true哦
		assertFalse(pathMatcher.match("/bla*bla/test", "/blaXXXbl/test"));

		assertFalse(pathMatcher.match("/????", "/bala/bla"));
		assertFalse(pathMatcher.match("/**/*bla", "/bla/bla/bla/bbb"));

		assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
		assertTrue(pathMatcher.match("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
		assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
		assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));
		assertTrue(pathMatcher.match("/foo/bar/**", "/foo/bar"));

		//这个需要特别注意：{}里面的相当于Spring MVC里接受一个参数一样，所以任何东西都会匹配的
		assertTrue(pathMatcher.match("/{bla}.*", "/testing.html"));
		assertFalse(pathMatcher.match("/{bla}.htm", "/testing.html")); //这样就是false了
	}

	/**
	 * 测试 URI 模板变量提取
	 */
	@Test
	public void testExtractUriTemplateVariables() {
		final AntPathMatcher antPathMatcher = new AntPathMatcher();
		final HashMap<String, String> map = (HashMap<String, String>) antPathMatcher.extractUriTemplateVariables("/api/org/organization/{orgId}",
				"/api/org" +
						"/organization" +
						"/999");
		assertEquals(1, map.size());
	}

	/**
	 * Test that the pattern comparator correctly handles ".*" in the middle of a pattern.
	 * The bug: substring(pos - 1) without end index returns the rest of the string,
	 * not just two characters, so ".*" followed by more chars is miscounted.
	 */
	@Test
	public void testPatternComparatorDotStarInMiddle() {
		final AntPathMatcher matcher = new AntPathMatcher();
		final Comparator<String> comparator = matcher.getPatternComparator("/files/file.txt");

		// Pattern A: "/files/.*.txt" - the ".*" should NOT count as a single wildcard
		// because ".*" is a special pattern (match any extension), not a standalone "*"
		// Correct total count: 0 (no single wildcards, no uri vars, no double wildcards)
		//
		// Pattern B: "/files/*a.txt" - the "*" preceded by "/" IS a single wildcard
		// Correct total count: 1 (one single wildcard)
		//
		// Pattern A should be more specific (fewer wildcards), so compare(A, B) < 0
		final int result = comparator.compare("/files/.*.txt", "/files/*a.txt");
		assertTrue(result < 0, "Pattern with .* should be more specific than pattern with *; " +
			"got compare result: " + result);
	}
}
