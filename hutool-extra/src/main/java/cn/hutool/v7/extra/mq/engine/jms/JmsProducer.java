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
