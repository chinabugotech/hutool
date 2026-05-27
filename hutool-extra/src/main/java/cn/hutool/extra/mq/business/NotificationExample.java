package cn.hutool.extra.mq.business;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.mq.MQUtil;
import cn.hutool.extra.mq.QueueConfig;
import cn.hutool.extra.mq.rabbitmq.RabbitMQConfig;
import cn.hutool.json.JSONUtil;

public class NotificationExample {

    public static void main(String[] args) {
        RabbitMQConfig rabbitConfig = new RabbitMQConfig();
        rabbitConfig.setHost("localhost");
        rabbitConfig.setPort(5672);
        rabbitConfig.setUsername("guest");
        rabbitConfig.setPassword("guest");
        rabbitConfig.setVirtualHost("/");

        QueueConfig queueConfig = new QueueConfig("notification.queue");
        queueConfig.setExchangeName("notification.exchange");
        queueConfig.setExchangeType("direct");
        queueConfig.setRoutingKey("notification.key");
        queueConfig.setDurable(true);

        NotificationMessageListener listener = new NotificationMessageListener();

        System.out.println("=== 通知服务示例 ===\n");

        sendNotification("user001", "系统通知", "您的订单已发货，请查收", "system", rabbitConfig, queueConfig);
        sendNotification("user002", "促销活动", "限时折扣，全场8折起", "promotion", rabbitConfig, queueConfig);
        sendNotification("user001", "安全提醒", "您的账号在新设备登录", "security", rabbitConfig, queueConfig);

        System.out.println("\n=== 通知发送完成 ===");
    }

    private static void sendNotification(String userId, String title, String content, String type, 
                                        RabbitMQConfig rabbitConfig, QueueConfig queueConfig) {
        NotificationMessage notification = new NotificationMessage();
        notification.setNotificationId(IdUtil.fastSimpleUUID());
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);

        String jsonBody = JSONUtil.toJsonStr(notification);
        
        System.out.println("发送通知: " + title + " 给用户 " + userId);
        MQUtil.sendMessage(queueConfig.getExchangeName(), queueConfig.getRoutingKey(), jsonBody, rabbitConfig);
    }
}
