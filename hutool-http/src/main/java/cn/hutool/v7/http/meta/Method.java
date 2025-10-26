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

package cn.hutool.v7.http.meta;

/**
 * Http方法枚举
 *
 * @author Looly
 */
public enum Method {
	/**
	 * GET 请求
	 */
	GET,
	/**
	 * POST 请求
	 */
	POST,
	/**
	 * HEAD 请求
	 */
	HEAD,
	/**
	 * OPTIONS 请求
	 */
	OPTIONS,
	/**
	 * PUT 请求
	 */
	PUT,
	/**
	 * DELETE 请求
	 */
	DELETE,
	/**
	 * TRACE 请求
	 */
	TRACE,
	/**
	 * CONNECT 请求
	 */
	CONNECT,
	/**
	 * PATCH 请求
	 */
	PATCH;

	/**
	 * 是否忽略读取响应body部分<br>
	 * HEAD、CONNECT、TRACE方法将不读取响应体
	 *
	 * @return 是否需要忽略响应body部分
	 */
	public boolean isIgnoreBody() {
		//https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Methods/OPTIONS
		// OPTIONS请求可以带有响应体
		return switch (this) {
			case HEAD, CONNECT, TRACE -> true;
			default -> false;
		};
	}
}
