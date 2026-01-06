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

package cn.hutool.v7.extra.mq.engine.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.MixAll;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.extra.mq.Consumer;
import cn.hutool.v7.extra.mq.MQConfig;
import cn.hutool.v7.extra.mq.MQException;
import cn.hutool.v7.extra.mq.Producer;
import cn.hutool.v7.extra.mq.engine.MQEngine;

/**
 * RocketMQ引擎
 *
 * @author Looly
 * @since 6.0.0
 */
public class RocketMQEngine implements MQEngine {

	private MQConfig config;
	private String producerGroup;
	private String consumerGroup;

	/**
	 * 默认构造
	 */
	public RocketMQEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull( org.apache.rocketmq.common.message.Message.class);
		this.producerGroup = MixAll.DEFAULT_PRODUCER_GROUP;
		this.consumerGroup = MixAll.DEFAULT_CONSUMER_GROUP;
	}

	/**
	 * 设置生产者组
	 *
	 * @param producerGroup 生产者组
	 * @return this
	 */
	public RocketMQEngine setProducerGroup(final String producerGroup) {
		this.producerGroup = producerGroup;
		return this;
	}

	/**
	 * 设置消费者组
	 *
	 * @param consumerGroup 消费者组
	 * @return this
	 */
	public RocketMQEngine setConsumerGroup(final String consumerGroup) {
		this.consumerGroup = consumerGroup;
		return this;
	}

	@Override
	public RocketMQEngine init(final MQConfig config) {
		this.config = config;
		return this;
	}

	@Override
	public Producer getProducer() {
		final DefaultMQProducer defaultMQProducer = new DefaultMQProducer(producerGroup);
		defaultMQProducer.setNamesrvAddr(config.getBrokerUrl());
		try {
			defaultMQProducer.start();
		} catch (final MQClientException e) {
			throw new MQException(e);
		}
		return new RocketMQProducer(defaultMQProducer);
	}

	@Override
	public Consumer getConsumer() {
		final DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup);
		defaultMQPushConsumer.setNamesrvAddr(config.getBrokerUrl());
		return new RocketMQConsumer(defaultMQPushConsumer);
	}
}
