package com.fruitshop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体类
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Integer id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 顾客ID
     */
    private Integer customerId;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态（待支付/已支付/配送中/已完成/已取消）
     */
    private String status;

    /**
     * 支付方式（微信支付/支付宝/银行卡/现金）
     */
    private String paymentMethod;

    /**
     * 配送地址
     */
    private String deliveryAddress;

    /**
     * 下单时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", customerId=" + customerId +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
