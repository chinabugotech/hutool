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

import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.http.HttpException;
import cn.hutool.v7.http.server.ServerConfig;
import cn.hutool.v7.http.server.engine.AbstractServerEngine;
import org.smartboot.http.server.*;
import org.smartboot.http.server.impl.Request;
import org.smartboot.socket.extension.plugins.SslPlugin;

import javax.net.ssl.SSLContext;

/**
 * smart-http-server引擎
 *
 * @author Looly
 * @since 6.0.0
 */
public class SmartHttpServerEngine extends AbstractServerEngine {

	private HttpBootstrap bootstrap;

	/**
	 * 构造
	 */
	public SmartHttpServerEngine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(HttpBootstrap.class);
	}

	@Override
	public void start() {
		initEngine();
		bootstrap.start();
	}

	@Override
	public HttpBootstrap getRawEngine() {
		return this.bootstrap;
	}

	@Override
	protected void reset() {
		if(null != this.bootstrap){
			this.bootstrap.shutdown();
			this.bootstrap = null;
		}
	}

	@Override
	protected void initEngine() {
		if (null != this.bootstrap) {
			return;
		}

		final HttpBootstrap bootstrap = new HttpBootstrap();
		final HttpServerConfiguration configuration = bootstrap.configuration();

		final ServerConfig config = this.config;
		configuration.host(config.getHost());

		// SSL
		final SSLContext sslContext = config.getSslContext();
		if(null != sslContext){
			final SslPlugin<Request> sslPlugin;
			try {
				sslPlugin = new SslPlugin<>(() -> sslContext);
			} catch (final Exception e) {
				throw new HttpException(e);
			}
			configuration.addPlugin(sslPlugin);
		}

		// 选项
		final int coreThreads = config.getCoreThreads();
		if(coreThreads > 0){
			configuration.threadNum(coreThreads);
		}

		final long idleTimeout = config.getIdleTimeout();
		if(idleTimeout > 0){
			configuration.setHttpIdleTimeout((int) idleTimeout);
		}

		// 请求处理器
		bootstrap.httpHandler(new HttpServerHandler() {
			@Override
			public void handle(final HttpRequest request, final HttpResponse response) {
				handler.handle(new SmartHttpRequest(request), new SmartHttpResponse(response));
			}
		});

		bootstrap.setPort(config.getPort());
		this.bootstrap = bootstrap;
	}
}
