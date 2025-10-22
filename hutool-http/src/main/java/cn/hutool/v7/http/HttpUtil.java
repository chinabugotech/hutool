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

package cn.hutool.v7.http;

import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.map.CaseInsensitiveMap;
import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.http.client.ClientConfig;
import cn.hutool.v7.http.client.Request;
import cn.hutool.v7.http.client.Response;
import cn.hutool.v7.http.client.engine.ClientEngine;
import cn.hutool.v7.http.client.engine.ClientEngineFactory;
import cn.hutool.v7.http.meta.Method;
import cn.hutool.v7.http.server.engine.sun.SimpleServer;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/**
 * Http请求工具类
 *
 * @author Looly
 */
public class HttpUtil {

	/**
	 * 检测是否https
	 *
	 * @param url URL
	 * @return 是否https
	 */
	public static boolean isHttps(final String url) {
		return StrUtil.startWithIgnoreCase(url, "https:");
	}

	/**
	 * 检测是否http
	 *
	 * @param url URL
	 * @return 是否http
	 * @since 5.3.8
	 */
	public static boolean isHttp(final String url) {
		return StrUtil.startWithIgnoreCase(url, "http:");
	}

	/**
	 * 创建Http请求对象
	 *
	 * @param method 方法枚举{@link Method}
	 * @param url    请求的URL，可以使HTTP或者HTTPS
	 * @return {@link Request}
	 */
	public static Request createRequest(final String url, final Method method) {
		return Request.of(url).method(method);
	}

	/**
	 * 创建Http GET请求对象
	 *
	 * @param url 请求的URL，可以使HTTP或者HTTPS
	 * @return {@link Request}
	 */
	public static Request createGet(final String url) {
		return createRequest(url, Method.GET);
	}

