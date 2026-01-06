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

package cn.hutool.v7.extra.mq.engine.jms;

import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.extra.mq.MQException;
import cn.hutool.v7.extra.mq.Message;
import cn.hutool.v7.extra.mq.Producer;
import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;

import java.io.IOException;

/**
 * JMS消息生产者
 *
 * @author looly
 * @since 7.0.0
 */
public class JmsProducer implements Producer {

	private final Session session;
	private final MessageProducer producer;

	/**
	 * 构造
	 *
	 * @param session  JMS会话
	 * @param producer JMS消息生产者
	 */
	public JmsProducer(final Session session, final MessageProducer producer) {
		this.session = session;
		this.producer = producer;
	}

	@Override
	public void send(final Message message) {
		final BytesMessage bytesMessage;
		try {
			bytesMessage = this.session.createBytesMessage();
			bytesMessage.writeBytes(message.content());
			this.producer.send(bytesMessage);
		} catch (final JMSException e) {
			throw new MQException(e);
		}
	}

	@Override
	public void close() throws IOException {
		IoUtil.closeQuietly(this.producer);
	}
}
