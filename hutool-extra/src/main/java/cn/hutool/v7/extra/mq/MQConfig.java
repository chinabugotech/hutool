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

package cn.hutool.v7.extra.mq;

import cn.hutool.v7.extra.mq.engine.MQEngine;

import java.io.Serial;
import java.io.Serializable;
import java.util.Properties;

/**
 * MQ配置
 *
 * @author Looly
 * @since 6.0.0
 */
public class MQConfig implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 创建配置
	 *
	 * @param brokerUrl Broker地址
	 * @return 配置
	 */
	public static MQConfig of(final String brokerUrl) {
		return new MQConfig(brokerUrl);
	}

	private String brokerUrl;
	private Properties properties;
	/**
	 * 自定义引擎，当多个jar包引入时，可以自定使用的默认引擎
	 */
	private Class<? extends MQEngine> customEngine;

	/**
	 * 构造
	 *
	 * @param brokerUrl Broker地址
	 */
	public MQConfig(final String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	/**
	 * 获取Broker地址
	 *
	 * @return Broker地址
	 */
	public String getBrokerUrl() {
		return brokerUrl;
	}

	/**
	 * 设置Broker地址
	 *
	 * @param brokerUrl Broker地址
	 * @return this
	 */
	public MQConfig setBrokerUrl(final String brokerUrl) {
		this.brokerUrl = brokerUrl;
		return this;
	}

	/**
	 * 获取配置
	 *
	 * @return 配置
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * 设置配置
	 *
	 * @param properties 配置
	 * @return this
	 */
	public MQConfig setProperties(final Properties properties) {
		this.properties = properties;
		return this;
	}

	/**
	 * 添加配置项
	 *
	 * @param key   键
	 * @param value 值
	 * @return this
	 */
	public Properties addProperty(final String key, final String value) {
		if (null == this.properties) {
			this.properties = new Properties();
		}
		this.properties.setProperty(key, value);
		return this.properties;
	}

	/**
	 * 自定义引擎，当多个jar包引入时，可以自定使用的默认引擎
	 *
	 * @return 自定义引擎
	 */
	public Class<? extends MQEngine> getCustomEngine() {
		return customEngine;
	}

	/**
	 * 自定义引擎，当多个jar包引入时，可以自定使用的默认引擎
	 *
	 * @param customEngine 自定义引擎
	 * @return this
	 */
	public MQConfig setCustomEngine(final Class<? extends MQEngine> customEngine) {
		this.customEngine = customEngine;
		return this;
	}
}
