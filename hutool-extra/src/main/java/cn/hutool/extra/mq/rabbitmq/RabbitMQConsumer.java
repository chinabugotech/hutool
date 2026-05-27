package cn.hutool.extra.mq.rabbitmq;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mq.*;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.*;

public class RabbitMQConsumer {

    private final ConnectionFactory connectionFactory;
    private final RabbitMQConfig config;
    private Connection connection;
    private final ExecutorService executorService;
    private final ConcurrentHashMap<String, Channel> consumerChannels = new ConcurrentHashMap<>();

    public RabbitMQConsumer(RabbitMQConfig config) {
        this.config = config;
        this.connectionFactory = createConnectionFactory(config);
        this.executorService = Executors.newCachedThreadPool();
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
            this.connection = connectionFactory.newConnection(executorService);
        } catch (Exception e) {
            throw new MessageQueueException("Failed to create RabbitMQ connection", e);
        }
    }

    public void startConsumer(QueueConfig queueConfig, MessageListener listener) {
        try {
            Channel channel = connection.createChannel();
            channel.basicQos(config.getPrefetchCount());

            declareQueue(channel, queueConfig);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Message message = convertToMessage(delivery);
                try {
                    listener.onMessage(message);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    listener.onSuccess(message);
                } catch (Exception e) {
                    try {
                        channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
                        listener.onFailure(message, e);
                    } catch (IOException ioException) {
                        throw new MessageQueueException("Failed to nack message", ioException);
                    }
                }
            };

            CancelCallback cancelCallback = consumerTag -> {
            };

            for (int i = 0; i < config.getConcurrentConsumers(); i++) {
                String consumerTag = channel.basicConsume(queueConfig.getQueueName(), false, deliverCallback, cancelCallback);
                consumerChannels.put(consumerTag, channel);
            }

        } catch (IOException e) {
            throw new MessageQueueException("Failed to start consumer", e);
        }
    }

    public void startConsumerWithRetry(QueueConfig queueConfig, MessageListener listener, int maxRetries) {
        try {
            Channel channel = connection.createChannel();
            channel.basicQos(config.getPrefetchCount());

            declareQueue(channel, queueConfig);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Message message = convertToMessage(delivery);
                int retryCount = getRetryCount(delivery);

                try {
                    listener.onMessage(message);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    listener.onSuccess(message);
                } catch (Exception e) {
                    if (retryCount < maxRetries) {
                        try {
                            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                        } catch (IOException ioException) {
                            throw new MessageQueueException("Failed to requeue message", ioException);
                        }
                    } else {
                        try {
                            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
                            listener.onFailure(message, e);
                        } catch (IOException ioException) {
                            throw new MessageQueueException("Failed to reject message", ioException);
                        }
                    }
                }
            };

            CancelCallback cancelCallback = consumerTag -> {
            };

            for (int i = 0; i < config.getConcurrentConsumers(); i++) {
                String consumerTag = channel.basicConsume(queueConfig.getQueueName(), false, deliverCallback, cancelCallback);
                consumerChannels.put(consumerTag, channel);
            }

        } catch (IOException e) {
            throw new MessageQueueException("Failed to start consumer with retry", e);
        }
    }

    private void declareQueue(Channel channel, QueueConfig queueConfig) throws IOException {
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
    }

    private Message convertToMessage(Delivery delivery) {
        Message message = new Message();
        message.setMessageId(delivery.getProperties().getMessageId());
        message.setBody(delivery.getBody());
        message.setHeaders(delivery.getProperties().getHeaders());
        message.setContentType(delivery.getProperties().getContentType());

        if (delivery.getProperties().getTimestamp() != null) {
            message.setTimestamp(delivery.getProperties().getTimestamp().getTime());
        }

        return message;
    }

    private int getRetryCount(Delivery delivery) {
        Map<String, Object> headers = delivery.getProperties().getHeaders();
        if (headers != null && headers.containsKey("x-retry-count")) {
            return (int) headers.get("x-retry-count");
        }
        return 0;
    }

    public void stopConsumer(String consumerTag) {
        Channel channel = consumerChannels.get(consumerTag);
        if (channel != null) {
            try {
                channel.basicCancel(consumerTag);
                consumerChannels.remove(consumerTag);
            } catch (IOException e) {
                throw new MessageQueueException("Failed to stop consumer", e);
            }
        }
    }

    public void close() {
        try {
            for (Channel channel : consumerChannels.values()) {
                if (channel.isOpen()) {
                    channel.close();
                }
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
            executorService.shutdown();
        } catch (Exception e) {
            throw new MessageQueueException("Failed to close consumer", e);
        }
    }
}
