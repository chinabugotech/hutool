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

package cn.hutool.v7.core.io.resource;

import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.core.io.stream.ReaderInputStream;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * 基于{@link InputStream}的资源获取器<br>
 * 注意：此对象中getUrl方法始终返回null
 *
 * @author Looly
 * @since 4.0.9
 */
public class InputStreamResource implements Resource, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * {@link InputStream}
	 */
	private final InputStream in;
	/**
	 * 资源名称
	 */
	private final String name;

	/**
	 * 构造
	 *
	 * @param reader {@link Reader}
	 * @param charset 编码
	 */
	public InputStreamResource(final Reader reader, final Charset charset) {
		this(new ReaderInputStream(reader, charset));
	}

	/**
	 * 构造
	 *
	 * @param in {@link InputStream}
	 */
	public InputStreamResource(final InputStream in) {
		this(in, null);
	}

	/**
	 * 构造
	 *
	 * @param in {@link InputStream}
	 * @param name 资源名称
	 */
	public InputStreamResource(final InputStream in, final String name) {
		this.in = in;
		this.name = name;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public long size() {
		try {
			return this.in.available();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public InputStream getStream() {
		return this.in;
	}
}
