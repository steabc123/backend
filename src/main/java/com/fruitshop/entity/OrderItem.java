package com.fruitshop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单明细实体类
 */
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    private Integer id;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 水果ID
     */
    private Integer fruitId;

    /**
     * 购买数量(斤)
     */
    private BigDecimal quantity;

    /**
     * 购买时单价
     */
    private BigDecimal unitPrice;

    /**
     * 小计金额
     */
    private BigDecimal subtotal;

    /**
     * 创建时间
     */
    private Date createdAt;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getFruitId() {
        return fruitId;
    }

    public void setFruitId(Integer fruitId) {
        this.fruitId = fruitId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", fruitId=" + fruitId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                ", createdAt=" + createdAt +
                '}';
    }
}
