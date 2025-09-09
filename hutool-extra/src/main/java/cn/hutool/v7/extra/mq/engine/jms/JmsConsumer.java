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

package cn.hutool.v7.extra.mq.engine.jms;

import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.util.ByteUtil;
import cn.hutool.v7.extra.mq.Consumer;
import cn.hutool.v7.extra.mq.MQException;
import cn.hutool.v7.extra.mq.Message;
import cn.hutool.v7.extra.mq.MessageHandler;
import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.TextMessage;

import java.io.IOException;

/**
 * JMS消息消费者
 *
 * @author Looly
 * @since 7.0.0
 */
public class JmsConsumer implements Consumer {

	private String consumerGroup;
	private final MessageConsumer consumer;

	/**
	 * 构造
	 *
	 * @param consumerGroup 消费者组
	 * @param consumer      消费者
	 */
	public JmsConsumer(final String consumerGroup, final MessageConsumer consumer) {
		this.consumerGroup = consumerGroup;
		this.consumer = consumer;
	}

	/**
	 * 设置消费者组
	 *
	 * @param consumerGroup 消费者组
	 * @return this
	 */
	public JmsConsumer setConsumerGroup(final String consumerGroup) {
		this.consumerGroup = consumerGroup;
		return this;
	}

	@Override
	public void subscribe(final MessageHandler messageHandler) {
		try {
			this.consumer.setMessageListener(message -> {
				messageHandler.handle(new Message() {
					@Override
					public String topic() {
						return consumerGroup;
					}

					@Override
					public byte[] content() {
						try {
							if (message instanceof TextMessage) {
								// TODO 考虑编码
								return ByteUtil.toUtf8Bytes(((TextMessage) message).getText());
							} else if (message instanceof BytesMessage) {
								return new byte[(int) ((BytesMessage) message).getBodyLength()];
							} else {
								throw new IllegalArgumentException("Unsupported message type: " + message.getClass().getName());
							}
						} catch (final JMSException e) {
							throw new MQException(e);
						}
					}
				});
			});
		} catch (final JMSException e) {
			throw new MQException(e);
		}
	}

	@Override
	public void close() throws IOException {
		IoUtil.closeQuietly(this.consumer);
	}
}
