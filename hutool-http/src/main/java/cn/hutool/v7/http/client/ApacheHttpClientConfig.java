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

package cn.hutool.v7.http.client;

/**
 * 针对HttpClient5和HttpClient4的配置
 *
 * @author Looly
 * @since 6.0.0
 */
public class ApacheHttpClientConfig extends ClientConfig {

	/**
	 * 创建新的 HttpClientConfig
	 *
	 * @return HttpClientConfig
	 */
	public static ApacheHttpClientConfig of() {
		return new ApacheHttpClientConfig();
	}

	/**
	 * 最大连接数
	 */
	private int maxTotal;
	/**
	 * 每个路由的最大连接数
	 */
	private int maxPerRoute;
	/**
	 * 获取最大连接总数
	 *
	 * @return 最大连接总数
	 */
	public int getMaxTotal() {
		return maxTotal;
	}

	/**
	 * 设置最大连接总数
	 *
	 * @param maxTotal 最大连接总数
	 * @return 当前HttpClientConfig实例，用于链式调用
	 */
	public ApacheHttpClientConfig setMaxTotal(final int maxTotal) {
		this.maxTotal = maxTotal;
		return this;
	}

	/**
	 * 获取每个路由的最大连接数
	 *
	 * @return 每个路由的最大连接数
	 */
	public int getMaxPerRoute() {
		return maxPerRoute;
	}

	/**
	 * 设置每个路由的最大连接数
	 *
	 * @param maxPerRoute 每个路由的最大连接数
	 * @return 当前HttpClientConfig实例，用于链式调用
	 */
	public ApacheHttpClientConfig setMaxPerRoute(final int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
		return this;
	}
}
