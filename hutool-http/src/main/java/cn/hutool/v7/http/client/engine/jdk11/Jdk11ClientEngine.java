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

import cn.hutool.v7.http.HttpException;
import cn.hutool.v7.http.client.ClientConfig;
import cn.hutool.v7.http.client.Request;
import cn.hutool.v7.http.client.Response;
import cn.hutool.v7.http.client.cookie.InMemoryCookieStore;
import cn.hutool.v7.http.client.engine.AbstractClientEngine;
import cn.hutool.v7.http.client.engine.jdk.JdkCookieManager;
import cn.hutool.v7.http.proxy.ProxyInfo;
import cn.hutool.v7.http.ssl.SSLInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * JDK11 HttpClient引擎实现
 *
 * @author looly
 * @since 7.0.0
 */
public class Jdk11ClientEngine extends AbstractClientEngine {

	/**
	 * HttpClient
	 */
	private HttpClient client;
	/**
	 * Cookie管理
	 */
	private JdkCookieManager cookieManager;

	/**
	 * 获取Cookie管理器
	 *
	 * @return Cookie管理器
	 */
	public JdkCookieManager getCookieManager() {
		return this.cookieManager;
	}

	@Override
	public Response send(final Request message) {
		initEngine();

		final HttpRequest.Builder builder = HttpRequest.newBuilder()
			.uri(message.url().toURI())
			.method(message.method().name(), null == message.body() ?
				HttpRequest.BodyPublishers.noBody() :
				HttpRequest.BodyPublishers.ofInputStream(message::bodyStream));

		// Read超时
		if(null != this.config){
			builder.timeout(Duration.ofMillis(this.config.getReadTimeout()));
		}

		// 自定义Headers
		message.headers().forEach((k, v1) -> v1.forEach(v2 -> builder.header(k, v2)));

		final HttpResponse<InputStream> response;
		try {
			response = this.client.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream());
		} catch (final Exception e) {
			throw new HttpException(e);
		}

		return new Jdk11HttpResponse(response, message);
	}

	@Override
	public Object getRawEngine() {
		return this.client;
	}

	@Override
	public void close() throws IOException {
		// 关闭Cookie管理器
		this.cookieManager = null;
	}

	@Override
	protected void reset() {
		// do nothing
	}

	@Override
	protected void initEngine() {
		if (null != this.client) {
			return;
		}

		final HttpClient.Builder builder = HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_1_1);

		final ClientConfig config = this.config;
		if(null != config){
			builder.connectTimeout(Duration.ofMillis(config.getConnectionTimeout()));

			// SSL
			final SSLInfo sslInfo = config.getSslInfo();
			if(null != sslInfo){
				builder.sslContext(sslInfo.getSslContext());
			}

			// Cookie
			if (config.isUseCookieManager()) {
				this.cookieStore = new InMemoryCookieStore();
				this.cookieManager = new JdkCookieManager(this.cookieStore);
				builder.cookieHandler(this.cookieManager.getCookieManager());
			}

			// Proxy
			final ProxyInfo proxy = config.getProxy();
			if (null != proxy) {
				builder.proxy(proxy.getProxySelector());
			}
		}

		this.client = builder.build();
	}
}
