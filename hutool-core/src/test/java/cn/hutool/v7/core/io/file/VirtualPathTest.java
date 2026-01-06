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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * VirtualPath单元测试
 */
public class VirtualPathTest {

	@Test
	public void testConstructor() {
		final byte[] content = "Hello World".getBytes();
		final VirtualPath virtualPath = new VirtualPath("/tmp/test.txt", new BytesResource(content));

		assertEquals("/tmp/test.txt", virtualPath.toString());
		assertArrayEquals(content, virtualPath.getBytes());
	}

	@Test
	public void testGetFileName() {
		final byte[] content = "Hello World".getBytes();
		final VirtualPath virtualPath = new VirtualPath("/tmp/test.txt", new BytesResource(content));
		final Path fileName = virtualPath.getFileName();

		assertEquals("test.txt", fileName.toString());
		assertArrayEquals(content, ((VirtualPath) fileName).getBytes());
	}

	@Test
	public void testGetParent() {
		final VirtualPath virtualPath = new VirtualPath("/tmp/subdir/test.txt", null);
		final Path parent = virtualPath.getParent();

		assertEquals("/tmp/subdir", parent.toString());
		assertNull(((VirtualPath) parent).getContent());
	}

	@Test
	public void testGetNameCount() {
		final String pathStr = "/tmp/subdir/test.txt";

		final VirtualPath virtualPath = new VirtualPath(pathStr, null);
		assertEquals(Paths.get(pathStr).getNameCount(), virtualPath.getNameCount());

		final VirtualPath rootPath = new VirtualPath("/", null);
		assertEquals(Paths.get("/").getNameCount(), rootPath.getNameCount());

		final VirtualPath emptyPath = new VirtualPath("", null);
		assertEquals(Paths.get("").getNameCount(), emptyPath.getNameCount());
	}

	@Test
	public void testGetName() {
		final byte[] content = "Hello World".getBytes();
		final VirtualPath virtualPath = new VirtualPath("/tmp/subdir/test.txt", new BytesResource(content));

		final Path name0 = virtualPath.getName(0);
		assertEquals("tmp", name0.toString());
		assertNull(((VirtualPath) name0).getBytes());

		final Path name1 = virtualPath.getName(1);
		assertEquals("subdir", name1.toString());
		assertNull(((VirtualPath) name1).getBytes());

		final Path name2 = virtualPath.getName(2);
		assertEquals("test.txt", name2.toString());
		assertArrayEquals(content, ((VirtualPath) name2).getBytes());
	}

	@Test
	public void testSubpath() {
		final VirtualPath virtualPath = new VirtualPath("/tmp/subdir/test.txt", null);

		final Path subpath = virtualPath.subpath(1, 3);
		assertEquals("subdir/test.txt", subpath.toString());

		final Path single = virtualPath.subpath(0, 1);
		assertEquals("tmp", single.toString());
	}

	@Test
	public void testStartsWith() {
		final VirtualPath virtualPath = new VirtualPath("/tmp/subdir/test.txt", null);

		assertTrue(virtualPath.startsWith("/tmp"));
		assertTrue(virtualPath.startsWith(new VirtualPath("/tmp", null)));
		assertFalse(virtualPath.startsWith("/home"));
	}

	@Test
	public void testEndsWith() {
		final VirtualPath virtualPath = new VirtualPath("/tmp/subdir/test.txt", null);

		assertTrue(virtualPath.endsWith("test.txt"));
		assertTrue(virtualPath.endsWith(new VirtualPath("test.txt", null)));
		assertFalse(virtualPath.endsWith("config.txt"));
	}

	@Test
	public void testResolve() {
		final VirtualPath virtualPath = new VirtualPath("/tmp", null);

		final Path resolved = virtualPath.resolve("test.txt");
		assertEquals("/tmp/test.txt", resolved.toString());

		final Path resolvedPath = virtualPath.resolve(new VirtualPath("subdir/test.txt", new BytesResource("content".getBytes())));
		assertEquals("/tmp/subdir/test.txt", resolvedPath.toString());
	}

	@Test
	public void testResolveSibling() {
		final VirtualPath virtualPath = new VirtualPath("/tmp/test.txt", null);

		final Path resolved = virtualPath.resolveSibling("config.txt");
		assertEquals("/tmp/config.txt", resolved.toString());
	}

	@Test
	public void testRelativize() {
		final VirtualPath path1 = new VirtualPath("/tmp/subdir", null);
		final VirtualPath path2 = new VirtualPath("/tmp/subdir/test.txt", null);

		final Path relativized = path1.relativize(path2);
		assertEquals("test.txt", relativized.toString());
	}

	@Test
	public void testToFile() {
		final byte[] content = "Hello World".getBytes();
		final VirtualPath virtualPath = new VirtualPath("/tmp/test.txt", new BytesResource( content));

		final File file = virtualPath.toFile();
		assertInstanceOf(VirtualFile.class, file);
		assertEquals("/tmp/test.txt", file.getPath().replace(File.separator, "/"));
		assertArrayEquals(content, ((VirtualFile) file).getBytes());
	}

	@Test
	public void testIterator() {
		final VirtualPath virtualPath = new VirtualPath("/tmp/subdir/test.txt", new BytesResource("content".getBytes()));
		final Iterator<Path> iterator = virtualPath.iterator();

		assertTrue(iterator.hasNext());
		assertEquals("tmp", iterator.next().toString());

		assertTrue(iterator.hasNext());
		assertEquals("subdir", iterator.next().toString());

		assertTrue(iterator.hasNext());
		final Path last = iterator.next();
		assertEquals("test.txt", last.toString());
		assertArrayEquals("content".getBytes(), ((VirtualPath) last).getBytes());

		assertFalse(iterator.hasNext());
	}

	@Test
	public void testCompareTo() {
		final VirtualPath path1 = new VirtualPath("/tmp/a.txt", null);
		final VirtualPath path2 = new VirtualPath("/tmp/b.txt", null);

		assertTrue(path1.compareTo(path2) < 0);
		assertTrue(path2.compareTo(path1) > 0);
		assertEquals(0, path1.compareTo(new VirtualPath("/tmp/a.txt", null)));
	}

	@Test
	public void testEqualsAndHashCode() {
		final VirtualPath path1 = new VirtualPath("/tmp/test.txt", null);
		final VirtualPath path2 = new VirtualPath("/tmp/test.txt", null);
		final VirtualPath path3 = new VirtualPath("/tmp/config.txt", null);

		assertEquals(path1, path2);
		assertNotEquals(path1, path3);
		assertEquals(path1.hashCode(), path2.hashCode());
	}

	@Test
	public void testContentImmutability() {
		final byte[] originalContent = "Hello World".getBytes();
		final VirtualPath virtualPath = new VirtualPath("/tmp/test.txt", new BytesResource(originalContent));

		final byte[] contentFromMethod = virtualPath.getBytes();
		// 修改获取到的内容不应该影响原始内容
		contentFromMethod[0] = 'h';

		// 重新获取内容，应该和原始内容一致
		final byte[] newContentFromMethod = virtualPath.getBytes();
		assertArrayEquals(originalContent, newContentFromMethod);
	}

	@Test
	public void testNullContent() {
		final VirtualPath virtualPath = new VirtualPath("/tmp/test.txt", null);
		assertNull(virtualPath.getContent());
	}
}

