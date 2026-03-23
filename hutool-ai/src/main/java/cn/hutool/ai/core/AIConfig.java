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

import java.net.Proxy;
import java.util.Map;

/**
 * AI配置类
 *
 * @author elichow
 * @since 5.8.38
 */
public interface AIConfig {

	/**
	 * 获取模型（厂商）名称
	 *
	 * @return 模型（厂商）名称
	 * @since 5.8.38
	 */
	default String getModelName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * 设置apiKey
	 *
	 * @param apiKey apiKey
	 * @since 5.8.38
	 */
	void setApiKey(String apiKey);

	/**
	 * 获取apiKey
	 *
	 * @return apiKey
	 * @since 5.8.38
	 */
	String getApiKey();

	/**
	 * 设置apiUrl
	 *
	 * @param apiUrl api请求地址
	 * @since 5.8.38
	 */
	void setApiUrl(String apiUrl);

	/**
	 * 获取apiUrl
	 *
	 * @return apiUrl
	 * @since 5.8.38
	 */
	String getApiUrl();

	/**
	 * 设置model
	 *
	 * @param model model
	 * @since 5.8.38
	 */
	void setModel(String model);

	/**
	 * 返回model
	 *
	 * @return model
	 * @since 5.8.38
	 */
	String getModel();

	/**
	 * 设置动态参数
	 *
	 * @param key   参数字段
	 * @param value 参数值
	 * @since 5.8.38
	 */
	void putAdditionalConfigByKey(String key, Object value);

	/**
	 * 获取动态参数
	 *
	 * @param key 参数字段
	 * @return 参数值
	 * @since 5.8.38
	 */
	Object getAdditionalConfigByKey(String key);

	/**
	 * 获取动态参数列表
	 *
	 * @return 参数列表Map
	 * @since 5.8.38
	 */
	Map<String, Object> getAdditionalConfigMap();

	/**
	 * 设置连接超时时间
	 *
	 * @param timeout 连接超时时间
	 * @since 5.8.39
	 */
	void setTimeout(int timeout);

	/**
	 * 获取连接超时时间
	 *
	 * @return timeout
	 * @since 5.8.39
	 */
	int getTimeout();

	/**
	 * 设置读取超时时间
	 *
	 * @param readTimeout 连接超时时间
	 * @since 5.8.39
	 */
	void setReadTimeout(int readTimeout);

	/**
	 * 获取读取超时时间
	 *
	 * @return readTimeout
	 * @since 5.8.39
	 */
	int getReadTimeout();

	/**
	 * 获取是否使用代理
	 *
	 * @return hasProxy
	 * @since 5.8.42
	 */
	boolean getHasProxy();

	/**
	 * 设置是否使用代理
	 *
	 * @param hasProxy 是否使用代理
	 * @since 5.8.42
	 */
	void setHasProxy(boolean hasProxy);

	/**
	 * 获取代理配置
	 *
	 * @return proxy
	 * @since 5.8.42
	 */
	Proxy getProxy();

	/**
	 * 设置代理配置
	 *
	 * @param proxy 连接超时时间
	 * @since 5.8.42
	 */
	void setProxy(Proxy proxy);

}
