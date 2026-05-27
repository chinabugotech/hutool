package cn.hutool.extra.mq;

import cn.hutool.core.util.IdUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void testMessageCreation() {
        Message message = new Message();
        message.setMessageId(IdUtil.fastSimpleUUID());
        message.setExchange("order.exchange");
        message.setRoutingKey("order.created");
        message.setBody("test message".getBytes());

        assertNotNull(message.getMessageId());
        assertEquals("order.exchange", message.getExchange());
        assertEquals("order.created", message.getRoutingKey());
        assertNotNull(message.getBody());
        assertNotNull(message.getHeaders());
        assertNotNull(message.getTimestamp());
        assertEquals(2, message.getDeliveryMode());
        assertEquals("application/json", message.getContentType());
    }

    @Test
    public void testMessageHeaders() {
        Message message = new Message();
        message.addHeader("x-custom-header", "custom-value");
        message.addHeader("x-retry-count", 3);

        assertEquals("custom-value", message.getHeaders().get("x-custom-header"));
        assertEquals(3, message.getHeaders().get("x-retry-count"));
    }

    @Test
    public void testMessageConstructor() {
        String messageId = IdUtil.fastSimpleUUID();
        byte[] body = "test body".getBytes();

        Message message = new Message(messageId, "test.exchange", "test.routing", body);

        assertEquals(messageId, message.getMessageId());
        assertEquals("test.exchange", message.getExchange());
        assertEquals("test.routing", message.getRoutingKey());
        assertArrayEquals(body, message.getBody());
    }

    @Test
    public void testMessageToString() {
        Message message = new Message();
        message.setMessageId("test-id");
        message.setExchange("test.exchange");
        message.setRoutingKey("test.routing");

        String str = message.toString();
        assertTrue(str.contains("test-id"));
        assertTrue(str.contains("test.exchange"));
        assertTrue(str.contains("test.routing"));
    }
}
