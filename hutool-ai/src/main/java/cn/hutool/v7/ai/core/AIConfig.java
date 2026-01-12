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

import cn.hutool.v7.http.proxy.ProxyInfo;

import java.util.Map;

/**
 * AI配置类
 *
 * @author elichow
 * @since 7.0.0
 */
public interface AIConfig {

	/**
	 * 获取模型（厂商）名称
	 *
	 * @return 模型（厂商）名称
	 * @since 7.0.0
	 */
	default String getModelName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * 设置apiKey
	 *
	 * @param apiKey apiKey
	 * @since 7.0.0
	 */
	void setApiKey(String apiKey);

	/**
	 * 获取apiKey
	 *
	 * @return apiKey
	 * @since 7.0.0
	 */
	String getApiKey();

	/**
	 * 设置apiUrl
	 *
	 * @param apiUrl api请求地址
	 * @since 7.0.0
	 */
	void setApiUrl(String apiUrl);

	/**
	 * 获取apiUrl
	 *
	 * @return apiUrl
	 * @since 7.0.0
	 */
	String getApiUrl();

	/**
	 * 设置model
	 *
	 * @param model model
	 * @since 7.0.0
	 */
	void setModel(String model);

	/**
	 * 返回model
	 *
	 * @return model
	 * @since 7.0.0
	 */
	String getModel();

	/**
	 * 设置动态参数
	 *
	 * @param key   参数字段
	 * @param value 参数值
	 * @since 7.0.0
	 */
	void putAdditionalConfigByKey(String key, Object value);

	/**
	 * 获取动态参数
	 *
	 * @param key 参数字段
	 * @return 参数值
	 * @since 7.0.0
	 */
	Object getAdditionalConfigByKey(String key);

	/**
	 * 获取动态参数列表
	 *
	 * @return 参数列表Map
	 * @since 7.0.0
	 */
	Map<String, Object> getAdditionalConfigMap();

	/**
	 * 设置连接超时时间
	 *
	 * @param timeout 连接超时时间
	 * @since 7.0.0
	 */
	void setTimeout(int timeout);

	/**
	 * 获取连接超时时间
	 *
	 * @return timeout
	 * @since 7.0.0
	 */
	int getTimeout();

	/**
	 * 设置读取超时时间
	 *
	 * @param readTimeout 连接超时时间
	 * @since 7.0.0
	 */
	void setReadTimeout(int readTimeout);

	/**
	 * 获取读取超时时间
	 *
	 * @return readTimeout
	 * @since 7.0.0
	 */
	int getReadTimeout();

	/**
	 * 获取http代理
	 *
	 * @return http代理
	 * @since 7.0.0
	 */
	ProxyInfo getProxy();

	/**
	 * 设置代理配置
	 *
	 * @param proxy 连接超时时间
	 * @since 7.0.0
	 */
	void setProxy(ProxyInfo proxy);
}
