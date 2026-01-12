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

package cn.hutool.v7.extra.mq.engine;

import cn.hutool.v7.extra.mq.*;
import cn.hutool.v7.extra.mq.engine.mica.MicaMqttEngine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * MicaMqttEngine 测试
 *
 * @author Looly
 * @since 7.0.0
 */
class MicaMqttEngineTest {

	@Test
	void testMicaMqttEngineCreation() {
		// 创建MQTT引擎
		final MicaMqttEngine engine = new MicaMqttEngine();

		// 初始化配置
		final MQConfig config = MQConfig.of("tcp://localhost:1883");
		final MQEngine initializedEngine = engine.init(config);

		// 验证引擎初始化成功
		assertNotNull(initializedEngine);
		assertNotNull(initializedEngine.getProducer());
		assertNotNull(initializedEngine.getConsumer());
	}

	@Test
	void testMicaMqttProducer() {
		final MicaMqttEngine engine = new MicaMqttEngine();
		final MQConfig config = MQConfig.of("tcp://localhost:1883");
		engine.init(config);

		final Producer producer = engine.getProducer();
		assertNotNull(producer);
		// 这里不实际发送消息，因为需要真实的MQTT broker
	}

	@Test
	void testMicaMqttConsumer() {
		final MicaMqttEngine engine = new MicaMqttEngine();
		final MQConfig config = MQConfig.of("tcp://localhost:1883");
		engine.init(config);

		final Consumer consumer = engine.getConsumer();
		assertNotNull(consumer);
		// 这里不实际订阅消息，因为需要真实的MQTT broker
	}
}
