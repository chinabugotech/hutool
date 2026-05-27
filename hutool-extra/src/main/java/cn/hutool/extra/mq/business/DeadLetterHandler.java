package cn.hutool.extra.mq.business;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mq.Message;
import cn.hutool.extra.mq.MessageListener;
import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DeadLetterHandler implements MessageListener {

    private final List<DeadLetterMessage> deadLetterMessages = new CopyOnWriteArrayList<>();
    private final DeadLetterProcessor processor;

    public DeadLetterHandler() {
        this.processor = new DeadLetterProcessor();
    }

    public DeadLetterHandler(DeadLetterProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void onMessage(Message message) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);

        DeadLetterMessage dlxMessage = new DeadLetterMessage();
        dlxMessage.setOriginalMessage(body);
        dlxMessage.setMessageId(message.getMessageId());
        dlxMessage.setDeadTime(System.currentTimeMillis());
        dlxMessage.setReason(extractFailureReason(message));

        if (StrUtil.isNotBlank(body)) {
            try {
                OrderMessage orderMessage = JSONUtil.toBean(body, OrderMessage.class);
                dlxMessage.setOrderId(orderMessage.getOrderId());
            } catch (Exception e) {
                dlxMessage.setOrderId("unknown");
            }
        }

        deadLetterMessages.add(dlxMessage);

        processor.handleDeadLetter(dlxMessage);
    }

    @Override
    public void onSuccess(Message message) {
        System.out.println("Dead letter message handled successfully: " + message.getMessageId());
    }

    @Override
    public void onFailure(Message message, Exception exception) {
        System.err.println("Failed to handle dead letter message: " + message.getMessageId());
    }

    private String extractFailureReason(Message message) {
        if (message.getHeaders() != null) {
            Object reason = message.getHeaders().get("x-death-reason");
            if (reason != null) {
                return reason.toString();
            }
        }
        return "max_retries_exceeded";
    }

    public List<DeadLetterMessage> getDeadLetterMessages() {
        return new ArrayList<>(deadLetterMessages);
    }

    public int getDeadLetterCount() {
        return deadLetterMessages.size();
    }

    public static class DeadLetterMessage {
        private String messageId;
        private String orderId;
        private String originalMessage;
        private Long deadTime;
        private String reason;

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOriginalMessage() {
            return originalMessage;
        }

        public void setOriginalMessage(String originalMessage) {
            this.originalMessage = originalMessage;
        }

        public Long getDeadTime() {
            return deadTime;
        }

        public void setDeadTime(Long deadTime) {
            this.deadTime = deadTime;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        @Override
        public String toString() {
            return "DeadLetterMessage{" +
                    "messageId='" + messageId + '\'' +
                    ", orderId='" + orderId + '\'' +
                    ", deadTime=" + deadTime +
                    ", reason='" + reason + '\'' +
                    '}';
        }
    }

    public static class DeadLetterProcessor {

        public void handleDeadLetter(DeadLetterMessage message) {
            System.out.println("Handling dead letter message: " + message.getMessageId() +
                    ", orderId: " + message.getOrderId() +
                    ", reason: " + message.getReason());

            saveToDatabase(message);
            sendAlert(message);
        }

        private void saveToDatabase(DeadLetterMessage message) {
            System.out.println("Saving dead letter message to database: " + message.getMessageId());
        }

        private void sendAlert(DeadLetterMessage message) {
            System.out.println("Sending alert for dead letter message: " + message.getMessageId());
        }
    }
}
