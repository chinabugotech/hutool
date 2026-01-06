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

import cn.hutool.v7.core.io.resource.BytesResource;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class VirtualFileTest {
	@Test
	public void testConstructorWithParentChildAndContent() {
		final byte[] content = "Hello World".getBytes();
		final VirtualFile virtualFile = new VirtualFile("/tmp", "test.txt", new BytesResource(content));

		assertEquals("/tmp", virtualFile.getParent().replace(File.separator, "/"));
		assertEquals("test.txt", virtualFile.getName());
		assertArrayEquals(content, virtualFile.getBytes());
	}

	@Test
	public void testConstructorWithStringAndContent() {
		final byte[] content = "Hello World".getBytes();
		final VirtualFile virtualFile = new VirtualFile("/tmp/test.txt", new BytesResource(content));

		assertEquals("/tmp/test.txt", virtualFile.getPath().replace(File.separator, "/"));
		assertArrayEquals(content, virtualFile.getBytes());
	}

	@Test
	public void testConstructorWithFileParentAndContent() {
		final File parent = new File("/tmp");
		final byte[] content = "Hello World".getBytes();
		final VirtualFile virtualFile = new VirtualFile(parent, "test.txt", new BytesResource(content));

		assertEquals("/tmp", virtualFile.getParent().replace(File.separator, "/"));
		assertEquals("test.txt", virtualFile.getName());
		assertArrayEquals(content, virtualFile.getBytes());
	}

	@Test
	public void testFileProperties() {
		final byte[] content = "Hello World".getBytes();
		final VirtualFile virtualFile = new VirtualFile("/tmp/test.txt", new BytesResource(content));

		assertTrue(virtualFile.exists());
		assertTrue(virtualFile.isFile());
		assertFalse(virtualFile.isDirectory());
		assertEquals(content.length, virtualFile.length());
		assertTrue(virtualFile.canRead());
		assertFalse(virtualFile.canWrite());
		assertFalse(virtualFile.canExecute());
		assertTrue(virtualFile.lastModified() > 0);
	}

	@Test
	public void testContentImmutability() {
		final byte[] originalContent = "Hello World".getBytes();
		final VirtualFile virtualFile = new VirtualFile("/tmp/test.txt", new BytesResource(originalContent));

		final byte[] contentFromMethod = virtualFile.getBytes();
		// 修改获取到的内容不应该影响原始内容
		contentFromMethod[0] = 'h';

		// 重新获取内容，应该和原始内容一致
		final byte[] newContentFromMethod = virtualFile.getBytes();
		assertArrayEquals(originalContent, newContentFromMethod);
	}

	@Test
	public void testNullContent() {
		final VirtualFile virtualFile = new VirtualFile("/tmp/test.txt", null);
		assertNull(virtualFile.getBytes());
	}
}
