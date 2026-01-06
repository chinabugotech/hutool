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

package cn.hutool.v7.extra.mq.engine.jms;

import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.extra.mq.Consumer;
import cn.hutool.v7.extra.mq.MQConfig;
import cn.hutool.v7.extra.mq.MQException;
import cn.hutool.v7.extra.mq.Producer;
import cn.hutool.v7.extra.mq.engine.MQEngine;
import jakarta.jms.*;

import java.io.Closeable;
import java.io.IOException;

/**
 * JMS(Java Message Service)引擎
 *
 * @author Looly
 * @since 7.0.0
 */
public abstract class JmsEngine implements MQEngine, Closeable {

	private Connection connection;
	private Session session;
	private boolean isTopic;
	private String producerGroup = "hutool.queue";
	private String consumerGroup = "hutool.queue";

	@Override
	public MQEngine init(final MQConfig config) {
		try {
			this.connection = createConnectionFactory(config).createConnection();
			this.session = this.connection.createSession();
		} catch (final JMSException e) {
			throw new MQException(e);
		}
		return this;
	}

	/**
	 * 创建ConnectionFactory
	 *
	 * @param config 配置
	 * @return ConnectionFactory
	 */
	protected abstract ConnectionFactory createConnectionFactory(final MQConfig config);

	/**
	 * 设置是否Topic
	 *
	 * @param isTopic 是否Topic
	 * @return this
	 */
	public JmsEngine setTopic(final boolean isTopic) {
		this.isTopic = isTopic;
		return this;
	}

	/**
	 * 设置生产者组
	 *
	 * @param producerGroup 生产者组
	 * @return this
	 */
	public JmsEngine setProducerGroup(final String producerGroup) {
		this.producerGroup = producerGroup;
		return this;
	}

	/**
	 * 设置消费者组
	 *
	 * @param consumerGroup 消费者组
	 * @return this
	 */
	public JmsEngine setConsumerGroup(final String consumerGroup) {
		this.consumerGroup = consumerGroup;
		return this;
	}

	@Override
	public Producer getProducer() {
		final MessageProducer messageProducer;
		try {
			messageProducer = this.session.createProducer(createDestination(producerGroup));
		} catch (final JMSException e) {
			throw new MQException(e);
		}

		return new JmsProducer(this.session, messageProducer);
	}

	@Override
	public Consumer getConsumer() {
		final MessageConsumer messageConsumer;
		try {
			messageConsumer = this.session.createConsumer(createDestination(consumerGroup));
		} catch (final JMSException e) {
			throw new MQException(e);
		}

		return new JmsConsumer(this.consumerGroup, messageConsumer);
	}

	@Override
	public void close() throws IOException {
		IoUtil.closeQuietly(this.session);
		IoUtil.closeQuietly(this.connection);
	}

	/**
	 * 创建Destination
	 *
	 * @param group 组
	 * @return Destination
	 */
	private Destination createDestination(final String group) {
		try {
			return isTopic ? this.session.createTopic(group) : this.session.createQueue(group);
		} catch (final JMSException e) {
			throw new MQException(e);
		}
	}
}
