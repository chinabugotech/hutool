package cn.hutool.extra.mq.business;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NotificationMessageTest {

    @Test
    public void testNotificationMessageCreation() {
        NotificationMessage notification = new NotificationMessage();
        notification.setNotificationId("notif001");
        notification.setUserId("user001");
        notification.setTitle("测试标题");
        notification.setContent("测试内容");
        notification.setType("test");

        assertEquals("notif001", notification.getNotificationId());
        assertEquals("user001", notification.getUserId());
        assertEquals("测试标题", notification.getTitle());
        assertEquals("测试内容", notification.getContent());
        assertEquals("test", notification.getType());
        assertFalse(notification.isRead());
        assertNotNull(notification.getCreateTime());
    }

    @Test
    public void testNotificationMessageConstructor() {
        NotificationMessage notification = new NotificationMessage(
            "notif002", 
            "user002", 
            "构造函数标题", 
            "构造函数内容", 
            "system"
        );

        assertEquals("notif002", notification.getNotificationId());
        assertEquals("user002", notification.getUserId());
        assertEquals("构造函数标题", notification.getTitle());
        assertEquals("构造函数内容", notification.getContent());
        assertEquals("system", notification.getType());
    }

    @Test
    public void testNotificationReadStatus() {
        NotificationMessage notification = new NotificationMessage();
        assertFalse(notification.isRead());
        
        notification.setRead(true);
        assertTrue(notification.isRead());
        
        notification.setRead(false);
        assertFalse(notification.isRead());
    }

    @Test
    public void testNotificationMessageToString() {
        NotificationMessage notification = new NotificationMessage(
            "notif003", 
            "user003", 
            "测试", 
            "内容", 
            "system"
        );

        String str = notification.toString();
        assertTrue(str.contains("notif003"));
        assertTrue(str.contains("user003"));
        assertTrue(str.contains("测试"));
    }
}
