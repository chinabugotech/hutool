package cn.hutool.extra.mq;

public interface MessageProducer {

    void send(Message message);

    void send(String exchange, String routingKey, Object payload);

    void sendWithConfirm(Message message, long timeout);

    void sendAsync(Message message, SendCallback callback);
}
