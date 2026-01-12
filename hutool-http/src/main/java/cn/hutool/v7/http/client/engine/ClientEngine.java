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

package cn.hutool.v7.http.client.engine;

import cn.hutool.v7.http.client.engine.okhttp.OkHttpClientConfig;
import cn.hutool.v7.http.client.ApacheHttpClientConfig;
import cn.hutool.v7.http.client.ClientConfig;
import cn.hutool.v7.http.client.Request;
import cn.hutool.v7.http.client.Response;

import java.io.Closeable;

/**
 * HTTP客户端引擎接口，通过不同实现，完成HTTP请求发送
 *
 * @author Looly
 * @since 7.0.0
 */
public interface ClientEngine extends Closeable {

	/**
	 * 设置客户端引擎参数，如超时、代理等信息
	 * 对不同引擎个性化配置，使用对应的{@link ClientConfig} 子类：
	 *
	 * <ul>
	 *     <li>HttpClient4和HttpClient5使用{@link ApacheHttpClientConfig}</li>
	 *     <li>OkHttp使用{@link OkHttpClientConfig}</li>
	 * </ul>
	 * <p>
	 * 如果混用这些配置，则个性配置不生效
	 *
	 * @param config 客户端设置
	 * @return this
	 */
	ClientEngine init(ClientConfig config);

	/**
	 * 发送HTTP请求
	 *
	 * @param message HTTP请求消息
	 * @return 响应内容
	 */
	Response send(Request message);

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return 对应HTTP客户端实现的引擎对象
	 * @since 7.0.0
	 */
	Object getRawEngine();
}
