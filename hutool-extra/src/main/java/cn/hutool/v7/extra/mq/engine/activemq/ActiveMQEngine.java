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
