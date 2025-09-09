/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

import cn.hutool.v7.core.io.IORuntimeException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 代理路径类，实现Path接口，用于代理一个实际的Path对象，并提供对Files类功能的访问
 *
 * @author Looly
 * @since 7.0.0
 */
public class DelegatePath implements Path {

	private final Path path;

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
		this.path = path;
	}
	// endregion

	/**
	 * 获取被代理的路径对象
	 *
	 * @return 被代理的路径对象
	 */
	public Path getRawPath() {
		return this.path;
	}

	@Override
	public FileSystem getFileSystem() {
		return path.getFileSystem();
	}

	@Override
	public boolean isAbsolute() {
		return path.isAbsolute();
	}

	@Override
	public Path getRoot() {
		final Path root = path.getRoot();
		return root == null ? null : new DelegatePath(root);
	}

	@Override
	public Path getFileName() {
		final Path fileName = path.getFileName();
		return fileName == null ? null : new DelegatePath(fileName);
	}

	@Override
	public Path getParent() {
		final Path parent = path.getParent();
		return parent == null ? null : new DelegatePath(parent);
	}

	@Override
	public int getNameCount() {
		return path.getNameCount();
	}

	@Override
	public Path getName(final int index) {
		return new DelegatePath(path.getName(index));
	}

	@Override
	public Path subpath(final int beginIndex, final int endIndex) {
		return new DelegatePath(path.subpath(beginIndex, endIndex));
	}

	@Override
	public boolean startsWith(final Path other) {
		if (other instanceof DelegatePath) {
			return path.startsWith(((DelegatePath) other).path);
		}
		return path.startsWith(other);
	}

	@Override
	public boolean startsWith(final String other) {
		return path.startsWith(other);
	}

	@Override
	public boolean endsWith(final Path other) {
		if (other instanceof DelegatePath) {
			return path.endsWith(((DelegatePath) other).path);
		}
		return path.endsWith(other);
	}

	@Override
	public boolean endsWith(final String other) {
		return path.endsWith(other);
	}

	@Override
	public Path normalize() {
		return new DelegatePath(path.normalize());
	}

	@Override
	public Path resolve(final Path other) {
		if (other instanceof DelegatePath) {
			return new DelegatePath(path.resolve(((DelegatePath) other).path));
		}
		return new DelegatePath(path.resolve(other));
	}

	@Override
	public Path resolve(final String other) {
		return new DelegatePath(path.resolve(other));
	}

	@Override
	public Path resolveSibling(final Path other) {
		if (other instanceof DelegatePath) {
			return new DelegatePath(path.resolveSibling(((DelegatePath) other).path));
		}
		return new DelegatePath(path.resolveSibling(other));
	}

	@Override
	public Path resolveSibling(final String other) {
		return new DelegatePath(path.resolveSibling(other));
	}

	@Override
	public Path relativize(final Path other) {
		if (other instanceof DelegatePath) {
			return new DelegatePath(path.relativize(((DelegatePath) other).path));
		}
		return new DelegatePath(path.relativize(other));
	}

	@Override
	public URI toUri() {
		return path.toUri();
	}

	@Override
	public Path toAbsolutePath() {
		return new DelegatePath(path.toAbsolutePath());
	}

	@Override
	public Path toRealPath(final LinkOption... options) throws IOException {
		return new DelegatePath(path.toRealPath(options));
	}

	@Override
	public File toFile() {
		return path.toFile();
	}

	@Override
	public WatchKey register(final WatchService watcher, final WatchEvent.Kind<?>[] events, final WatchEvent.Modifier... modifiers) throws IOException {
		return path.register(watcher, events, modifiers);
	}

	@Override
	public WatchKey register(final WatchService watcher, final WatchEvent.Kind<?>... events) throws IOException {
		return path.register(watcher, events);
	}

	@Override
	public Iterator<Path> iterator() {
		return new Iterator<>() {
			private final Iterator<Path> itr = path.iterator();

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
			return path.compareTo(((DelegatePath) other).path);
		}
		return path.compareTo(other);
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (other instanceof DelegatePath) {
			return path.equals(((DelegatePath) other).path);
		}
		if (other instanceof Path) {
			return path.equals(other);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public String toString() {
		return path.toString();
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
		return Files.exists(path, options);
	}

	/**
	 * 检查文件是否不存在
	 *
	 * @param options 检查选项
	 * @return 文件是否不存在
	 * @see Files#notExists(Path, LinkOption...)
	 */
	public boolean notExists(final LinkOption... options) {
		return Files.notExists(path, options);
	}

	/**
	 * 检查文件是否为目录
	 *
	 * @param options 检查选项
	 * @return 是否为目录
	 * @see Files#isDirectory(Path, LinkOption...)
	 */
	public boolean isDirectory(final LinkOption... options) {
		return Files.isDirectory(path, options);
	}

	/**
	 * 检查文件是否为普通文件
	 *
	 * @param options 检查选项
	 * @return 是否为普通文件
	 * @see Files#isRegularFile(Path, LinkOption...)
	 */
	public boolean isFile(final LinkOption... options) {
		return Files.isRegularFile(path, options);
	}

	/**
	 * 检查文件是否可执行
	 *
	 * @return 是否可执行
	 * @see Files#isExecutable(Path)
	 */
	public boolean isExecutable() {
		return Files.isExecutable(path);
	}

	/**
	 * 检查文件是否可读
	 *
	 * @return 是否可读
	 * @see Files#isReadable(Path)
	 */
	public boolean isReadable() {
		return Files.isReadable(path);
	}

	/**
	 * 检查文件是否可写
	 *
	 * @return 是否可写
	 * @see Files#isWritable(Path)
	 */
	public boolean isWritable() {
		return Files.isWritable(path);
	}

	/**
	 * 获取文件大小
	 *
	 * @return 文件大小
	 * @throws IORuntimeException IO异常
	 * @see Files#size(Path)
	 */
	public long size() throws IORuntimeException {
		try {
			return Files.size(path);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 删除文件或目录
	 *
	 * @throws IORuntimeException IO异常
	 * @see Files#delete(Path)
	 */
	public void delete() throws IORuntimeException {
		try {
			Files.delete(path);
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
			return Files.deleteIfExists(path);
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
			return new DelegatePath(Files.createDirectory(path, attrs));
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
			return new DelegatePath(Files.createDirectories(path, attrs));
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
			return new DelegatePath(Files.createFile(path, attrs));
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
			return new DelegatePath(Files.createTempDirectory(path, prefix, attrs));
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
			return new DelegatePath(Files.createTempFile(path, prefix, suffix, attrs));
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
		try {
			if (target instanceof DelegatePath) {
				return new DelegatePath(Files.copy(path, ((DelegatePath) target).path, options));
			}
			return new DelegatePath(Files.copy(path, target, options));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
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
		try {
			if (target instanceof DelegatePath) {
				return new DelegatePath(Files.move(path, ((DelegatePath) target).path, options));
			}
			return new DelegatePath(Files.move(path, target, options));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}

