package cn.hutool.extra.mq.business;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.mq.Message;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DeadLetterHandlerTest {

    @Test
    public void testDeadLetterMessageCreation() {
        DeadLetterHandler.DeadLetterMessage dlxMessage = new DeadLetterHandler.DeadLetterMessage();
        dlxMessage.setMessageId(IdUtil.fastSimpleUUID());
        dlxMessage.setOrderId("order001");
        dlxMessage.setReason("max_retries_exceeded");
        dlxMessage.setDeadTime(System.currentTimeMillis());
        dlxMessage.setOriginalMessage("{\"orderId\":\"order001\"}");

        assertNotNull(dlxMessage.getMessageId());
        assertEquals("order001", dlxMessage.getOrderId());
        assertEquals("max_retries_exceeded", dlxMessage.getReason());
        assertNotNull(dlxMessage.getDeadTime());
        assertNotNull(dlxMessage.getOriginalMessage());
    }

    @Test
    public void testDeadLetterHandlerOnMessage() {
        DeadLetterHandler handler = new DeadLetterHandler();

        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId("order001");
        orderMessage.setUserId("user001");
        orderMessage.setTotalAmount(new BigDecimal("100.00"));
        orderMessage.setQuantity(1);

        String json = cn.hutool.json.JSONUtil.toJsonStr(orderMessage);

        Message message = new Message();
        message.setMessageId(IdUtil.fastSimpleUUID());
        message.setBody(json.getBytes(StandardCharsets.UTF_8));

        handler.onMessage(message);

        assertEquals(1, handler.getDeadLetterCount());

        DeadLetterHandler.DeadLetterMessage dlxMessage = handler.getDeadLetterMessages().get(0);
        assertEquals("order001", dlxMessage.getOrderId());
        assertNotNull(dlxMessage.getDeadTime());
    }

    @Test
    public void testDeadLetterHandlerWithInvalidMessage() {
        DeadLetterHandler handler = new DeadLetterHandler();

        Message message = new Message();
        message.setMessageId(IdUtil.fastSimpleUUID());
        message.setBody("invalid json".getBytes(StandardCharsets.UTF_8));

        handler.onMessage(message);

        assertEquals(1, handler.getDeadLetterCount());

        DeadLetterHandler.DeadLetterMessage dlxMessage = handler.getDeadLetterMessages().get(0);
        assertEquals("unknown", dlxMessage.getOrderId());
    }

    @Test
    public void testDeadLetterProcessor() {
        DeadLetterHandler.DeadLetterProcessor processor = new DeadLetterHandler.DeadLetterProcessor();

        DeadLetterHandler.DeadLetterMessage message = new DeadLetterHandler.DeadLetterMessage();
        message.setMessageId(IdUtil.fastSimpleUUID());
        message.setOrderId("order001");
        message.setReason("test_reason");

        processor.handleDeadLetter(message);
    }
}
