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

import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.extra.mq.Consumer;
import cn.hutool.v7.extra.mq.MQConfig;
import cn.hutool.v7.extra.mq.Producer;
import cn.hutool.v7.extra.mq.engine.MQEngine;
import cn.hutool.v7.setting.props.Props;
import org.dromara.mica.mqtt.codec.MqttVersion;
import org.dromara.mica.mqtt.core.client.MqttClient;
import org.dromara.mica.mqtt.core.client.MqttClientCreator;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

/**
 * MQTT协议消息队列引擎实现
 *
 * @author Looly
 * @since 7.0.0
 */
public class MicaMqttEngine implements MQEngine, Closeable {

	private MqttClient mqttClient;

	/**
	 * 默认构造
	 */
	public MicaMqttEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(MqttClient.class);
	}

	@Override
	public MicaMqttEngine init(final MQConfig config) {
		final MqttClientCreator creator = MqttClient.create()
			.version(MqttVersion.MQTT_5)
			.ip(config.getBrokerUrl());

		// 其他参数配置
		final Properties properties = config.getProperties();
		if(MapUtil.isNotEmpty(properties)) {
			final Props props = new Props(properties);

			final Integer port = props.getInt("port");
			if (null != port) {
				creator.port(port);
			}

			final String username = props.getStr("username");
			if (null != username) {
				creator.username(username);
			}

			final String password = props.getStr("password");
			if (null != password) {
				creator.password(password);
			}

			final String clientId = props.getStr("clientId");
			if (null != clientId) {
				creator.clientId(clientId);
			}

			final Integer readBufferSize = props.getInt("readBufferSize");
			if (null != readBufferSize) {
				creator.readBufferSize(readBufferSize);
			}

			final Integer maxBytesInMessage = props.getInt("maxBytesInMessage");
			if (null != maxBytesInMessage) {
				creator.maxBytesInMessage(maxBytesInMessage);
			}

			final Integer keepAliveSecs = props.getInt("keepAliveSecs");
			if (null != keepAliveSecs) {
				creator.keepAliveSecs(keepAliveSecs);
			}

			final Integer timeout = props.getInt("timeout");
			if (null != timeout) {
				creator.timeout(timeout);
			}

			final Boolean reconnect = props.getBool("reconnect");
			if (null != reconnect) {
				creator.reconnect(reconnect);
			}

			final Integer reInterval = props.getInt("reInterval");
			if (null != reInterval) {
				creator.reInterval(reInterval);
			}
		}

		this.mqttClient = creator.connect();
		return this;
	}

	@Override
	public Producer getProducer() {
		return new MicaMqttProducer(mqttClient);
	}

	@Override
	public Consumer getConsumer() {
		return new MicaMqttConsumer(mqttClient);
	}

	@Override
	public void close() throws IOException {
		if (null != mqttClient && mqttClient.isConnected()) {
			mqttClient.disconnect();
			mqttClient.stop();
		}
	}
}
