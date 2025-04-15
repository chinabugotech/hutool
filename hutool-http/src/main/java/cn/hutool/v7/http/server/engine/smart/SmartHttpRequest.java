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

package cn.hutool.v7.http.server.engine.smart;

import cn.hutool.v7.core.io.IORuntimeException;
import cn.hutool.v7.http.server.handler.ServerRequest;
import org.smartboot.http.server.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * SmartHttp请求对象
 *
 * @author Looly
 * @since 6.0.0
 */
public class SmartHttpRequest implements ServerRequest {

	private final HttpRequest request;

	/**
	 * 构造
	 *
	 * @param request 请求对象
	 */
	public SmartHttpRequest(final HttpRequest request) {
		this.request = request;
	}

	@Override
	public String getMethod() {
		return request.getMethod();
	}

	@Override
	public String getPath() {
		return request.getRequestURI();
	}

	@Override
	public String getQuery() {
		return request.getQueryString();
	}

	@Override
	public String getHeader(final String name) {
		return request.getHeader(name);
	}

	/**
	 * 获取所有Header名称
	 *
	 * @return 所有Header名称
	 */
	public Collection<String> getHeaderNames() {
		return request.getHeaderNames();
	}

	@Override
	public InputStream getBodyStream() {
		try {
			return request.getInputStream();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
