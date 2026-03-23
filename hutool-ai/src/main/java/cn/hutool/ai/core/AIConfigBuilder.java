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

import java.lang.reflect.Constructor;
import java.net.Proxy;

/**
 * 用于AIConfig的创建，创建同时支持链式设置参数
 *
 * @author elichow
 * @since 5.8.38
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
	 * @since 5.8.38
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
	 * @since 5.8.38
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
	 * @since 5.8.38
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
	 * @since 5.8.38
	 */
	public AIConfigBuilder putAdditionalConfig(final String key, final Object value) {
		if (value != null) {
			config.putAdditionalConfigByKey(key, value);
		}
		return this;
	}

	/**
	 * 设置连接超时时间，不设置为默认值
	 *
	 * @param timeout 超时时间
	 * @return config
	 * @since 5.8.39
	 * @deprecated 请使用 {@link #setTimeout(int)}
	 */
	@Deprecated
	public AIConfigBuilder setTimout(final int timeout) {
		return setTimeout(timeout);
	}

	/**
	 * 设置连接超时时间，不设置为默认值
	 *
	 * @param timeout 超时时间
	 * @return config
	 * @since 5.8.41
	 */
	public synchronized AIConfigBuilder setTimeout(final int timeout) {
		if (timeout > 0) {
			config.setTimeout(timeout);
		}
		return this;
	}

	/**
	 * 设置读取超时时间，不设置为默认值
	 *
	 * @param readTimout 取超时时间
	 * @return config
	 * @since 5.8.39
	 * @deprecated 请使用 {@link #setReadTimeout(int)}
	 */
	@Deprecated
	public AIConfigBuilder setReadTimout(final int readTimout) {
		return setReadTimeout(readTimout);
	}

	/**
	 * 设置读取超时时间，不设置为默认值
	 *
	 * @param readTimeout 取超时时间
	 * @return config
	 * @since 5.8.41
	 */
	public synchronized AIConfigBuilder setReadTimeout(final int readTimeout) {
		if (readTimeout > 0) {
			config.setReadTimeout(readTimeout);
		}
		return this;
	}

	/**
	 * 设置代理
	 *
	 * @param proxy 取超时时间
	 * @return config
	 * @since 5.8.42
	 */
	public synchronized AIConfigBuilder setProxy(final Proxy proxy) {
		if (null != proxy) {
			config.setHasProxy(true);
			config.setProxy(proxy);
		}
		return this;
	}

	/**
	 * 返回config实例
	 *
	 * @return config
	 * @since 5.8.38
	 */
	public AIConfig build() {
		return config;
	}
}
