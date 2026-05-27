package cn.hutool.extra.mq;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class QueueConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private String queueName;
    private boolean durable = true;
    private boolean exclusive = false;
    private boolean autoDelete = false;
    private Map<String, Object> arguments;

    private String exchangeName;
    private String exchangeType = "direct";
    private String routingKey;

    private String deadLetterExchange;
    private String deadLetterRoutingKey;
    private Long messageTtl;
    private Long maxLength;

    public QueueConfig() {
        this.arguments = new HashMap<>();
    }

    public QueueConfig(String queueName) {
        this();
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    public void addArgument(String key, Object value) {
        this.arguments.put(key, value);
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getDeadLetterExchange() {
        return deadLetterExchange;
    }

    public void setDeadLetterExchange(String deadLetterExchange) {
        this.deadLetterExchange = deadLetterExchange;
    }

    public String getDeadLetterRoutingKey() {
        return deadLetterRoutingKey;
    }

    public void setDeadLetterRoutingKey(String deadLetterRoutingKey) {
        this.deadLetterRoutingKey = deadLetterRoutingKey;
    }

    public Long getMessageTtl() {
        return messageTtl;
    }

    public void setMessageTtl(Long messageTtl) {
        this.messageTtl = messageTtl;
    }

    public Long getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Long maxLength) {
        this.maxLength = maxLength;
    }

    public void enableDeadLetter(String dlxExchange, String dlxRoutingKey) {
        this.deadLetterExchange = dlxExchange;
        this.deadLetterRoutingKey = dlxRoutingKey;
        this.arguments.put("x-dead-letter-exchange", dlxExchange);
        this.arguments.put("x-dead-letter-routing-key", dlxRoutingKey);
    }

    public void setMessageTtl(long ttl) {
        this.messageTtl = ttl;
        this.arguments.put("x-message-ttl", ttl);
    }

    public void setMaxLength(long maxLength) {
        this.maxLength = maxLength;
        this.arguments.put("x-max-length", maxLength);
    }
}
