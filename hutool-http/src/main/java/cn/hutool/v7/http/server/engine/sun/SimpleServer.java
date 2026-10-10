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

package cn.hutool.v7.http.server.engine.sun;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.http.server.ServerConfig;
import cn.hutool.v7.http.server.engine.sun.filter.HttpFilter;
import cn.hutool.v7.http.server.engine.sun.filter.SimpleFilter;
import cn.hutool.v7.http.server.handler.HttpHandler;
import cn.hutool.v7.http.server.handler.NotFoundHandler;
import cn.hutool.v7.http.server.handler.RootHandler;
import cn.hutool.v7.http.server.handler.RouteHttpHandler;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/**
 * 简易Http服务器，基于{@link SunHttpServerEngine}
 *
 * @author Looly
 * @since 5.2.5
 */
public class SimpleServer {

	private final SunHttpServerEngine engine;
	private final RouteHttpHandler handler;

	/**
	 * 构造
	 *
	 * @param port 监听端口
	 */
	public SimpleServer(final int port) {
		this(new InetSocketAddress(port));
	}

	/**
	 * 构造
	 *
	 * @param hostname 监听地址
	 * @param port     监听端口
	 */
	public SimpleServer(final String hostname, final int port) {
		this(new InetSocketAddress(hostname, port));
	}

	/**
	 * 构造
	 *
	 * @param address 监听地址
	 */
	public SimpleServer(final InetSocketAddress address) {
		this(address, null);
	}

	/**
	 * 构造
	 *
	 * @param address    监听地址
	 * @param sslContext ssl配置
	 */
	public SimpleServer(final InetSocketAddress address, final SSLContext sslContext) {
		this.engine = new SunHttpServerEngine();

		final ServerConfig serverConfig = ServerConfig.of()
			.setHost(address.getHostName())
			.setPort(address.getPort())
			.setSslContext(sslContext);
		this.engine.init(serverConfig);
		this.handler = new RouteHttpHandler(new NotFoundHandler());
		this.engine.setHandler(this.handler);
	}

	/**
	 * 增加请求过滤器，此过滤器对所有请求有效<br>
	 * 此方法需在以下方法前之前调用：
	 *
	 * <ul>
	 *     <li>{@link #setRoot(File)}  </li>
	 *     <li>{@link #setRoot(String)}  </li>
	 *     <li>{@link #addAction(String, HttpHandler)}</li>
	 * </ul>
	 *
	 * @param filter {@link Filter} 请求过滤器
	 * @return this
	 * @since 5.5.7
	 */
	public SimpleServer addFilter(final Filter filter) {
		this.engine.addFilter(filter);
		return this;
	}

	/**
	 * 增加请求过滤器，此过滤器对所有请求有效<br>
	 * 此方法需在以下方法前之前调用：
	 *
	 * <ul>
	 *     <li>{@link #setRoot(File)}  </li>
	 *     <li>{@link #setRoot(String)}  </li>
	 *     <li>{@link #addAction(String, HttpHandler)} (String, HttpHandler)}</li>
	 * </ul>
	 *
	 * @param filter {@link Filter} 请求过滤器
	 * @return this
	 * @since 5.5.7
	 */
	public SimpleServer addFilter(final HttpFilter filter) {
		return addFilter(new SimpleFilter() {
			@Override
			public void doFilter(final HttpExchange httpExchange, final Chain chain) throws IOException {
				final HttpExchangeWrapper httpExchangeWrapper = new HttpExchangeWrapper(httpExchange);
				filter.doFilter(httpExchangeWrapper.getRequest(), httpExchangeWrapper.getResponse(), chain);
			}
		});
	}

	/**
	 * 增加请求处理规则
	 *
	 * @param path    路径，例如:/a/b 或者 a/b
	 * @param handler 处理器，包括请求和响应处理
	 * @return this
	 */
	public SimpleServer addAction(String path, final HttpHandler handler) {
		// 非/开头的路径会报错
		path = StrUtil.addPrefixIfNot(path, StrUtil.SLASH);
		this.handler.route(path, handler);
		return this;
	}

	/**
	 * 设置根目录，默认的页面从root目录中读取解析返回
	 *
	 * @param root 路径
	 * @return this
	 */
	public SimpleServer setRoot(final String root) {
		return setRoot(new File(root));
	}

	/**
	 * 设置根目录，默认的页面从root目录中读取解析返回
	 *
	 * @param root 路径
	 * @return this
	 */
	public SimpleServer setRoot(final File root) {
		addAction("/", new RootHandler(root));
		return this;
	}

	/**
	 * 设置自定义线程池
	 *
	 * @param executor {@link Executor}
	 * @return this
	 */
	public SimpleServer setExecutor(final Executor executor) {
		this.engine.setExecutor(executor);
		return this;
	}

	/**
	 * 获得原始HttpServer对象
	 *
	 * @return {@link HttpServer}
	 */
	public HttpServer getRawServer() {
		return this.engine.getRawEngine();
	}

	/**
	 * 获取服务器地址信息
	 *
	 * @return {@link InetSocketAddress}
	 */
	public InetSocketAddress getAddress() {
		return getRawServer().getAddress();
	}

	/**
	 * 启动Http服务器，启动后会阻塞当前线程
	 */
	public void start() {
		final InetSocketAddress address = getAddress();
		Console.log("Hutool Simple Http Server listen on 【{}:{}】", address.getHostName(), address.getPort());
		this.engine.start();
	}
}
