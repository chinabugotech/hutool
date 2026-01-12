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

import cn.hutool.v7.extra.mq.*;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.IOException;

/**
 * RocketMQ 消费者
 *
 * @author Looly
 * @since 7.0.0
 */
public class RocketMQConsumer implements Consumer {

	private final MQPushConsumer consumer;

	/**
	 * 构造
	 *
	 * @param consumer RocketMQ PushConsumer
	 */
	public RocketMQConsumer(final MQPushConsumer consumer) {
		this.consumer = consumer;
	}

	/**
	 * 设置消费的Topic
	 *
	 * @param topic Topic
	 * @return this
	 */
	public RocketMQConsumer setTopic(final String topic) {
		try {
			this.consumer.subscribe(topic, "*");
		} catch (final MQClientException e) {
			throw new MQException(e);
		}
		return this;
	}

	@Override
	public void subscribe(final MessageHandler messageHandler) {
		this.consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
			for (final MessageExt msg : msgs) {
				messageHandler.handle(new SimpleMessage(msg.getTopic(), msg.getBody()));
			}
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		});
	}

	@Override
	public void close() throws IOException {
		if (null != this.consumer) {
			this.consumer.shutdown();
		}
	}
}
