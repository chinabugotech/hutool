package org.dromara.hutool.ai.core;

import java.util.Map;

/**
 * AI配置类
 *
 * @author elichow
 * @since 6.0.0
 */
public interface AIConfig {

	/**
	 * 获取模型（厂商）名称
	 *
	 * @return 模型（厂商）名称
	 * @since 6.0.0
	 */
	default String getModelName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * 设置apiKey
	 *
	 * @param apiKey apiKey
	 * @since 6.0.0
	 */
	void setApiKey(String apiKey);

	/**
	 * 获取apiKey
	 *
	 * @return apiKey
	 * @since 6.0.0
	 */
	String getApiKey();

	/**
	 * 设置apiUrl
	 *
	 * @param apiUrl api请求地址
	 * @since 6.0.0
	 */
	void setApiUrl(String apiUrl);

	/**
	 * 获取apiUrl
	 *
	 * @return apiUrl
	 * @since 6.0.0
	 */
	String getApiUrl();

	/**
	 * 设置model
	 *
	 * @param model model
	 * @since 6.0.0
	 */
	void setModel(String model);

	/**
	 * 返回model
	 *
	 * @return model
	 * @since 6.0.0
	 */
	String getModel();

	/**
	 * 设置动态参数
	 *
	 * @param key   参数字段
	 * @param value 参数值
	 * @since 6.0.0
	 */
	void putAdditionalConfigByKey(String key, Object value);

	/**
	 * 获取动态参数
	 *
	 * @param key 参数字段
	 * @return 参数值
	 * @since 6.0.0
	 */
	Object getAdditionalConfigByKey(String key);

	/**
	 * 获取动态参数列表
	 *
	 * @return 参数列表Map
	 * @since 6.0.0
	 */
	Map<String, Object> getAdditionalConfigMap();

}
