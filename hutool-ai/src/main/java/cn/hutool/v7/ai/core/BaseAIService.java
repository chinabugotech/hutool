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

package cn.hutool.v7.ai.core;

import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.http.HttpUtil;
import cn.hutool.v7.http.client.ClientConfig;
import cn.hutool.v7.http.client.Response;
import cn.hutool.v7.http.client.engine.ClientEngine;
import cn.hutool.v7.http.client.engine.ClientEngineFactory;
import cn.hutool.v7.http.meta.ContentType;
import cn.hutool.v7.http.meta.HeaderName;
import cn.hutool.v7.http.meta.Method;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 基础AIService，包含基公共参数和公共方法
 *
 * @author elichow
 * @since 7.0.0
 */
public class BaseAIService {

	/**
	 * AI配置
	 */
	protected final AIConfig config;
	private final ClientEngine clientEngine;

	/**
	 * 构造方法
	 *
	 * @param config AI配置
	 */
	public BaseAIService(final AIConfig config) {
		this.config = config;
		this.clientEngine = ClientEngineFactory.createEngine(
			ClientConfig.of().setTimeout(config.getTimeout()).setProxy(config.getProxy()));
	}

	/**
	 * 发送Get请求
	 * @param endpoint 请求节点
	 * @return 请求响应
	 */
	protected Response sendGet(final String endpoint) {
		return HttpUtil.createRequest(config.getApiUrl() + endpoint, Method.GET)
			.header(HeaderName.ACCEPT, ContentType.JSON.getValue())
			.bearerAuth(config.getApiKey())
			.send(this.clientEngine);
	}

	/**
	 * 发送Post请求
	 * @param endpoint 请求节点
	 * @param paramJson 请求参数json
	 * @return 请求响应
	 */
	protected Response sendPost(final String endpoint, final String paramJson) {
		return HttpUtil.createRequest(config.getApiUrl() + endpoint, Method.POST)
			.header(HeaderName.CONTENT_TYPE, ContentType.JSON.getValue())
			.header(HeaderName.ACCEPT, ContentType.JSON.getValue())
			.bearerAuth(config.getApiKey())
			.body(paramJson)
			.send(this.clientEngine);
	}

	/**
	 * 发送表单请求
	 * @param endpoint 请求节点
	 * @param paramMap 请求参数map
	 * @return 请求响应
	 */
	protected Response sendFormData(final String endpoint, final Map<String, Object> paramMap) {
		return HttpUtil.createPost(config.getApiUrl() + endpoint)
			//form表单中有file对象会自动将文件编码为 multipart/form-data 格式。不需要设置
//				.header(HeaderName.CONTENT_TYPE, "multipart/form-data")
			.bearerAuth(config.getApiKey())
			.form(paramMap)
			.send(this.clientEngine);
	}

	/**
	 * 支持流式返回的 POST 请求
	 *
	 * @param endpoint 请求地址
	 * @param paramMap 请求参数
	 * @param callback 流式数据回调函数
	 */
	protected void sendPostStream(final String endpoint, final Map<String, Object> paramMap, final Consumer<String> callback) {
		final Response response = HttpUtil.createPost(config.getApiUrl() + endpoint)
			//form表单中有file对象会自动将文件编码为 multipart/form-data 格式。不需要设置
//				.header(HeaderName.CONTENT_TYPE, "multipart/form-data")
			.bearerAuth(config.getApiKey())
			.form(paramMap)
			.send(this.clientEngine);

		// 读取流式响应
		try (final BufferedReader reader = IoUtil.toUtf8Reader(response.bodyStream())) {
			String line;
			while ((line = reader.readLine()) != null) {
				// 调用回调函数处理每一行数据
				callback.accept(line);
			}
		} catch (final IOException e){
			callback.accept("{\"error\": \"" + e.getMessage() + "\"}");
		}
	}
}