	/**
	 * 创建Http POST请求对象
	 *
	 * @param url 请求的URL，可以使HTTP或者HTTPS
	 * @return {@link Request}
	 */
	public static Request createPost(final String url) {
		return createRequest(url, Method.POST);
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString     网址
	 * @param customCharset 自定义请求字符集，如果字符集获取不到，使用此字符集
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 */
	@SuppressWarnings("resource")
	public static String get(final String urlString, final Charset customCharset) {
		return send(Request.of(urlString).charset(customCharset)).bodyStr();
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString 网址
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 */
	@SuppressWarnings("resource")
	public static String get(final String urlString) {
		return ClientEngineFactory.getEngine().send(Request.of(urlString)).bodyStr();
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString 网址
	 * @param timeout   超时时长，-1表示默认超时，单位毫秒
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 * @since 3.2.0
	 */
	@SuppressWarnings("resource")
	public static String get(final String urlString, final int timeout) {
		// 创建自定义客户端
		return ClientEngineFactory.createEngine()
			.init(ClientConfig.of().setConnectionTimeout(timeout).setReadTimeout(timeout))
			.send(Request.of(urlString)).bodyStr();
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString 网址
	 * @param paramMap  post表单数据
	 * @return 返回数据
	 */
	@SuppressWarnings("resource")
	public static String get(final String urlString, final Map<String, Object> paramMap) {
		return send(Request.of(urlString).form(paramMap))
			.bodyStr();
	}

	/**
	 * 发送post请求
	 *
	 * @param urlString 网址
	 * @param paramMap  post表单数据
	 * @return 返回数据
	 */
	@SuppressWarnings("resource")
	public static String post(final String urlString, final Map<String, Object> paramMap) {
		return send(Request.of(urlString).method(Method.POST).form(paramMap))
			.bodyStr();
	}

	/**
	 * 发送post请求<br>
	 * 请求体body参数支持两种类型：
	 *
	 * <pre>
	 * 1. 标准参数，例如 a=1&amp;b=2 这种格式
	 * 2. Rest模式，此时body需要传入一个JSON或者XML字符串，Hutool会自动绑定其对应的Content-Type
	 * </pre>
	 *
	 * @param urlString 网址
	 * @param body      post表单数据
	 * @return 返回数据
	 */
	@SuppressWarnings("resource")
	public static String post(final String urlString, final String body) {
		return send(Request.of(urlString).method(Method.POST).body(body))
			.bodyStr();
	}

	/**
	 * 发送post请求<br>
	 * 请求体body参数支持两种类型：
	 *
	 * <pre>
	 * 1. 标准参数，例如 a=1&amp;b=2 这种格式
	 * 2. Rest模式，此时body需要传入一个JSON或者XML字符串，Hutool会自动绑定其对应的Content-Type
	 * </pre>
	 *
	 * @param urlString 网址
	 * @param body      post表单数据
	 * @param timeout   超时时长，-1表示默认超时，单位毫秒
	 * @return 返回数据
	 */
	@SuppressWarnings("resource")
	public static String post(final String urlString, final String body, final int timeout) {
		// 创建自定义客户端
		return ClientEngineFactory.createEngine()
			.init(ClientConfig.of().setConnectionTimeout(timeout).setReadTimeout(timeout))
			.send(Request.of(urlString).method(Method.POST).body(body)).bodyStr();
	}

	/**
	 * 使用默认HTTP引擎，发送HTTP请求
	 *
	 * @param request HTTP请求
	 * @return HTTP响应
	 */
	public static Response send(final Request request) {
		return ClientEngineFactory.getEngine().send(request);
	}

	/**
	 * 创建客户端引擎
	 *
	 * @param engineName 引擎名称
	 * @return {@link ClientEngine}
	 */
	public static ClientEngine createClient(final String engineName) {
		return ClientEngineFactory.createEngine(engineName);
	}

	/**
	 * 创建简易的Http服务器
	 *
	 * @param port 端口
	 * @return {@link SimpleServer}
	 * @since 5.2.6
	 */
	public static SimpleServer createServer(final int port) {
		return new SimpleServer(port);
	}

	/**
	 * 打印{@link Response} 为可读形式
	 *
	 * @param response {@link Response}
	 * @return 字符串
	 */
	public static String toString(final Response response) {
		final StringBuilder sb = StrUtil.builder();
		sb.append("Response Status: ").append(response.getStatus()).append(StrUtil.CRLF);

		// header
		sb.append("Response Headers: ").append(StrUtil.CRLF);
		response.headers().forEach((key, value) -> sb.append("    ").append(key).append(": ").append(CollUtil.join(value, ",")).append(StrUtil.CRLF));

		// body
		sb.append("Response Body: ").append(StrUtil.CRLF);
		sb.append("    ").append(response.bodyStr()).append(StrUtil.CRLF);

		return sb.toString();
	}

	/**
	 * 打印 {@link Request} 为可读形式
	 *
	 * @param request {@link Request}
	 * @return 字符串
	 */
	public static String toString(final Request request) {
		final StringBuilder sb = StrUtil.builder();
		sb.append("Request Url: ").append(request.url()).append(StrUtil.CRLF);

		// header
		sb.append("Request Headers: ").append(StrUtil.CRLF);
		request.headers().forEach((key, value) -> sb.append("    ").append(key).append(": ").append(CollUtil.join(value, ",")).append(StrUtil.CRLF));

		// body
		sb.append("Request Body: ").append(StrUtil.CRLF);
		sb.append("    ").append(request.bodyStr()).append(StrUtil.CRLF);
		return sb.toString();
	}

	/**
	 * 获取指定的Header值，如果不存在返回{@code null}<br>
	 * 根据RFC2616规范，header的name不区分大小写，因此首先get值，不存在则遍历匹配不区分大小写的key。
	 *
	 * @param headers 头信息的Map
	 * @param name    header名
	 * @return header值
	 * @since 6.0.0
	 */
	public static String header(final Map<String, ? extends Collection<String>> headers, final String name) {
		Collection<String> values = headers.get(name);
		if (null == values && !(headers instanceof CaseInsensitiveMap)) {
			// issue#I96U4T，根据RFC2616规范，header的name不区分大小写
			values = MapUtil.firstMatchValue(headers, entry -> StrUtil.equalsIgnoreCase(name, entry.getKey()));
		}
		if (CollUtil.isNotEmpty(values)) {
			return CollUtil.getFirst(values);
		}

		return null;
	}
}
