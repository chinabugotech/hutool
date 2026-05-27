package cn.hutool.extra.mq;

import cn.hutool.extra.mq.rabbitmq.RabbitMQConfig;
import cn.hutool.extra.mq.rabbitmq.RabbitMQConsumer;
import cn.hutool.extra.mq.rabbitmq.RabbitMQProducer;

public class MQUtil {

    private static volatile RabbitMQProducer producer;
    private static volatile RabbitMQConsumer consumer;
    private static final Object producerLock = new Object();
    private static final Object consumerLock = new Object();

    public static RabbitMQProducer getProducer(RabbitMQConfig config) {
        if (producer == null) {
            synchronized (producerLock) {
                if (producer == null) {
                    producer = new RabbitMQProducer(config);
                }
            }
        }
        return producer;
    }

    public static RabbitMQConsumer getConsumer(RabbitMQConfig config) {
        if (consumer == null) {
            synchronized (consumerLock) {
                if (consumer == null) {
                    consumer = new RabbitMQConsumer(config);
                }
            }
        }
        return consumer;
    }

    public static void sendMessage(Message message, RabbitMQConfig config) {
        getProducer(config).send(message);
    }

    public static void sendMessage(String exchange, String routingKey, Object payload, RabbitMQConfig config) {
        getProducer(config).send(exchange, routingKey, payload);
    }

    public static void startConsumer(QueueConfig queueConfig, MessageListener listener, RabbitMQConfig config) {
        getConsumer(config).startConsumer(queueConfig, listener);
    }

    public static void close() {
        if (producer != null) {
            producer.close();
            producer = null;
        }
        if (consumer != null) {
            consumer.close();
            consumer = null;
        }
    }
}
