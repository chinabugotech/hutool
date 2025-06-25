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

package cn.hutool.v7.http.server.engine;

import cn.hutool.v7.http.server.ServerConfig;
import cn.hutool.v7.http.server.handler.HttpHandler;

/**
 * 服务端引擎抽象类，实现重置引擎功能
 *
 * @author Looly
 */
public abstract class AbstractServerEngine implements ServerEngine {

	/**
	 * 配置
	 */
	protected ServerConfig config;
	/**
	 * 处理器
	 */
	protected HttpHandler handler;

	@Override
	public AbstractServerEngine init(final ServerConfig config) {
		this.config = config;
		reset();
		return this;
	}

	@Override
	public AbstractServerEngine setHandler(final HttpHandler handler) {
		this.handler = handler;
		return this;
	}

	/**
	 * 重置引擎
	 */
	protected abstract void reset();

	/**
	 * 初始化引擎，实现逻辑中如果初始化完成，不再重新初始化
	 */
	protected abstract void initEngine();
}
