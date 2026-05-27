package cn.hutool.extra.mq.business;

import cn.hutool.core.util.IdUtil;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class OrderMessageTest {

    @Test
    public void testOrderMessageCreation() {
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(IdUtil.fastSimpleUUID());
        orderMessage.setUserId("user001");
        orderMessage.setProductId("prod001");
        orderMessage.setQuantity(2);
        orderMessage.setTotalAmount(new BigDecimal("199.99"));

        assertNotNull(orderMessage.getOrderId());
        assertEquals("user001", orderMessage.getUserId());
        assertEquals("prod001", orderMessage.getProductId());
        assertEquals(Integer.valueOf(2), orderMessage.getQuantity());
        assertEquals(new BigDecimal("199.99"), orderMessage.getTotalAmount());
        assertNotNull(orderMessage.getCreateTime());
        assertEquals(Integer.valueOf(OrderMessage.STATUS_PENDING), orderMessage.getStatus());
    }

    @Test
    public void testOrderMessageStatusTransition() {
        OrderMessage orderMessage = new OrderMessage();

        assertEquals(Integer.valueOf(OrderMessage.STATUS_PENDING), orderMessage.getStatus());

        orderMessage.setStatus(OrderMessage.STATUS_PROCESSING);
        assertEquals(Integer.valueOf(OrderMessage.STATUS_PROCESSING), orderMessage.getStatus());

        orderMessage.setStatus(OrderMessage.STATUS_SUCCESS);
        assertEquals(Integer.valueOf(OrderMessage.STATUS_SUCCESS), orderMessage.getStatus());

        orderMessage.setStatus(OrderMessage.STATUS_FAILED);
        assertEquals(Integer.valueOf(OrderMessage.STATUS_FAILED), orderMessage.getStatus());
    }

    @Test
    public void testOrderProcessorValidOrder() {
        OrderMessageListener.OrderProcessor processor = new OrderMessageListener.OrderProcessor();

        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(IdUtil.fastSimpleUUID());
        orderMessage.setTotalAmount(new BigDecimal("100.00"));
        orderMessage.setQuantity(1);

        boolean result = processor.processOrder(orderMessage);
        assertTrue(result);
    }

    @Test
    public void testOrderProcessorInvalidAmount() {
        OrderMessageListener.OrderProcessor processor = new OrderMessageListener.OrderProcessor();

        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(IdUtil.fastSimpleUUID());
        orderMessage.setTotalAmount(new BigDecimal("0"));
        orderMessage.setQuantity(1);

        boolean result = processor.processOrder(orderMessage);
        assertFalse(result);
    }

    @Test
    public void testOrderProcessorInvalidQuantity() {
        OrderMessageListener.OrderProcessor processor = new OrderMessageListener.OrderProcessor();

        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(IdUtil.fastSimpleUUID());
        orderMessage.setTotalAmount(new BigDecimal("100.00"));
        orderMessage.setQuantity(0);

        boolean result = processor.processOrder(orderMessage);
        assertFalse(result);
    }

    @Test
    public void testOrderProcessorNullAmount() {
        OrderMessageListener.OrderProcessor processor = new OrderMessageListener.OrderProcessor();

        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(IdUtil.fastSimpleUUID());
        orderMessage.setTotalAmount(null);
        orderMessage.setQuantity(1);

        boolean result = processor.processOrder(orderMessage);
        assertFalse(result);
    }
}
