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

package cn.hutool.v7.ai.core;

import cn.hutool.v7.ai.AIException;
import cn.hutool.v7.http.HttpGlobalConfig;
import cn.hutool.v7.http.HttpUtil;
import cn.hutool.v7.http.client.Response;
import cn.hutool.v7.http.meta.HeaderName;
import cn.hutool.v7.http.meta.Method;
import cn.hutool.v7.json.JSONUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 基础AIService，包含基公共参数和公共方法
 *
 * @author elichow
 * @since 6.0.0
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
	protected Response sendGet(final String endpoint) {
		//链式构建请求
		try {
			//设置超时
			HttpGlobalConfig.setTimeout(config.getTimeout());
			return HttpUtil.createRequest(config.getApiUrl() + endpoint, Method.GET)
				.header(HeaderName.ACCEPT, "application/json")
				.header(HeaderName.AUTHORIZATION, "Bearer " + config.getApiKey())
				.send();
		} catch (final AIException e) {
			throw new AIException("Failed to send GET request: " + e.getMessage(), e);
		}
	}

	/**
	 * 发送Post请求
	 * @param endpoint 请求节点
	 * @param paramJson 请求参数json
	 * @return 请求响应
	 */
	protected Response sendPost(final String endpoint, final String paramJson) {
		//链式构建请求
		try {
			//设置超时3分钟
			HttpGlobalConfig.setTimeout(config.getTimeout());
			return HttpUtil.createRequest(config.getApiUrl() + endpoint, Method.POST)
				.header(HeaderName.CONTENT_TYPE, "application/json")
				.header(HeaderName.ACCEPT, "application/json")
				.header(HeaderName.AUTHORIZATION, "Bearer " + config.getApiKey())
				.body(paramJson)
				.send();
		} catch (final AIException e) {
			throw new AIException("Failed to send POST request：" + e.getMessage(), e);
		}

	}

	/**
	 * 发送表单请求
	 * @param endpoint 请求节点
	 * @param paramMap 请求参数map
	 * @return 请求响应
	 */
	protected Response sendFormData(final String endpoint, final Map<String, Object> paramMap) {
		//链式构建请求
		try {
			//设置超时3分钟
			HttpGlobalConfig.setTimeout(config.getTimeout());
			return HttpUtil.createPost(config.getApiUrl() + endpoint)
				//form表单中有file对象会自动将文件编码为 multipart/form-data 格式。不需要设置
//				.header(HeaderName.CONTENT_TYPE, "multipart/form-data")
				.header(HeaderName.AUTHORIZATION, "Bearer " + config.getApiKey())
				.form(paramMap)
				.send();
		} catch (final AIException e) {
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
			connection.setRequestMethod(Method.POST.name());
			connection.setRequestProperty(HeaderName.CONTENT_TYPE.getValue(), "application/json");
			connection.setRequestProperty(HeaderName.AUTHORIZATION.getValue(), "Bearer " + config.getApiKey());
			connection.setDoOutput(true);
			//设置读取超时
			connection.setReadTimeout(config.getReadTimeout());
			//设置连接超时
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
