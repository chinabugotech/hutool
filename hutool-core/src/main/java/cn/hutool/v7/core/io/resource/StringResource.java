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

import cn.hutool.v7.core.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * 字符串资源，字符串做为资源
 *
 * @author Looly
 * @since 4.1.0
 * @see CharSequenceResource
 */
public class StringResource extends CharSequenceResource {
	private static final long serialVersionUID = 1L;


	/**
	 * 构造，使用UTF8编码
	 *
	 * @param data 资源数据
	 */
	public StringResource(final String data) {
		super(data, null);
	}

	/**
	 * 构造，使用UTF8编码
	 *
	 * @param data 资源数据
	 * @param name 资源名称
	 */
	public StringResource(final String data, final String name) {
		super(data, name, CharsetUtil.UTF_8);
	}

	/**
	 * 构造
	 *
	 * @param data 资源数据
	 * @param name 资源名称
	 * @param charset 编码
	 */
	public StringResource(final String data, final String name, final Charset charset) {
		super(data, name, charset);
	}
}
