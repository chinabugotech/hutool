package cn.hutool.extra.mq.rabbitmq;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mq.*;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class RabbitMQProducer implements MessageProducer {

    private final ConnectionFactory connectionFactory;
    private final RabbitMQConfig config;
    private Connection connection;
    private final ThreadLocal<Channel> channelHolder = new ThreadLocal<>();
    private final ExecutorService executorService;

    public RabbitMQProducer(RabbitMQConfig config) {
        this.config = config;
        this.connectionFactory = createConnectionFactory(config);
        this.executorService = Executors.newFixedThreadPool(10);
        initConnection();
    }

    private ConnectionFactory createConnectionFactory(RabbitMQConfig config) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getHost());
        factory.setPort(config.getPort());
        factory.setUsername(config.getUsername());
        factory.setPassword(config.getPassword());
        factory.setVirtualHost(config.getVirtualHost());
        factory.setConnectionTimeout(config.getConnectionTimeout());
        factory.setRequestedHeartbeat(config.getHeartbeat());
        return factory;
    }

    private void initConnection() {
        try {
            this.connection = connectionFactory.newConnection();
        } catch (Exception e) {
            throw new MessageQueueException("Failed to create RabbitMQ connection", e);
        }
    }

    private Channel getChannel() {
        Channel channel = channelHolder.get();
        if (channel == null || !channel.isOpen()) {
            try {
                channel = connection.createChannel();
                if (config.isPublisherConfirms()) {
                    channel.confirmSelect();
                }
                channelHolder.set(channel);
            } catch (IOException e) {
                throw new MessageQueueException("Failed to create channel", e);
            }
        }
        return channel;
    }

    @Override
    public void send(Message message) {
        if (StrUtil.isBlank(message.getMessageId())) {
            message.setMessageId(IdUtil.fastSimpleUUID());
        }

        try {
            Channel channel = getChannel();
            AMQP.BasicProperties props = buildProperties(message);

            channel.basicPublish(
                    message.getExchange(),
                    message.getRoutingKey(),
                    config.isPublisherReturns(),
                    props,
                    message.getBody()
            );
        } catch (IOException e) {
            throw new MessageQueueException("Failed to send message", e);
        }
    }

    @Override
    public void send(String exchange, String routingKey, Object payload) {
        String body = payload instanceof String ? (String) payload : cn.hutool.json.JSONUtil.toJsonStr(payload);
        Message message = new Message();
        message.setExchange(exchange);
        message.setRoutingKey(routingKey);
        message.setBody(body.getBytes(StandardCharsets.UTF_8));
        send(message);
    }

    @Override
    public void sendWithConfirm(Message message, long timeout) {
        if (StrUtil.isBlank(message.getMessageId())) {
            message.setMessageId(IdUtil.fastSimpleUUID());
        }

        try {
            Channel channel = getChannel();
            if (!channel.waitForConfirms(timeout)) {
                throw new MessageQueueException("Message send confirmation timeout");
            }

            AMQP.BasicProperties props = buildProperties(message);
            channel.basicPublish(
                    message.getExchange(),
                    message.getRoutingKey(),
                    config.isPublisherReturns(),
                    props,
                    message.getBody()
            );

            boolean confirmed = channel.waitForConfirms(timeout);
            if (!confirmed) {
                throw new MessageQueueException("Message was nack'd by broker");
            }
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new MessageQueueException("Failed to send message with confirm", e);
        }
    }

    @Override
    public void sendAsync(Message message, SendCallback callback) {
        executorService.submit(() -> {
            try {
                send(message);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private AMQP.BasicProperties buildProperties(Message message) {
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.messageId(message.getMessageId());
        builder.timestamp(new java.util.Date(message.getTimestamp()));
        builder.deliveryMode(message.getDeliveryMode());
        builder.contentType(message.getContentType());

        if (message.getHeaders() != null && !message.getHeaders().isEmpty()) {
            builder.headers(message.getHeaders());
        }

        return builder.build();
    }

    public void declareQueue(QueueConfig queueConfig) {
        try {
            Channel channel = getChannel();

            if (StrUtil.isNotBlank(queueConfig.getExchangeName())) {
                channel.exchangeDeclare(
                        queueConfig.getExchangeName(),
                        queueConfig.getExchangeType(),
                        true
                );
            }

            channel.queueDeclare(
                    queueConfig.getQueueName(),
                    queueConfig.isDurable(),
                    queueConfig.isExclusive(),
                    queueConfig.isAutoDelete(),
                    queueConfig.getArguments()
            );

            if (StrUtil.isNotBlank(queueConfig.getExchangeName()) &&
                    StrUtil.isNotBlank(queueConfig.getRoutingKey())) {
                channel.queueBind(
                        queueConfig.getQueueName(),
                        queueConfig.getExchangeName(),
                        queueConfig.getRoutingKey()
                );
            }
        } catch (IOException e) {
            throw new MessageQueueException("Failed to declare queue", e);
        }
    }

    public void close() {
        try {
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
            executorService.shutdown();
        } catch (IOException e) {
            throw new MessageQueueException("Failed to close connection", e);
        }
    }
}
