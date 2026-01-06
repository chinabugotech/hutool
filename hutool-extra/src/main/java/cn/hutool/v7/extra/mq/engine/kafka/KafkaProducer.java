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

import org.apache.kafka.clients.producer.ProducerRecord;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.extra.mq.Message;
import cn.hutool.v7.extra.mq.Producer;

import java.io.IOException;
import java.util.Properties;

/**
 * Kafka 生产者
 *
 * @author Looly
 * @since 6.0.0
 */
public class KafkaProducer implements Producer {

	private final org.apache.kafka.clients.producer.Producer<String, byte[]> producer;

	/**
	 * 构造
	 *
	 * @param properties 配置
	 */
	public KafkaProducer(final Properties properties) {
		this(new org.apache.kafka.clients.producer.KafkaProducer<>(properties));
	}

	/**
	 * 构造
	 *
	 * @param producer Kafka Producer
	 */
	public KafkaProducer(final org.apache.kafka.clients.producer.Producer<String, byte[]> producer) {
		this.producer = producer;
	}

	@Override
	public void send(final Message message) {
		this.producer.send(new ProducerRecord<>(message.topic(), message.content()));
	}

	@Override
	public void close() throws IOException {
		IoUtil.nullSafeClose(this.producer);
	}
}
