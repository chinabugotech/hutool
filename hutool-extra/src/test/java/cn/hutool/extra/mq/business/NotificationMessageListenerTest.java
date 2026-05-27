package cn.hutool.extra.mq.business;

import cn.hutool.extra.mq.Message;
import cn.hutool.json.JSONUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

public class NotificationMessageListenerTest {

    @Test
    public void testNotificationStore() {
        NotificationMessageListener listener = new NotificationMessageListener();
        
        NotificationMessage notification1 = new NotificationMessage(
            "notif001", "user001", "标题1", "内容1", "system"
        );
        NotificationMessage notification2 = new NotificationMessage(
            "notif002", "user001", "标题2", "内容2", "promotion"
        );
        NotificationMessage notification3 = new NotificationMessage(
            "notif003", "user002", "标题3", "内容3", "security"
        );

        Message message1 = createMessage(notification1);
        Message message2 = createMessage(notification2);
        Message message3 = createMessage(notification3);

        listener.onMessage(message1);
        listener.onMessage(message2);
        listener.onMessage(message3);

        List<NotificationMessage> allNotifications = listener.getAllNotifications();
        assertEquals(3, allNotifications.size());

        List<NotificationMessage> user1Notifications = listener.getUserNotifications("user001");
        assertEquals(2, user1Notifications.size());

        List<NotificationMessage> user2Notifications = listener.getUserNotifications("user002");
        assertEquals(1, user2Notifications.size());
    }

    @Test
    public void testEmptyUserNotifications() {
        NotificationMessageListener listener = new NotificationMessageListener();
        
        List<NotificationMessage> notifications = listener.getUserNotifications("nonexistent");
        assertTrue(notifications.isEmpty());
    }

    private Message createMessage(NotificationMessage notification) {
        Message message = new Message();
        message.setMessageId(notification.getNotificationId());
        message.setBody(JSONUtil.toJsonStr(notification).getBytes(StandardCharsets.UTF_8));
        return message;
    }
}
