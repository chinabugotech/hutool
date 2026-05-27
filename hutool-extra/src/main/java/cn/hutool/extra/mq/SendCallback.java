package cn.hutool.extra.mq;

public interface SendCallback {

    void onSuccess();

    void onFailure(Throwable throwable);
}
