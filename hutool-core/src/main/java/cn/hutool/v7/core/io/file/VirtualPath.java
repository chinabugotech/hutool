/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

import cn.hutool.v7.core.io.resource.Resource;
import cn.hutool.v7.core.text.CharUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.text.split.SplitUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 虚拟路径类，实现Path接口，用于在内存中模拟文件路径
 */
public class VirtualPath implements Path {

	private final String path;
	private final Resource content;

	/**
	 * 构造一个虚拟路径
	 *
	 * @param path    路径字符串
	 * @param content 路径对应的内容
	 */
	public VirtualPath(final String path, final Resource content) {
		this.path = path;
		this.content = content;
	}

	/**
	 * 获取路径内容
	 *
	 * @return 内容
	 */
	public Resource getContent() {
		return this.content;
	}

	/**
	 * 获取文件内容，{@code null}表示无此文件
	 *
	 * @return 文件内容
	 */
	public byte[] getBytes() {
		return null != this.content ? this.content.readBytes() : null;
	}

	@Override
	public FileSystem getFileSystem() {
		throw new UnsupportedOperationException("VirtualPath does not support FileSystem operations");
	}

	@Override
	public boolean isAbsolute() {
		return false;
	}

	@Override
	public Path getRoot() {
		return null;
	}

	@Override
	public Path getFileName() {
		final int index = path.lastIndexOf(CharUtil.SLASH);
		if (index == -1) {
			return new VirtualPath(path, content);
		}
		return new VirtualPath(path.substring(index + 1), content);
	}

	@Override
	public Path getParent() {
		final int index = path.lastIndexOf(CharUtil.SLASH);
		if (index == -1) {
			return null;
		}
		return new VirtualPath(path.substring(0, index), null);
	}

	@Override
	public int getNameCount() {
		if(StrUtil.isEmpty(path)){
			// ""表示一个有效名称
			return 1;
		}
		if(StrUtil.equals(path, StrUtil.SLASH)){
			// /表示根路径，无名称
			return 0;
		}
		// 根路径不算名称
		return StrUtil.count(path, StrUtil.SLASH);
	}

	@Override
	public Path getName(final int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index must be >= 0");
		}

		final List<String> parts = SplitUtil.splitTrim(path, StrUtil.SLASH);
		if (index >= parts.size()) {
			throw new IllegalArgumentException("index exceeds name count");
		}

		return new VirtualPath(parts.get(index), index == parts.size() - 1 ? content : null);
	}

	@Override
	public Path subpath(final int beginIndex, final int endIndex) {
		if (beginIndex < 0 || endIndex <= beginIndex) {
			throw new IllegalArgumentException("beginIndex or endIndex is invalid");
		}

		final List<String> parts = SplitUtil.splitTrim(path, StrUtil.SLASH);
		if (endIndex > parts.size()) {
			throw new IllegalArgumentException("endIndex exceeds name count");
		}

		final StringBuilder sb = new StringBuilder();
		for (int i = beginIndex; i < endIndex; i++) {
			if (!sb.isEmpty()) {
				sb.append(CharUtil.SLASH);
			}
			sb.append(parts.get(i));
		}

		return new VirtualPath(sb.toString(), endIndex == parts.size() ? content : null);
	}

	@Override
	public boolean startsWith(final Path other) {
		if (!(other instanceof final VirtualPath otherPath)) {
			return false;
		}
		return this.path.startsWith(otherPath.path);
	}

	@Override
	public boolean startsWith(final String other) {
		return this.path.startsWith(other);
	}

	@Override
	public boolean endsWith(final Path other) {
		if (!(other instanceof final VirtualPath otherPath)) {
			return false;
		}
		return this.path.endsWith(otherPath.path);
	}

	@Override
	public boolean endsWith(final String other) {
		return this.path.endsWith(other);
	}

	@Override
	public Path normalize() {
		return this;
	}

	@Override
	public Path resolve(final Path other) {
		if (other.isAbsolute()) {
			return other;
		}
		if (other.toString().isEmpty()) {
			return this;
		}
		final String newPath = this.path + "/" + other;
		return new VirtualPath(newPath, other instanceof VirtualPath ? ((VirtualPath) other).content : null);
	}

	@Override
	public Path resolve(final String other) {
		if (other.isEmpty()) {
			return this;
		}
		final String newPath = this.path + StrUtil.SLASH + other;
		return new VirtualPath(newPath, null);
	}

	@Override
	public Path resolveSibling(final Path other) {
		if (other == null) {
			throw new NullPointerException("other cannot be null");
		}
		final Path parent = getParent();
		return (parent == null) ? other : parent.resolve(other);
	}

	@Override
	public Path resolveSibling(final String other) {
		if (other == null) {
			throw new NullPointerException("other cannot be null");
		}
		final Path parent = getParent();
		return (parent == null) ? new VirtualPath(other, null) : parent.resolve(other);
	}

	@Override
	public Path relativize(final Path other) {
		if (!(other instanceof final VirtualPath otherPath)) {
			throw new IllegalArgumentException("other must be a VirtualPath");
		}

		if (this.path.isEmpty()) {
			return otherPath;
		}

		if (otherPath.path.startsWith(this.path + "/")) {
			return new VirtualPath(otherPath.path.substring(this.path.length() + 1), otherPath.content);
		}

		return otherPath;
	}

	@Override
	public URI toUri() {
		throw new UnsupportedOperationException("VirtualPath does not support URI conversion");
	}

	@Override
	public Path toAbsolutePath() {
		return this;
	}

	@Override
	public Path toRealPath(final LinkOption... options) {
		return this;
	}

	@Override
	public File toFile() {
		return new VirtualFile(path, content);
	}

	@Override
	public WatchKey register(final WatchService watcher, final WatchEvent.Kind<?>[] events, final WatchEvent.Modifier... modifiers) throws IOException {
		throw new UnsupportedOperationException("VirtualPath does not support watch service");
	}

	@Override
	public WatchKey register(final WatchService watcher, final WatchEvent.Kind<?>... events) throws IOException {
		throw new UnsupportedOperationException("VirtualPath does not support watch service");
	}

	@Override
	public Iterator<Path> iterator() {
		return new Iterator<>() {
			private int index = 0;
			private final List<String> parts = SplitUtil.splitTrim(path, StrUtil.SLASH);

			@Override
			public boolean hasNext() {
				return index < parts.size();
			}

			@Override
			public Path next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return new VirtualPath(parts.get(index++), index == parts.size() ? content : null);
			}
		};
	}

	@Override
	public int compareTo(final Path other) {
		if (!(other instanceof final VirtualPath otherPath)) {
			throw new ClassCastException("Cannot compare VirtualPath with " + other.getClass().getName());
		}
		return this.path.compareTo(otherPath.path);
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof final VirtualPath otherPath)) {
			return false;
		}
		return this.path.equals(otherPath.path);
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public String toString() {
		return path;
	}
}

