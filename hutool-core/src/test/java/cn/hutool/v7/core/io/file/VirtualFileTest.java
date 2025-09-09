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
