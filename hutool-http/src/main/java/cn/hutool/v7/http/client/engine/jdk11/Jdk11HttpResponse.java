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

package cn.hutool.v7.http.client.engine.jdk11;

import cn.hutool.v7.core.lang.wrapper.SimpleWrapper;
import cn.hutool.v7.http.GlobalCompressStreamRegister;
import cn.hutool.v7.http.HttpUtil;
import cn.hutool.v7.http.client.Request;
import cn.hutool.v7.http.client.Response;
import cn.hutool.v7.http.client.body.ResponseBody;
import cn.hutool.v7.http.meta.HeaderName;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

/**
 * JDK11 Http响应包装类
 *
 * @author looly
 * @since 7.0.0
 */
public class Jdk11HttpResponse extends SimpleWrapper<HttpResponse<InputStream>> implements Response {

	/**
	 * 响应头
	 */
	private final Map<String, List<String>> headers;
	/**
	 * 响应内容体，{@code null} 表示无内容
	 */
	private final ResponseBody body;

	/**
	 * 构造
	 *
	 * @param rawRes 原始响应
	 * @param message 请求信息
	 */
	public Jdk11HttpResponse(final HttpResponse<InputStream> rawRes, final Request message) {
		super(rawRes);
		this.headers = this.raw.headers().map();
		this.body = message.method().isIgnoreBody() ? null : new ResponseBody(this, bodyStream());
	}

	@Override
	public int getStatus() {
		return this.raw.statusCode();
	}

	@Override
	public String header(final String name) {
		return HttpUtil.header(this.headers, name);
	}

	@Override
	public Map<String, List<String>> headers() {
		return this.headers;
	}

	@Override
	public InputStream bodyStream() {
		// 直接取得解压后的原始流
		return GlobalCompressStreamRegister.INSTANCE.wrapStream(this.raw.body(),
			header(HeaderName.CONTENT_ENCODING.getValue()));
	}

	@Override
	public Response sync() {
		final ResponseBody body = this.body;
		if(null != body){
			body.sync();
		}
		this.raw = null;
		return this;
	}

	@Override
	public ResponseBody body() {
		return this.body;
	}

	@Override
	public void close() throws IOException {
		// ignore
	}
}
