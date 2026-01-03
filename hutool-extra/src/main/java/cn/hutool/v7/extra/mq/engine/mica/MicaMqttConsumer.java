/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.extra.mq.engine.mica;

import cn.hutool.v7.extra.mq.Consumer;
import cn.hutool.v7.extra.mq.MQException;
import cn.hutool.v7.extra.mq.Message;
import cn.hutool.v7.extra.mq.MessageHandler;
import org.dromara.mica.mqtt.codec.MqttQoS;
import org.dromara.mica.mqtt.core.client.MqttClient;

import java.io.IOException;

/**
 * mica-mqtt协议消息消费者
 *
 * @author Looly
 * @since 7.0.0
 */
public class MicaMqttConsumer implements Consumer {

	private final MqttClient mqttClient;
	private String topic;
	private MqttQoS mqttQoS;

	/**
	 * 构造
	 *
	 * @param mqttClient MQTT客户端
	 */
	public MicaMqttConsumer(final MqttClient mqttClient) {
		this.mqttClient = mqttClient;
		// 默认使用QOS0
		this.mqttQoS = MqttQoS.QOS0;
	}

	/**
	 * 设置消费的Topic
	 *
	 * @param topic Topic
	 * @return this
	 */
	public MicaMqttConsumer setTopic(final String topic) {
		this.topic = topic;
		return this;
	}

	/**
	 * 设置MQTT消息质量
	 *
	 * @param mqttQoS MQTT消息质量
	 * @return this
	 */
	public MicaMqttConsumer setMqttQoS(final MqttQoS mqttQoS) {
		this.mqttQoS = mqttQoS;
		return this;
	}

	@Override
	public void subscribe(final MessageHandler messageHandler) {
		if (null == this.topic) {
			throw new MQException("Topic must be set before subscription");
		}

		// 订阅topic并设置消息处理器
		this.mqttClient.subscribe(this.topic, mqttQoS, (context, topic, message, payload) -> {
			messageHandler.handle(new MicaMqttMessage(topic, payload));
		});
	}

	@Override
	public void close() throws IOException {
		if (null != this.mqttClient) {
			try {
				if (this.topic != null) {
					this.mqttClient.unSubscribe(this.topic);
				}
				this.mqttClient.disconnect();
			} catch (final Exception e) {
				throw new IOException("Failed to close MQTT consumer", e);
			}
		}
	}

	/**
	 * MQTT消息包装类
	 *
	 * @author Looly
	 * @since 7.0.0
	 */
	private record MicaMqttMessage(String topic, byte[] content) implements Message {
	}
}
