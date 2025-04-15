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

import cn.hutool.v7.core.map.concurrent.SafeConcurrentHashMap;

import java.util.Map;

/**
 * Config基础类，定义模型配置的基本属性
 *
 * @author elichow
 * @since 6.0.0
 */
public class BaseConfig implements AIConfig {

	//apiKey
	protected volatile String apiKey;
	//API请求地址
	protected volatile String apiUrl;
	//具体模型
	protected volatile String model;
	//动态扩展字段
	protected Map<String, Object> additionalConfig = new SafeConcurrentHashMap<>();

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
		return new SafeConcurrentHashMap<>(additionalConfig);
	}

}
