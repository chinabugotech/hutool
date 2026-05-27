package cn.hutool.extra.mq;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String messageId;
    private String exchange;
    private String routingKey;
    private byte[] body;
    private Map<String, Object> headers;
    private long timestamp;
    private int deliveryMode;
    private String contentType;

    public Message() {
        this.headers = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
        this.deliveryMode = 2;
        this.contentType = "application/json";
    }

    public Message(String messageId, String exchange, String routingKey, byte[] body) {
        this();
        this.messageId = messageId;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.body = body;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, Object value) {
        this.headers.put(key, value);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(int deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", timestamp=" + timestamp +
                ", deliveryMode=" + deliveryMode +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
