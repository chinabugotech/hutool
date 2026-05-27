package cn.hutool.extra.mq.business;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mq.Message;
import cn.hutool.extra.mq.MessageListener;
import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class OrderMessageListener implements MessageListener {

    private final ConcurrentHashMap<String, OrderMessage> orderCache = new ConcurrentHashMap<>();
    private final OrderProcessor orderProcessor;

    public OrderMessageListener() {
        this.orderProcessor = new OrderProcessor();
    }

    public OrderMessageListener(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    @Override
    public void onMessage(Message message) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);

        if (StrUtil.isBlank(body)) {
            throw new IllegalArgumentException("Message body is empty");
        }

        OrderMessage orderMessage = JSONUtil.toBean(body, OrderMessage.class);

        if (orderMessage == null || StrUtil.isBlank(orderMessage.getOrderId())) {
            throw new IllegalArgumentException("Invalid order message");
        }

        orderCache.put(orderMessage.getOrderId(), orderMessage);

        orderMessage.setStatus(OrderMessage.STATUS_PROCESSING);

        boolean success = orderProcessor.processOrder(orderMessage);

        if (!success) {
            throw new RuntimeException("Order processing failed: " + orderMessage.getOrderId());
        }

        orderMessage.setStatus(OrderMessage.STATUS_SUCCESS);
        orderCache.put(orderMessage.getOrderId(), orderMessage);
    }

    @Override
    public void onSuccess(Message message) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        OrderMessage orderMessage = JSONUtil.toBean(body, OrderMessage.class);

        System.out.println("Order processed successfully: " + orderMessage.getOrderId());
    }

    @Override
    public void onFailure(Message message, Exception exception) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        OrderMessage orderMessage = JSONUtil.toBean(body, OrderMessage.class);

        orderMessage.setStatus(OrderMessage.STATUS_FAILED);
        orderCache.put(orderMessage.getOrderId(), orderMessage);

        System.err.println("Order processing failed: " + orderMessage.getOrderId() + ", error: " + exception.getMessage());
    }

    public OrderMessage getOrderStatus(String orderId) {
        return orderCache.get(orderId);
    }

    public static class OrderProcessor {

        public boolean processOrder(OrderMessage orderMessage) {
            try {
                Thread.sleep(100);

                if (orderMessage.getTotalAmount() == null || orderMessage.getTotalAmount().doubleValue() <= 0) {
                    return false;
                }

                if (orderMessage.getQuantity() == null || orderMessage.getQuantity() <= 0) {
                    return false;
                }

                System.out.println("Processing order: " + orderMessage.getOrderId() +
                        ", amount: " + orderMessage.getTotalAmount());

                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
    }
}
