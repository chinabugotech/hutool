package org.dromara.hutool.ai.core;


import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;

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
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String getApiKey() {
		return apiKey;
	}

	@Override
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	@Override
	public String getApiUrl() {
		return apiUrl;
	}

	@Override
	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String getModel() {
		return model;
	}

	@Override
	public void putAdditionalConfigByKey(String key, Object value) {
		this.additionalConfig.put(key, value);
	}

	@Override
	public Object getAdditionalConfigByKey(String key) {
		return additionalConfig.get(key);
	}

	@Override
	public Map<String, Object> getAdditionalConfigMap() {
		return new SafeConcurrentHashMap<>(additionalConfig);
	}

}
