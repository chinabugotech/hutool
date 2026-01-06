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

package cn.hutool.v7.http.server.engine.smart;

import cn.hutool.v7.http.server.handler.ServerResponse;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.server.HttpResponse;

import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * SmartHttp响应对象
 *
 * @author Looly
 * @since 6.0.0
 */
public class SmartHttpResponse implements ServerResponse {

	private final HttpResponse response;
	private Charset charset;

	/**
	 * 构造
	 *
	 * @param response 响应对象
	 */
	public SmartHttpResponse(final HttpResponse response) {
		this.response = response;
	}

	@Override
	public SmartHttpResponse setStatus(final int statusCode) {
		response.setHttpStatus(HttpStatus.valueOf(statusCode));
		return this;
	}

	@Override
	public SmartHttpResponse setCharset(final Charset charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public Charset getCharset() {
		return this.charset;
	}

	@Override
	public SmartHttpResponse addHeader(final String header, final String value) {
		this.response.addHeader(header, value);
		return this;
	}

	@Override
	public SmartHttpResponse setHeader(final String header, final String value) {
		this.response.setHeader(header, value);
		return this;
	}

	@Override
	public OutputStream getOutputStream() {
		return this.response.getOutputStream();
	}
}
