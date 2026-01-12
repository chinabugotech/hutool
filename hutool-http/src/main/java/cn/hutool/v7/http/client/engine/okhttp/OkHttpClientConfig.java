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

package cn.hutool.v7.http.client.engine.okhttp;

import cn.hutool.v7.http.client.ClientConfig;

/**
 * OkHttpClient 配置
 *
 * @author Looly
 * @since 7.0.0
 */
public class OkHttpClientConfig extends ClientConfig {
	/**
	 * 创建新的 OkHttpClientConfig
	 *
	 * @return OkHttpClientConfig
	 */
	public static OkHttpClientConfig of() {
		return new OkHttpClientConfig();
	}

	/**
	 * 默认最大空闲连接数
	 */
	private int maxIdle;

	/**
	 * 获取最大空闲连接数
	 *
	 * @return 最大空闲连接数
	 */
	public int getMaxIdle() {
		return maxIdle;
	}

	/**
	 * 设置最大空闲连接数
	 *
	 * @param maxIdle 最大空闲连接数
	 * @return this
	 */
	public ClientConfig setMaxIdle(final int maxIdle) {
		this.maxIdle = maxIdle;
		return this;
	}
}
