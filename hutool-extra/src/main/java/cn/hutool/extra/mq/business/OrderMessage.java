package cn.hutool.extra.mq.business;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String userId;
    private String productId;
    private Integer quantity;
    private BigDecimal totalAmount;
    private Long createTime;
    private Integer status;

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_PROCESSING = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_FAILED = 3;

    public OrderMessage() {
        this.createTime = System.currentTimeMillis();
        this.status = STATUS_PENDING;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderMessage{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }
}
