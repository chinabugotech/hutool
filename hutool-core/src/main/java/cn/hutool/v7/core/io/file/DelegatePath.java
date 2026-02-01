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

import cn.hutool.v7.core.exception.HutoolException;
import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.core.io.resource.Resource;
import cn.hutool.v7.core.lang.wrapper.SimpleWrapper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.FileSystem;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 代理路径类，实现Path接口，用于代理一个实际的Path对象，并提供对Files类功能的访问
 *
 * @author Looly
 * @since 7.0.0
 */
public class DelegatePath extends SimpleWrapper<Path> implements Path, Resource {

	// region ----- Constructor

	/**
	 * 构造
	 *
	 * @param first 路径的第一个元素
	 * @param more  更多路径
	 */
	public DelegatePath(final String first, final String... more) {
		this(Paths.get(first, more));
	}

	/**
	 * 构造
	 *
	 * @param uri URI路径
	 */
	public DelegatePath(final URI uri) {
		this(Paths.get(uri));
	}

	/**
	 * 构造一个代理路径
	 *
	 * @param path 被代理的路径对象
	 */
	public DelegatePath(final Path path) {
		super(path);
	}
	// endregion

	/**
	 * 获取被代理的路径对象
	 *
	 * @return 被代理的路径对象
	 */
	public Path getRawPath() {
		return this.raw;
	}

	@Override
	public FileSystem getFileSystem() {
		return raw.getFileSystem();
	}

	@Override
	public boolean isAbsolute() {
		return raw.isAbsolute();
	}

	@Override
	public Path getRoot() {
		final Path root = raw.getRoot();
		return root == null ? null : new DelegatePath(root);
	}

	@Override
	public Path getFileName() {
		final Path fileName = raw.getFileName();
		return fileName == null ? null : new DelegatePath(fileName);
	}

	@Override
	public Path getParent() {
		final Path parent = raw.getParent();
		return parent == null ? null : new DelegatePath(parent);
	}

	@Override
	public int getNameCount() {
		return raw.getNameCount();
	}

	@Override
	public Path getName(final int index) {
		return new DelegatePath(raw.getName(index));
	}

	@Override
	public Path subpath(final int beginIndex, final int endIndex) {
		return new DelegatePath(raw.subpath(beginIndex, endIndex));
	}

	@Override
	public boolean startsWith(final Path other) {
		if (other instanceof DelegatePath) {
			return raw.startsWith(((DelegatePath) other).raw);
		}
		return raw.startsWith(other);
	}

	@Override
	public boolean startsWith(final String other) {
		return raw.startsWith(other);
	}

	@Override
	public boolean endsWith(final Path other) {
		if (other instanceof DelegatePath) {
			return raw.endsWith(((DelegatePath) other).raw);
		}
		return raw.endsWith(other);
	}

	@Override
	public boolean endsWith(final String other) {
		return raw.endsWith(other);
	}

	@Override
	public Path normalize() {
		return new DelegatePath(raw.normalize());
	}

	@Override
	public Path resolve(final Path other) {
		if (other instanceof DelegatePath) {
			return new DelegatePath(raw.resolve(((DelegatePath) other).raw));
		}
		return new DelegatePath(raw.resolve(other));
	}

	@Override
	public Path resolve(final String other) {
		return new DelegatePath(raw.resolve(other));
	}

	@Override
	public Path resolveSibling(final Path other) {
		if (other instanceof DelegatePath) {
			return new DelegatePath(raw.resolveSibling(((DelegatePath) other).raw));
		}
		return new DelegatePath(raw.resolveSibling(other));
	}

	@Override
	public Path resolveSibling(final String other) {
		return new DelegatePath(raw.resolveSibling(other));
	}

	@Override
	public Path relativize(final Path other) {
		if (other instanceof DelegatePath) {
			return new DelegatePath(raw.relativize(((DelegatePath) other).raw));
		}
		return new DelegatePath(raw.relativize(other));
	}

	@Override
	public URI toUri() {
		return raw.toUri();
	}

	@Override
	public Path toAbsolutePath() {
		return new DelegatePath(raw.toAbsolutePath());
	}

	@Override
	public Path toRealPath(final LinkOption... options) throws IOException {
		return new DelegatePath(raw.toRealPath(options));
	}

	@Override
	public File toFile() {
		return raw.toFile();
	}

	@Override
	public WatchKey register(final WatchService watcher, final WatchEvent.Kind<?>[] events, final WatchEvent.Modifier... modifiers) throws IOException {
		return raw.register(watcher, events, modifiers);
	}

	@Override
	public WatchKey register(final WatchService watcher, final WatchEvent.Kind<?>... events) throws IOException {
		return raw.register(watcher, events);
	}

