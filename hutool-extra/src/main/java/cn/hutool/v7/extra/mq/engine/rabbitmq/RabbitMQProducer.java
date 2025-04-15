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

package cn.hutool.v7.extra.mq.engine.rabbitmq;

import com.rabbitmq.client.Channel;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.extra.mq.MQException;
import cn.hutool.v7.extra.mq.Message;
import cn.hutool.v7.extra.mq.Producer;

import java.io.IOException;
import java.util.Map;

/**
 * RabbitMQ消息生产者
 *
 * @author Looly
 * @since 6.0.0
 */
public class RabbitMQProducer implements Producer {

	private final Channel channel;
	private String exchange = StrUtil.EMPTY;

	/**
	 * 构造
	 *
	 * @param channel Channel
	 */
	public RabbitMQProducer(final Channel channel) {
		this.channel = channel;
	}

	/**
	 * 设置交换器，默认为{@link StrUtil#EMPTY}
	 *
	 * @param exchange 交换器
	 * @return this
	 */
	public RabbitMQProducer setExchange(final String exchange) {
		this.exchange = exchange;
		return this;
	}

	/**
	 * 声明队列
	 *
	 * @param queue      队列名
	 * @param durable    是否持久化
	 * @param exclusive  是否排他
	 * @param autoDelete 是否自动删除
	 * @param arguments  其他参数
	 * @return this
	 */
	public RabbitMQProducer queueDeclare(final String queue, final boolean durable, final boolean exclusive, final boolean autoDelete,
										 final Map<String, Object> arguments) {
		try {
			this.channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
		} catch (final IOException e) {
			throw new MQException(e);
		}
		return this;
	}

	@Override
	public void send(final Message message) {
		try {
			this.channel.basicPublish(exchange, message.topic(), null, message.content());
		} catch (final IOException e) {
			throw new MQException(e);
		}
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(this.channel);
	}
}
