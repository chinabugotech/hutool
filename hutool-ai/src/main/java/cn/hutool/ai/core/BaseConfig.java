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
import java.util.concurrent.ConcurrentHashMap;

/**
 * Config基础类，定义模型配置的基本属性
 *
 * @author elichow
 * @since 5.8.38
 */
public class BaseConfig implements AIConfig {

	//apiKey
	protected volatile String apiKey;
	//API请求地址
	protected volatile String apiUrl;
	//具体模型
	protected volatile String model;
	//动态扩展字段
	protected Map<String, Object> additionalConfig = new ConcurrentHashMap<>();
	//连接超时时间
	protected volatile int timeout = 180000;
	//读取超时时间
	protected volatile int readTimeout = 300000;
	//是否设置代理
	protected volatile boolean hasProxy = false;
	//代理设置
	protected volatile Proxy proxy;

	@Override
	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String getApiKey() {
		return apiKey;
	}

	@Override
	public void setApiUrl(final String apiUrl) {
		this.apiUrl = apiUrl;
	}

	@Override
	public String getApiUrl() {
		return apiUrl;
	}

	@Override
	public void setModel(final String model) {
		this.model = model;
	}

	@Override
	public String getModel() {
		return model;
	}

	@Override
	public void putAdditionalConfigByKey(final String key, final Object value) {
		this.additionalConfig.put(key, value);
	}

	@Override
	public Object getAdditionalConfigByKey(final String key) {
		return additionalConfig.get(key);
	}

	@Override
	public Map<String, Object> getAdditionalConfigMap() {
		return new ConcurrentHashMap<>(additionalConfig);
	}

	@Override
	public int getTimeout() {
		return timeout;
	}

	@Override
	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	@Override
	public int getReadTimeout() {
		return readTimeout;
	}

	@Override
	public void setReadTimeout(final int readTimeout) {
		this.readTimeout = readTimeout;
	}

	@Override
	public boolean getHasProxy() {
		return hasProxy;
	}

	@Override
	public void setHasProxy(boolean hasProxy) {
		this.hasProxy = hasProxy;
	}

	@Override
	public Proxy getProxy() {
		return proxy;
	}

	@Override
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}
}
