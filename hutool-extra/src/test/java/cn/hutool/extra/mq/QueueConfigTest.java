package cn.hutool.extra.mq;

import org.junit.Test;

import static org.junit.Assert.*;

public class QueueConfigTest {

    @Test
    public void testQueueConfigDefaultValues() {
        QueueConfig config = new QueueConfig();
        config.setQueueName("test.queue");

        assertEquals("test.queue", config.getQueueName());
        assertTrue(config.isDurable());
        assertFalse(config.isExclusive());
        assertFalse(config.isAutoDelete());
        assertEquals("direct", config.getExchangeType());
        assertNotNull(config.getArguments());
    }

    @Test
    public void testQueueConfigWithExchange() {
        QueueConfig config = new QueueConfig("order.queue");
        config.setExchangeName("order.exchange");
        config.setExchangeType("topic");
        config.setRoutingKey("order.*");

        assertEquals("order.queue", config.getQueueName());
        assertEquals("order.exchange", config.getExchangeName());
        assertEquals("topic", config.getExchangeType());
        assertEquals("order.*", config.getRoutingKey());
    }

    @Test
    public void testEnableDeadLetter() {
        QueueConfig config = new QueueConfig("order.queue");
        config.enableDeadLetter("order.dlx.exchange", "order.dlx.routing");

        assertEquals("order.dlx.exchange", config.getDeadLetterExchange());
        assertEquals("order.dlx.routing", config.getDeadLetterRoutingKey());
        assertEquals("order.dlx.exchange", config.getArguments().get("x-dead-letter-exchange"));
        assertEquals("order.dlx.routing", config.getArguments().get("x-dead-letter-routing-key"));
    }

    @Test
    public void testSetMessageTtl() {
        QueueConfig config = new QueueConfig("order.queue");
        config.setMessageTtl(60000L);

        assertEquals(Long.valueOf(60000L), config.getMessageTtl());
        assertEquals(60000L, config.getArguments().get("x-message-ttl"));
    }

    @Test
    public void testSetMaxLength() {
        QueueConfig config = new QueueConfig("order.queue");
        config.setMaxLength(10000L);

        assertEquals(Long.valueOf(10000L), config.getMaxLength());
        assertEquals(10000L, config.getArguments().get("x-max-length"));
    }

    @Test
    public void testAddArgument() {
        QueueConfig config = new QueueConfig("order.queue");
        config.addArgument("x-custom-arg", "custom-value");

        assertEquals("custom-value", config.getArguments().get("x-custom-arg"));
    }
}
