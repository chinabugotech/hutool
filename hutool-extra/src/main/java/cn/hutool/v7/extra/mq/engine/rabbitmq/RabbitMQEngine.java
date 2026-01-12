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

package cn.hutool.v7.extra.mq.engine.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.extra.mq.Consumer;
import cn.hutool.v7.extra.mq.MQConfig;
import cn.hutool.v7.extra.mq.MQException;
import cn.hutool.v7.extra.mq.Producer;
import cn.hutool.v7.extra.mq.engine.MQEngine;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ引擎
 *
 * @author Looly
 * @since 7.0.0
 */
public class RabbitMQEngine implements MQEngine, Closeable {

	private Connection connection;

	/**
	 * 默认构造
	 */
	public RabbitMQEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(com.rabbitmq.client.Connection.class);
	}

	/**
	 * 构造
	 *
	 * @param config 配置
	 */
	public RabbitMQEngine(final MQConfig config){
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param factory 连接工厂
	 */
	@SuppressWarnings("resource")
	public RabbitMQEngine(final ConnectionFactory factory) {
		init(factory);
	}

	@Override
	public RabbitMQEngine init(final MQConfig config) {
		return init(createFactory(config));
	}

	/**
	 * 初始化
	 *
	 * @param factory 连接工厂
	 * @return this
	 */
	public RabbitMQEngine init(final ConnectionFactory factory){
		try {
			this.connection = factory.newConnection();
		} catch (final IOException | TimeoutException e) {
			throw new MQException(e);
		}
		return this;
	}

	@Override
	public Producer getProducer() {
		return new RabbitMQProducer(createChannel());
	}

	@Override
	public Consumer getConsumer() {
		return new RabbitMQConsumer(createChannel());
	}

	@Override
	public void close() throws IOException {
		IoUtil.nullSafeClose(this.connection);
	}

	/**
	 * 创建Channel
	 *
	 * @return Channel
	 */
	private Channel createChannel() {
		try {
			return this.connection.createChannel();
		} catch (final IOException e) {
			throw new MQException(e);
		}
	}

	/**
	 * 创建连接工厂
	 *
	 * @param config 配置
	 * @return 连接工厂
	 */
	private static ConnectionFactory createFactory(final MQConfig config) {
		final ConnectionFactory factory = new ConnectionFactory();
		try {
			factory.setUri(config.getBrokerUrl());
		} catch (final Exception e) {
			throw new MQException(e);
		}

		// TODO 配置其他参数

		return factory;
	}
}
