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

import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.extra.mq.Consumer;
import cn.hutool.v7.extra.mq.MQException;
import cn.hutool.v7.extra.mq.MessageHandler;
import cn.hutool.v7.extra.mq.SimpleMessage;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Map;

/**
 * RabbitMQ消费者
 *
 * @author Looly
 * @since 6.0.0
 */
public class RabbitMQConsumer implements Consumer {

	private final Channel channel;
	private String topic;

	/**
	 * 构造
	 *
	 * @param channel Channel
	 */
	public RabbitMQConsumer(final Channel channel) {
		this.channel = channel;
	}

	/**
	 * 设置队列（主题）
	 *
	 * @param topic 队列名
	 * @return this
	 */
	public RabbitMQConsumer setTopic(final String topic) {
		this.topic = topic;
		return this;
	}

	@Override
	public void subscribe(final MessageHandler messageHandler) {
		queueDeclare(false, false, false, null);

		try {
			this.channel.basicConsume(this.topic, true,
				(consumerTag, delivery) -> messageHandler.handle(new SimpleMessage(consumerTag, delivery.getBody())),
				consumerTag -> {});
		} catch (final IOException e) {
			throw new MQException(e);
		}
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(this.channel);
	}

	/**
	 * 声明队列
	 *
	 * @param durable    是否持久化
	 * @param exclusive  是否排他
	 * @param autoDelete 是否自动删除
	 * @param arguments  其他参数
	 */
	@SuppressWarnings("SameParameterValue")
	private void queueDeclare(final boolean durable, final boolean exclusive, final boolean autoDelete,
							  final Map<String, Object> arguments) {
		try {
			this.channel.queueDeclare(this.topic, durable, exclusive, autoDelete, arguments);
		} catch (final IOException e) {
			throw new MQException(e);
		}
	}
}
