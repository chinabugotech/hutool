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

package cn.hutool.v7.core.io.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileNameUtilTest {
	@Test
	public void cleanInvalidTest(){
		String name = FileNameUtil.cleanInvalid("1\n2\n");
		assertEquals("12", name);

		name = FileNameUtil.cleanInvalid("\r1\r\n2\n");
		assertEquals("12", name);
	}

	@Test
	public void mainNameTest() {
		final String s = FileNameUtil.mainName("abc.tar.gz");
		assertEquals("abc", s);
	}

	@Test
	public void normalizeTest() {
		assertEquals("/foo/", FileNameUtil.normalize("/foo//"));
		assertEquals("/foo/", FileNameUtil.normalize("/foo/./"));
		assertEquals("/bar", FileNameUtil.normalize("/foo/../bar"));
		assertEquals("/bar/", FileNameUtil.normalize("/foo/../bar/"));
		assertEquals("/baz", FileNameUtil.normalize("/foo/../bar/../baz"));
		assertEquals("/", FileNameUtil.normalize("/../"));
		assertEquals("foo", FileNameUtil.normalize("foo/bar/.."));
		assertEquals("../bar", FileNameUtil.normalize("foo/../../bar"));
		assertEquals("bar", FileNameUtil.normalize("foo/../bar"));
		assertEquals("/server/bar", FileNameUtil.normalize("//server/foo/../bar"));
		assertEquals("/bar", FileNameUtil.normalize("//server/../bar"));
		assertEquals("C:/bar", FileNameUtil.normalize("C:\\foo\\..\\bar"));
		//
		assertEquals("C:/bar", FileNameUtil.normalize("C:\\..\\bar"));
		assertEquals("../../bar", FileNameUtil.normalize("../../bar"));
		assertEquals("C:/bar", FileNameUtil.normalize("/C:/bar"));
		assertEquals("C:", FileNameUtil.normalize("C:"));

		// issue#3253，smb保留格式
		assertEquals("\\\\192.168.1.1\\Share\\", FileNameUtil.normalize("\\\\192.168.1.1\\Share\\"));
	}

	@Test
	public void normalizeBlankTest() {
		assertEquals("C:/aaa ", FileNameUtil.normalize("C:\\aaa "));
	}

	@Test
	void renameMainTest() {
		assertEquals("1.pdf", FileNameUtil.renameMain("a.b.pdf", "1"));
		assertEquals("a.pdf", FileNameUtil.renameMain(null, "a.pdf"));
		assertEquals("a.pdf", FileNameUtil.renameMain("", "a.pdf"));
	}

	@Test
	public void extNameAndMainNameBugTest() {
		// 正确，输出前缀为 "app-v2.3.1-star"
		assertEquals("app-v2.3.1-star",FileNameUtil.mainName("app-v2.3.1-star.gz"));
		// 当前代码会失败，预期后缀结果 "gz"，但是输出 "star.gz"
		assertEquals("gz", FileNameUtil.extName("app-v2.3.1-star.gz"));
	}
}
