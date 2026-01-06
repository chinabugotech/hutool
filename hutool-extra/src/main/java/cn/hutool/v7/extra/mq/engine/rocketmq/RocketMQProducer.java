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

import org.apache.rocketmq.client.producer.MQProducer;
import cn.hutool.v7.extra.mq.MQException;
import cn.hutool.v7.extra.mq.Message;
import cn.hutool.v7.extra.mq.Producer;

import java.io.IOException;

/**
 * RocketMQ Producer
 *
 * @author Looly
 * @since 6.0.0
 */
public class RocketMQProducer implements Producer {

	private final MQProducer producer;

	/**
	 * 构造
	 *
	 * @param producer RocketMQ Producer
	 */
	public RocketMQProducer(final MQProducer producer) {
		this.producer = producer;
	}

	@Override
	public void send(final Message message) {
		final org.apache.rocketmq.common.message.Message rocketMessage =
			new org.apache.rocketmq.common.message.Message(message.topic(), message.content());
		try {
			this.producer.send(rocketMessage);
		} catch (final Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void close() throws IOException {
		if (null != this.producer) {
			this.producer.shutdown();
		}
	}
}
