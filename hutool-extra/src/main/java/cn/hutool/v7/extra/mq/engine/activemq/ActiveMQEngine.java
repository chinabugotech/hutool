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

package cn.hutool.v7.extra.mq.engine.activemq;

import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.extra.mq.MQConfig;
import cn.hutool.v7.extra.mq.engine.jms.JmsEngine;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * ActiveMQ引擎
 *
 * @author Looly
 * @since 7.0.0
 */
public class ActiveMQEngine extends JmsEngine {

	/**
	 * 构造
	 */
	public ActiveMQEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(org.apache.activemq.ActiveMQConnectionFactory.class);
	}

	@Override
	protected ConnectionFactory createConnectionFactory(final MQConfig config) {
		return new ActiveMQConnectionFactory(config.getBrokerUrl());
	}
}
