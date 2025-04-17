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

package cn.hutool.v7.http.server.engine.jetty;

import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.util.ObjUtil;
import cn.hutool.v7.http.HttpException;
import cn.hutool.v7.http.server.ServerConfig;
import cn.hutool.v7.http.server.engine.AbstractServerEngine;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.net.ssl.SSLContext;

/**
 * Jetty引擎实现
 *
 * @author Looly
 */
public class JettyEngine extends AbstractServerEngine {

	private Server server;
	private Handler jettyHandler;

	/**
	 * 构造
	 */
	public JettyEngine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(Server.class);
	}

	/**
	 * 设置Jetty处理器，用于处理请求
	 *
	 * @param jettyHandler 处理器
	 * @return this
	 */
	public JettyEngine setJettyHandler(final Handler jettyHandler) {
		this.jettyHandler = jettyHandler;
		return this;
	}

	@Override
	public void start() {
		initEngine();
		try {
			this.server.start();
			this.server.join();
		} catch (final Exception e) {
			throw new HttpException(e);
		}
	}

	@Override
	public Server getRawEngine() {
		return this.server;
	}

	@Override
	protected void reset() {
		if (null != this.server) {
			this.server.destroy();
			this.server = null;
		}
	}

	@Override
	protected void initEngine() {
		if (null != this.server) {
			return;
		}

		final ServerConfig config = this.config;

		// 线程池
		final QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setName("Hutool");
		final int coreThreads = config.getCoreThreads();
		if(coreThreads > 0){
			threadPool.setMinThreads(coreThreads);
		}
		final int maxThreads = config.getMaxThreads();
		if(maxThreads > 0){
			threadPool.setMaxThreads(maxThreads);
		}
		final long idleTimeout = config.getIdleTimeout();
		if(idleTimeout > 0){
			threadPool.setIdleTimeout((int) idleTimeout);
		}

		final Server server = new Server(threadPool);
		server.addConnector(createConnector(server));
		server.setHandler(ObjUtil.defaultIfNull(this.jettyHandler,
			() -> new JettyHandler(this.handler)));
		this.server = server;
	}

	/**
	 * 创建连接器
	 *
	 * @param server 服务器
	 * @return 连接器
	 */
	private ServerConnector createConnector(final Server server) {
		final ServerConfig config = this.config;
		final ServerConnector connector;

		final HttpConnectionFactory connectionFactory = createHttpConnectionFactory(config);

		final SSLContext sslContext = config.getSslContext();
		if (null != sslContext) {
			final SslConnectionFactory sslConnectionFactory = createSslConnectionFactory(sslContext);
			connector = new ServerConnector(server, sslConnectionFactory, connectionFactory);
		} else {
			// 创建HTTP连接器
			connector = new ServerConnector(server, connectionFactory);
		}

		final long idleTimeout = config.getIdleTimeout();
		if(idleTimeout > 0){
			connector.setIdleTimeout(idleTimeout);
		}
		connector.setHost(config.getHost());
		connector.setPort(config.getPort());

		return connector;
	}

	/**
	 * 创建HTTP连接工厂
	 *
	 * @param config 配置
	 * @return 连接工厂
	 */
	private static HttpConnectionFactory createHttpConnectionFactory(final ServerConfig config) {
		final HttpConfiguration configuration = new HttpConfiguration();
		final int maxHeaderSize = config.getMaxHeaderSize();
		if(maxHeaderSize > 0){
			configuration.setRequestHeaderSize(maxHeaderSize);
		}
		return new HttpConnectionFactory(configuration);
	}

	/**
	 * 创建SSL连接工厂
	 *
	 * @param sslContext SSL上下文
	 * @return 连接工厂
	 */
	private static SslConnectionFactory createSslConnectionFactory(final SSLContext sslContext) {
		final SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
		sslContextFactory.setSslContext(sslContext);
		return new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString());
	}
}
