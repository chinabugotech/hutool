/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

package cn.hutool.ai.core;

import cn.hutool.ai.AIException;
import cn.hutool.http.*;
import cn.hutool.json.JSONUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;

import static cn.hutool.core.thread.GlobalThreadPool.execute;

/**
 * 基础AIService，包含基公共参数和公共方法
 *
 * @author elichow
 * @since 5.8.38
 */
public class BaseAIService {

	protected final AIConfig config;

	/**
	 * 构造方法
	 *
	 * @param config AI配置
	 */
	public BaseAIService(final AIConfig config) {
		this.config = config;
	}

	/**
	 * 发送Get请求
	 * @param endpoint 请求节点
	 * @return 请求响应
	 */
	protected HttpResponse sendGet(final String endpoint) {
		//链式构建请求
		try {
			//设置超时3分钟
			HttpRequest httpRequest = HttpRequest.get(config.getApiUrl() + endpoint)
				.header(Header.ACCEPT, "application/json")
				.header(Header.AUTHORIZATION, "Bearer " + config.getApiKey())
				.timeout(config.getTimeout());
			if (config.getHasProxy()) {
				httpRequest.setProxy(config.getProxy());
			}
			return httpRequest.execute();
		} catch (final Exception e) {
			throw new AIException("Failed to send GET request: " + e.getMessage(), e);
		}
	}

	/**
	 * 发送Post请求
	 * @param endpoint 请求节点
	 * @param paramJson 请求参数json
	 * @return 请求响应
	 */
	protected HttpResponse sendPost(final String endpoint, final String paramJson) {
		//链式构建请求
		try {
			HttpRequest httpRequest = HttpRequest.post(config.getApiUrl() + endpoint)
				.header(Header.CONTENT_TYPE, "application/json")
				.header(Header.ACCEPT, "application/json")
				.header(Header.AUTHORIZATION, "Bearer " + config.getApiKey())
				.body(paramJson)
				.timeout(config.getTimeout());
			if (config.getHasProxy()) {
				httpRequest.setProxy(config.getProxy());
			}
			return httpRequest.execute();
		} catch (final Exception e) {
			throw new AIException("Failed to send POST request：" + e.getMessage(), e);
		}

	}

	/**
	 * 发送表单请求
	 * @param endpoint 请求节点
	 * @param paramMap 请求参数map
	 * @return 请求响应
	 */
	protected HttpResponse sendFormData(final String endpoint, final Map<String, Object> paramMap) {
		//链式构建请求
		try {
			//设置超时3分钟
			HttpRequest httpRequest = HttpRequest.post(config.getApiUrl() + endpoint)
				.header(Header.CONTENT_TYPE, "multipart/form-data")
				.header(Header.ACCEPT, "application/json")
				.header(Header.AUTHORIZATION, "Bearer " + config.getApiKey())
				.form(paramMap)
				.timeout(config.getTimeout());
			if (config.getHasProxy()) {
				httpRequest.setProxy(config.getProxy());
			}
			return httpRequest.execute();
		} catch (final Exception e) {
			throw new AIException("Failed to send POST request：" + e.getMessage(), e);
		}
	}

	/**
	 * 支持流式返回的 POST 请求
	 *
	 * @param endpoint 请求地址
	 * @param paramMap 请求参数
	 * @param callback 流式数据回调函数
	 */
	protected void sendPostStream(final String endpoint, final Map<String, Object> paramMap, Consumer<String> callback) {
			HttpURLConnection connection = null;
			try {
				// 创建连接
				URL apiUrl = new URL(config.getApiUrl() + endpoint);
				connection = (HttpURLConnection) apiUrl.openConnection();
				if (config.getHasProxy()) {
					connection = (HttpURLConnection) apiUrl.openConnection(config.getProxy());
				}
				connection.setRequestMethod(Method.POST.name());
				connection.setRequestProperty(Header.CONTENT_TYPE.getValue(), "application/json");
				connection.setRequestProperty(Header.AUTHORIZATION.getValue(), "Bearer " + config.getApiKey());
				connection.setDoOutput(true);
				//5分钟
				connection.setReadTimeout(config.getReadTimeout());
				//3分钟
				connection.setConnectTimeout(config.getTimeout());
				// 发送请求体
				try (OutputStream os = connection.getOutputStream()) {
					String jsonInputString = JSONUtil.toJsonStr(paramMap);
					os.write(jsonInputString.getBytes());
					os.flush();
				}

				// 读取流式响应
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
					String line;
					while ((line = reader.readLine()) != null) {
						// 调用回调函数处理每一行数据
						callback.accept(line);
					}
				}
			} catch (Exception e) {
				callback.accept("{\"error\": \"" + e.getMessage() + "\"}");
			} finally {
				// 关闭连接
				if (connection != null) {
					connection.disconnect();
				}
			}
	}
}
