package cn.hutool.extra.mq;

public interface MessageListener {

    void onMessage(Message message);

    default void onSuccess(Message message) {
    }

    default void onFailure(Message message, Exception exception) {
    }
}