	@Override
	public Iterator<Path> iterator() {
		return new Iterator<>() {
			private final Iterator<Path> itr = raw.iterator();

			@Override
			public boolean hasNext() {
				return itr.hasNext();
			}

			@Override
			public Path next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return new DelegatePath(itr.next());
			}
		};
	}

	@Override
	public int compareTo(final Path other) {
		if (other instanceof DelegatePath) {
			return raw.compareTo(((DelegatePath) other).raw);
		}
		return raw.compareTo(other);
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (other instanceof DelegatePath) {
			return raw.equals(((DelegatePath) other).raw);
		}
		if (other instanceof Path) {
			return raw.equals(other);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return raw.hashCode();
	}

	@Override
	public String toString() {
		return raw.toString();
	}

	// 添加对Files类功能的便捷访问方法

	/**
	 * 检查文件是否存在
	 *
	 * @param options 检查选项
	 * @return 文件是否存在
	 * @see Files#exists(Path, LinkOption...)
	 */
	public boolean exists(final LinkOption... options) {
		return Files.exists(raw, options);
	}

	/**
	 * 检查文件是否为给定文件的子文件
	 *
	 * @param parent 父文件
	 * @return 是否为子文件
	 */
	public boolean isSubOf(final Path parent) {
		return PathUtil.isSub(parent, this.raw);
	}

	/**
	 * 检查文件是否不存在
	 *
	 * @param options 检查选项
	 * @return 文件是否不存在
	 * @see Files#notExists(Path, LinkOption...)
	 */
	public boolean notExists(final LinkOption... options) {
		return Files.notExists(raw, options);
	}

	/**
	 * 检查文件是否为目录
	 *
	 * @param options 检查选项
	 * @return 是否为目录
	 * @see Files#isDirectory(Path, LinkOption...)
	 */
	public boolean isDirectory(final LinkOption... options) {
		return Files.isDirectory(raw, options);
	}

	/**
	 * 检查文件是否为普通文件
	 *
	 * @param options 检查选项
	 * @return 是否为普通文件
	 * @see Files#isRegularFile(Path, LinkOption...)
	 */
	public boolean isFile(final LinkOption... options) {
		return Files.isRegularFile(raw, options);
	}

	/**
	 * 检查文件是否为符号链接
	 *
	 * @return 是否为符号链接
	 * @see Files#isSymbolicLink(Path)
	 */
	public boolean isSymbolicLink() {
		return Files.isSymbolicLink(raw);
	}

	/**
	 * 判断是否为其它类型文件，即非文件、非目录、非链接。
	 *
	 * @return 是否为其他类型
	 */
	public boolean isOther() {
		return PathUtil.isOther(this.raw);
	}

	/**
	 * 检查文件是否可执行
	 *
	 * @return 是否可执行
	 * @see Files#isExecutable(Path)
	 */
	public boolean isExecutable() {
		return Files.isExecutable(raw);
	}

	/**
	 * 检查文件是否可读
	 *
	 * @return 是否可读
	 * @see Files#isReadable(Path)
	 */
	public boolean isReadable() {
		return Files.isReadable(raw);
	}

	/**
	 * 检查文件是否可写
	 *
	 * @return 是否可写
	 * @see Files#isWritable(Path)
	 */
	public boolean isWritable() {
		return Files.isWritable(raw);
	}

	/**
	 * 获取文件大小
	 *
	 * @return 文件大小
	 * @throws IORuntimeException IO异常
	 * @see Files#size(Path)
	 */
	@Override
	public long size() throws IORuntimeException {
		try {
			return Files.size(raw);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public String name() {
		return PathUtil.getName(this.raw);
	}

	/**
	 * 删除文件或目录
	 *
	 * @throws IORuntimeException IO异常
	 * @see Files#delete(Path)
	 */
	public void delete() throws IORuntimeException {
		try {
			Files.delete(raw);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 如果存在则删除文件或目录
	 *
	 * @return 是否删除成功
	 * @throws IORuntimeException IO异常
	 * @see Files#deleteIfExists(Path)
	 */
	public boolean deleteIfExists() throws IORuntimeException {
		try {
			return Files.deleteIfExists(raw);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建目录
	 *
	 * @param attrs 文件属性
	 * @return 创建的目录路径
	 * @throws IORuntimeException IO异常
	 * @see Files#createDirectory(Path, FileAttribute[])
	 */
	public DelegatePath createDirectory(final FileAttribute<?>... attrs) throws IORuntimeException {
		try {
			return new DelegatePath(Files.createDirectory(raw, attrs));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建目录（包括必要时的父目录）
	 *
	 * @param attrs 文件属性
	 * @return 创建的目录路径
	 * @throws IORuntimeException IO异常
	 * @see Files#createDirectories(Path, FileAttribute[])
	 */
	public DelegatePath createDirectories(final FileAttribute<?>... attrs) throws IORuntimeException {
		try {
			return new DelegatePath(Files.createDirectories(raw, attrs));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建文件
	 *
	 * @param attrs 文件属性
	 * @return 创建的文件路径
	 * @throws IORuntimeException IO异常
	 * @see Files#createFile(Path, FileAttribute[])
	 */
	public DelegatePath createFile(final FileAttribute<?>... attrs) throws IORuntimeException {
		try {
			return new DelegatePath(Files.createFile(raw, attrs));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建临时目录
	 *
	 * @param prefix 前缀
	 * @param attrs  文件属性
	 * @return 创建的临时目录路径
	 * @throws IORuntimeException IO异常
	 * @see Files#createTempDirectory(Path, String, FileAttribute[])
	 */
	public DelegatePath createTempDirectory(final String prefix, final FileAttribute<?>... attrs) throws IORuntimeException {
		try {
			return new DelegatePath(Files.createTempDirectory(raw, prefix, attrs));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建临时文件
	 *
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param attrs  文件属性
	 * @return 创建的临时文件路径
	 * @throws IORuntimeException IO异常
	 * @see Files#createTempFile(Path, String, String, FileAttribute[])
	 */
	public DelegatePath createTempFile(final String prefix, final String suffix, final FileAttribute<?>... attrs) throws IORuntimeException {
		try {
			return new DelegatePath(Files.createTempFile(raw, prefix, suffix, attrs));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 复制文件
	 *
	 * @param target  目标路径
	 * @param options 复制选项
	 * @return 目标路径
	 * @throws IORuntimeException IO异常
	 * @see Files#copy(Path, Path, CopyOption...)
	 */
	public DelegatePath copyTo(final Path target, final CopyOption... options) throws IORuntimeException {
		Path acturalTarget = target;
		if (target instanceof DelegatePath) {
			acturalTarget = ((DelegatePath) target).raw;
		}
		try {
			Files.copy(raw, acturalTarget, options);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return new DelegatePath(acturalTarget);
	}

	/**
	 * 移动文件
	 *
	 * @param target  目标路径
	 * @param options 移动选项
	 * @return 目标路径
	 * @throws IORuntimeException IO异常
	 * @see Files#move(Path, Path, CopyOption...)
	 */
	public DelegatePath moveTo(final Path target, final CopyOption... options) throws IORuntimeException {
		Path acturalTarget = target;
		if (target instanceof DelegatePath) {
			acturalTarget = ((DelegatePath) target).raw;
		}
		try {
			Files.move(raw, acturalTarget, options);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return new DelegatePath(acturalTarget);
	}

	/**
	 * 判断文件是否为空目录
	 *
	 * @return 是否为空目录
	 */
	public boolean isDirEmpty() {
		return PathUtil.isDirEmpty(this);
	}

	/**
	 * 列出目录中的所有文件（不会递归子目录）
	 *
	 * @param filter 文件过滤器，null表示不过滤，返回所有文件
	 * @return 文件列表（包含目录）
	 */
	public Path[] listFiles(final Predicate<? super Path> filter) {
		return PathUtil.listFiles(this, filter);
	}

	/**
	 * 便利目录中的所有文件（不会递归子目录）
	 *
	 * @param options  访问选项
	 * @param maxDepth 最大深度
	 * @param visitor  {@link FileVisitor} 接口，用于自定义在访问文件时，访问目录前后等节点做的操作
	 */
	public void walkFiles(final Set<FileVisitOption> options, final int maxDepth, final FileVisitor<? super Path> visitor) {
		try {
			Files.walkFileTree(this.raw, options, maxDepth, visitor);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取文件属性
	 *
	 * @param options 链接选项
	 * @return 文件属性
	 */
	public BasicFileAttributes getAttributes(final LinkOption... options) {
		return PathUtil.getAttributes(this.raw, options);
	}

	/**
	 * 获取文件输入流
	 *
	 * @param options 链接选项
	 * @return 文件输入流
	 * @throws IORuntimeException IO异常
	 */
	public BufferedInputStream getStream(final LinkOption... options) throws IORuntimeException {
		return PathUtil.getInputStream(this, options);
	}

	@Override
	public InputStream getStream() {
		return getStream(new LinkOption[0]);
	}

	@Override
	public URL getUrl() {
		try {
			return this.raw.toUri().toURL();
		} catch (final MalformedURLException e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * 获取文件字符输入流
	 *
	 * @param charset 字符集
	 * @param options 链接选项
	 * @return 文件字符输入流
	 * @throws IORuntimeException IO异常
	 */
	public Reader getReader(final Charset charset, final OpenOption... options) {
		return PathUtil.getReader(this, charset, options);
	}

	@Override
	public byte[] readBytes() throws IORuntimeException {
		return PathUtil.readBytes(this);
	}

	/**
	 * 获取文件输出流
	 *
	 * @param options 链接选项
	 * @return 文件输出流
	 * @throws IORuntimeException IO异常
	 */
	public BufferedOutputStream getOutputStream(final OpenOption... options) throws IORuntimeException {
		return PathUtil.getOutputStream(this, options);
	}

	/**
	 * 获取文件的MIME类型
	 *
	 * @return MIME类型
	 */
	public String getMimeType() {
		return PathUtil.getMimeType(this);
	}
}

