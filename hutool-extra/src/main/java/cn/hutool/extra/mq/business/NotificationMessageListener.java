package cn.hutool.extra.mq.business;

import cn.hutool.extra.mq.Message;
import cn.hutool.extra.mq.MessageListener;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationMessageListener implements MessageListener {

    private final List<NotificationMessage> notificationStore = new CopyOnWriteArrayList<>();
    private final NotificationCallback callback;

    public NotificationMessageListener() {
        this.callback = new DefaultNotificationCallback();
    }

    public NotificationMessageListener(NotificationCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onMessage(Message message) {
        String body = new String(message.getBody());
        
        try {
            NotificationMessage notification = JSONUtil.toBean(body, NotificationMessage.class);
            notificationStore.add(notification);
            callback.onNotificationReceived(notification);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    @Override
    public void onSuccess(Message message) {
        callback.onNotificationProcessed();
    }

    @Override
    public void onFailure(Message message, Exception exception) {
        callback.onError(exception);
    }

    public List<NotificationMessage> getAllNotifications() {
        return new ArrayList<>(notificationStore);
    }

    public List<NotificationMessage> getUserNotifications(String userId) {
        List<NotificationMessage> result = new ArrayList<>();
        for (NotificationMessage notification : notificationStore) {
            if (userId.equals(notification.getUserId())) {
                result.add(notification);
            }
        }
        return result;
    }

    public interface NotificationCallback {
        void onNotificationReceived(NotificationMessage notification);
        void onNotificationProcessed();
        void onError(Exception e);
    }

    public static class DefaultNotificationCallback implements NotificationCallback {
        @Override
        public void onNotificationReceived(NotificationMessage notification) {
            System.out.println("收到通知: " + notification.getTitle() + ", 用户ID: " + notification.getUserId());
        }

        @Override
        public void onNotificationProcessed() {
            System.out.println("通知处理完成");
        }

        @Override
        public void onError(Exception e) {
            System.err.println("通知处理失败: " + e.getMessage());
        }
    }
}
