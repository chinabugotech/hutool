package org.dromara.hutool.ai.core;

import java.lang.reflect.Constructor;

/**
 * 用于AIConfig的创建，创建同时支持链式设置参数
 *
 * @author elichow
 * @since 6.0.0
 */
public class AIConfigBuilder {

	private final AIConfig config;

	/**
	 * 构造
	 *
	 * @param modelName 模型厂商的名称（注意不是指具体的模型）
	 */
	public AIConfigBuilder(final String modelName) {
		try {
			// 获取配置类
			final Class<? extends AIConfig> configClass = AIConfigRegistry.getConfigClass(modelName);
			if (configClass == null) {
				throw new IllegalArgumentException("Unsupported model: " + modelName);
			}

			// 使用反射创建实例
			final Constructor<? extends AIConfig> constructor = configClass.getDeclaredConstructor();
			config = constructor.newInstance();
		} catch (final Exception e) {
			throw new RuntimeException("Failed to create AIConfig instance", e);
		}
	}

	/**
	 * 设置apiKey
	 *
	 * @param apiKey apiKey
	 * @return config
	 * @since 6.0.0
	 */
	public synchronized AIConfigBuilder setApiKey(final String apiKey) {
		if (apiKey != null) {
			config.setApiKey(apiKey);
		}
		return this;
	}

	/**
	 * 设置AI模型请求API接口的地址，不设置为默认值
	 *
	 * @param apiUrl API接口地址
	 * @return config
	 * @since 6.0.0
	 */
	public synchronized AIConfigBuilder setApiUrl(final String apiUrl) {
		if (apiUrl != null) {
			config.setApiUrl(apiUrl);
		}
		return this;
	}

	/**
	 * 设置具体的model，不设置为默认值
	 *
	 * @param model 具体model的名称
	 * @return config
	 * @since 6.0.0
	 */
	public synchronized AIConfigBuilder setModel(final String model) {
		if (model != null) {
			config.setModel(model);
		}
		return this;
	}

	/**
	 * 动态设置Request请求体中的属性字段，每个模型功能支持的字段请参照对应的官方文档
	 *
	 * @param key   Request中的支持的属性名
	 * @param value 设置的属性值
	 * @return config
	 * @since 6.0.0
	 */
	public AIConfigBuilder putAdditionalConfig(final String key, final Object value) {
		if (value != null) {
			config.putAdditionalConfigByKey(key, value);
		}
		return this;
	}

	/**
	 * 返回config实例
	 *
	 * @return config
	 * @since 6.0.0
	 */
	public AIConfig build() {
		return config;
	}
}
