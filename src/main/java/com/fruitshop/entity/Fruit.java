package com.fruitshop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 水果实体类
 */
public class Fruit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 水果ID
     */
    private Integer id;

    /**
     * 水果名称
     */
    private String name;

    /**
     * 分类
     */
    private String category;

    /**
     * 单价(元/斤)
     */
    private BigDecimal price;

    /**
     * 库存数量(斤)
     */
    private Integer stockQuantity;

    /**
     * 供应商ID
     */
    private Integer supplierId;

    /**
     * 产地
     */
    private String origin;

    /**
     * 保质期(天)
     */
    private Integer shelfLifeDays;

    /**
     * 入库时间
     */
    private Date createdAt;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getShelfLifeDays() {
        return shelfLifeDays;
    }

    public void setShelfLifeDays(Integer shelfLifeDays) {
        this.shelfLifeDays = shelfLifeDays;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", supplierId=" + supplierId +
                ", origin='" + origin + '\'' +
                ", shelfLifeDays=" + shelfLifeDays +
                ", createdAt=" + createdAt +
                '}';
    }
}
