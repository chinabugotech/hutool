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

package cn.hutool.v7.core.io.resource;

import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.net.url.UrlUtil;

import javax.tools.FileObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * {@link FileObject} 资源包装
 *
 * @author Looly
 * @since 5.5.2
 */
public class FileObjectResource implements Resource {

	private final FileObject fileObject;

	/**
	 * 构造
	 *
	 * @param fileObject {@link FileObject}
	 */
	public FileObjectResource(final FileObject fileObject) {
		this.fileObject = fileObject;
	}

	/**
	 * 获取原始的{@link FileObject}
	 *
	 * @return {@link FileObject}
	 */
	public FileObject getFileObject() {
		return this.fileObject;
	}

	@Override
	public String getName() {
		return this.fileObject.getName();
	}

	@Override
	public URL getUrl() {
		try {
			return this.fileObject.toUri().toURL();
		} catch (final MalformedURLException e) {
			return null;
		}
	}

	@Override
	public long size() {
		return UrlUtil.size(getUrl());
	}

	@Override
	public InputStream getStream() {
		try {
			return this.fileObject.openInputStream();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public BufferedReader getReader(final Charset charset) {
		try {
			return IoUtil.toBuffered(this.fileObject.openReader(false));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
