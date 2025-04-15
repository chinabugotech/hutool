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

package cn.hutool.v7.extra.mq.engine.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.extra.mq.Consumer;
import cn.hutool.v7.extra.mq.Message;
import cn.hutool.v7.extra.mq.MessageHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Kafka消费端
 *
 * @author Looly
 * @since 6.0.0
 */
public class KafkaConsumer implements Consumer {

	private final org.apache.kafka.clients.consumer.Consumer<String, byte[]> consumer;

	/**
	 * 构造
	 *
	 * @param properties 配置
	 */
	public KafkaConsumer(final Properties properties) {
		this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(properties);
	}

	/**
	 * 构造
	 *
	 * @param consumer {@link org.apache.kafka.clients.consumer.Consumer}
	 */
	public KafkaConsumer(final org.apache.kafka.clients.consumer.Consumer<String, byte[]> consumer) {
		this.consumer = consumer;
	}

	/**
	 * 设置消费的topic
	 *
	 * @param topics topic
	 * @return this
	 */
	public KafkaConsumer setTopics(final String... topics) {
		this.consumer.subscribe(ListUtil.of(topics));
		return this;
	}

	/**
	 * 设置消费的topic正则
	 *
	 * @param topicPattern topic{@link Pattern}
	 * @return this
	 */
	public KafkaConsumer setTopicPattern(final Pattern topicPattern){
		this.consumer.subscribe(topicPattern);
		return this;
	}

	@Override
	public void subscribe(final MessageHandler messageHandler) {
		for (final ConsumerRecord<String, byte[]> record : this.consumer.poll(Duration.ofMillis(3000))) {
			messageHandler.handle(new ConsumerRecordMessage(record));
		}
	}

	@Override
	public void close() throws IOException {
		IoUtil.nullSafeClose(this.consumer);
	}

	/**
	 * 消费者记录包装为消息
	 *
	 * @author looly
	 */
	private static class ConsumerRecordMessage implements Message {

		private final ConsumerRecord<String, byte[]> record;

		/**
		 * 构造
		 *
		 * @param record {@link ConsumerRecord}
		 */
		private ConsumerRecordMessage(final ConsumerRecord<String, byte[]> record) {
			this.record = record;
		}

		@Override
		public String topic() {
			return record.topic();
		}

		@Override
		public byte[] content() {
			return record.value();
		}
	}
}
