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

package cn.hutool.v7.extra.mq.engine.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.extra.mq.Consumer;
import cn.hutool.v7.extra.mq.MQConfig;
import cn.hutool.v7.extra.mq.Producer;
import cn.hutool.v7.extra.mq.engine.MQEngine;

import java.util.Properties;

/**
 * Kafka引擎
 *
 * @author Looly
 * @since 7.0.0
 */
public class KafkaEngine implements MQEngine {

	private Properties properties;

	/**
	 * 默认构造
	 */
	public KafkaEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(org.apache.kafka.clients.CommonClientConfigs.class);
	}

	/**
	 * 构造
	 *
	 * @param config 配置
	 */
	public KafkaEngine(final MQConfig config) {
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param properties 配置
	 */
	public KafkaEngine(final Properties properties) {
		init(properties);
	}

	@Override
	public KafkaEngine init(final MQConfig config) {
		return init(buidProperties(config));
	}

	/**
	 * 初始化
	 *
	 * @param properties 配置
	 * @return this
	 */
	public KafkaEngine init(final Properties properties) {
		this.properties = properties;
		return this;
	}

	/**
	 * 增加配置项
	 *
	 * @param key   配置项
	 * @param value 值
	 * @return this
	 */
	public KafkaEngine addProperty(final String key, final String value) {
		this.properties.put(key, value);
		return this;
	}

	@Override
	public Producer getProducer() {
		return new KafkaProducer(this.properties);
	}

	@Override
	public Consumer getConsumer() {
		return new KafkaConsumer(this.properties);
	}

	/**
	 * 构建配置
	 *
	 * @param config 配置
	 * @return 配置
	 */
	private static Properties buidProperties(final MQConfig config) {
		final Properties properties = new Properties();
		properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, config.getBrokerUrl());
		properties.putAll(config.getProperties());
		return properties;
	}
}
