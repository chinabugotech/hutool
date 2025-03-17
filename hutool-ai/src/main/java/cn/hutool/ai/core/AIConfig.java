package cn.hutool.ai.core;

import java.util.Map;

/**
 * AI配置类
 *
 * @author elichow
 * @since 5.8.37
 */
public interface AIConfig {

	/**
	 * 获取模型（厂商）名称
	 *
	 * @return 模型（厂商）名称
	 * @since 5.8.37
	 */
	default String getModelName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * 设置apiKey
	 *
	 * @param apiKey apiKey
	 * @since 5.8.37
	 */
	void setApiKey(String apiKey);

	/**
	 * 获取apiKey
	 *
	 * @return apiKey
	 * @since 5.8.37
	 */
	String getApiKey();

	/**
	 * 设置apiUrl
	 *
	 * @param apiUrl api请求地址
	 * @since 5.8.37
	 */
	void setApiUrl(String apiUrl);

	/**
	 * 获取apiUrl
	 *
	 * @return apiUrl
	 * @since 5.8.37
	 */
	String getApiUrl();

	/**
	 * 设置model
	 *
	 * @param model model
	 * @since 5.8.37
	 */
	void setModel(String model);

	/**
	 * 返回model
	 *
	 * @return model
	 * @since 5.8.37
	 */
	String getModel();

	/**
	 * 设置动态参数
	 *
	 * @param key   参数字段
	 * @param value 参数值
	 * @since 5.8.37
	 */
	void putAdditionalConfigByKey(String key, Object value);

	/**
	 * 获取动态参数
	 *
	 * @param key 参数字段
	 * @return 参数值
	 * @since 5.8.37
	 */
	Object getAdditionalConfigByKey(String key);

	/**
	 * 获取动态参数列表
	 *
	 * @return 参数列表Map
	 * @since 5.8.37
	 */
	Map<String, Object> getAdditionalConfigMap();

}
