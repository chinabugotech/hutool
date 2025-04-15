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

package cn.hutool.v7.http.server.handler;

import cn.hutool.v7.core.lang.Assert;

/**
 * 路由处理器<br>
 * 根据请求的路径精确匹配路由，并调用对应的处理器，如果没有定义处理器，使用默认处理器
 *
 * @author Looly
 * @since 6.0.0
 */
public class RouteHttpHandler implements HttpHandler {

	/**
	 * 创建路由处理器
	 *
	 * @param defaultHandler 默认处理器
	 * @return {@code RouteHttpHandler}
	 */
	public static RouteHttpHandler of(final HttpHandler defaultHandler) {
		return new RouteHttpHandler(defaultHandler);
	}

	private final PathTrie pathTrie;
	private final HttpHandler defaultHandler;

	/**
	 * 构造
	 *
	 * @param defaultHandler 默认处理器
	 */
	public RouteHttpHandler(final HttpHandler defaultHandler) {
		this.pathTrie = new PathTrie();
		this.defaultHandler = Assert.notNull(defaultHandler);
	}


	/**
	 * 添加路由
	 *
	 * @param path    路径
	 * @param handler 处理器
	 * @return this
	 */
	public RouteHttpHandler route(final String path, final HttpHandler handler) {
		if (null != handler) {
			pathTrie.add(path, handler);
		}
		return this;
	}

	@Override
	public void handle(final ServerRequest request, final ServerResponse response) {
		final String path = request.getPath();
		final HttpHandler handler = pathTrie.match(path);
		if (null != handler) {
			handler.handle(request, response);
		} else {
			// 没有path匹配，使用默认处理器
			defaultHandler.handle(request, response);
		}
	}
}
