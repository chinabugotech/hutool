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

import cn.hutool.v7.core.io.resource.Resource;

import java.io.File;
import java.io.Serial;

/**
 * 虚拟文件类，继承自File，用于在内存中模拟文件
 */
public class VirtualFile extends File {
	@Serial
	private static final long serialVersionUID = 1L;

	private final Resource content;

	/**
	 * 构造一个虚拟文件
	 *
	 * @param pathname 文件路径
	 * @param content  文件内容
	 */
	public VirtualFile(final String pathname, final Resource content) {
		super(pathname);
		this.content = content;
	}

	/**
	 * 构造一个虚拟文件
	 *
	 * @param parent  父路径
	 * @param child   子文件名
	 * @param content 文件内容
	 */
	public VirtualFile(final String parent, final String child, final Resource content) {
		super(parent, child);
		this.content = content;
	}

	/**
	 * 构造一个虚拟文件
	 *
	 * @param parent  父文件
	 * @param child   子文件名
	 * @param content 文件内容
	 */
	public VirtualFile(final File parent, final String child, final Resource content) {
		super(parent, child);
		this.content = content;
	}

	/**
	 * 获取文件内容，{@code null}表示无此文件
	 *
	 * @return 文件内容
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
	public boolean exists() {
		return null != this.content;
	}

	@Override
	public boolean isFile() {
		return true;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public long length() {
		return null != this.content ? this.content.size() : 0L;
	}

	@Override
	public boolean canRead() {
		return exists();
	}

	@Override
	public boolean canWrite() {
		return false;
	}

	@Override
	public boolean canExecute() {
		return false;
	}

	@Override
	public long lastModified() {
		return System.currentTimeMillis();
	}
}

