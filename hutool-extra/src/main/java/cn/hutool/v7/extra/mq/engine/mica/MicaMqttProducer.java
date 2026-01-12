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

package cn.hutool.v7.extra.mq.engine.mica;

import cn.hutool.v7.extra.mq.MQException;
import cn.hutool.v7.extra.mq.Message;
import cn.hutool.v7.extra.mq.Producer;
import org.dromara.mica.mqtt.core.client.MqttClient;

import java.io.IOException;

/**
 * MQTT协议消息生产者
 *
 * @author Looly
 * @since 7.0.0
 */
public class MicaMqttProducer implements Producer {

	private final MqttClient mqttClient;

	/**
	 * 构造
	 *
	 * @param mqttClient MQTT客户端
	 */
	public MicaMqttProducer(final MqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}

	@Override
	public void send(final Message message) {
		try {
			// 使用MQTT协议发送消息，QoS级别设置为1（至少一次），retained设置为false
			this.mqttClient.publish(message.topic(), message.content(), false);
		} catch (final Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void close() throws IOException {
		if (null != this.mqttClient) {
			try {
				this.mqttClient.disconnect();
			} catch (final Exception e) {
				throw new IOException("Failed to close MQTT client", e);
			}
		}
	}
}
