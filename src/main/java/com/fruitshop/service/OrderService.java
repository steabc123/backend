package com.fruitshop.service;

import com.fruitshop.dao.FruitDao;
import com.fruitshop.dao.OrderDao;
import com.fruitshop.dao.OrderItemDao;
import com.fruitshop.entity.Fruit;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单业务逻辑层
 */
public class OrderService {
    private OrderDao orderDao = new OrderDao();
    private OrderItemDao orderItemDao = new OrderItemDao();
    private FruitDao fruitDao = new FruitDao();

    /**
     * 根据ID查询订单
     */
    public Order getOrderById(Integer id) {
        return orderDao.findById(id);
    }

    /**
     * 根据订单编号查询订单
     */
    public Order getOrderByOrderNo(String orderNo) {
        return orderDao.findByOrderNo(orderNo);
    }

    /**
     * 根据顾客ID查询订单列表
     */
    public List<Order> getOrdersByCustomerId(Integer customerId) {
        return orderDao.findByCustomerId(customerId);
    }

    /**
     * 查询所有订单
     */
    public List<Order> getAllOrders() {
        return orderDao.findAll();
    }

    /**
     * 根据状态查询订单
     */
    public List<Order> getOrdersByStatus(String status) {
        return orderDao.findByStatus(status);
    }

    /**
     * 创建订单（包含订单明细）
     */
    public boolean createOrder(Order order, List<OrderItem> orderItems) {
        if (order == null || orderItems == null || orderItems.isEmpty()) {
            throw new RuntimeException("订单信息不能为空");
        }

        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            // 验证水果是否存在
            Fruit fruit = fruitDao.findById(item.getFruitId());
            if (fruit == null) {
                throw new RuntimeException("水果ID " + item.getFruitId() + " 不存在");
            }

            // 检查库存
            if (fruit.getStockQuantity() < item.getQuantity().intValue()) {
                throw new RuntimeException("水果 " + fruit.getName() + " 库存不足");
            }

            // 设置单价和小计
            item.setUnitPrice(fruit.getPrice());
            item.setSubtotal(fruit.getPrice().multiply(item.getQuantity()));
            totalAmount = totalAmount.add(item.getSubtotal());
        }

        // 设置订单信息
        if (order.getOrderNo() == null || order.getOrderNo().isEmpty()) {
            // 自动生成订单号：ORD + 时间戳 + 随机数
            String orderNo = "ORD" + System.currentTimeMillis() + (int)(Math.random() * 1000);
            order.setOrderNo(orderNo);
        }
        order.setTotalAmount(totalAmount);
        if (order.getStatus() == null || order.getStatus().isEmpty()) {
            order.setStatus("待支付");
        }
        if (order.getCreatedAt() == null) {
            order.setCreatedAt(new Date());
        }
        if (order.getUpdatedAt() == null) {
            order.setUpdatedAt(new Date());
        }

        // 插入订单
        int orderRows = orderDao.insert(order);
        if (orderRows <= 0) {
            throw new RuntimeException("创建订单失败");
        }

        // 设置订单ID到订单项
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            if (item.getCreatedAt() == null) {
                item.setCreatedAt(new Date());
            }
        }

        // 批量插入订单明细
        int[] itemRows = orderItemDao.batchInsert(orderItems);
        if (itemRows.length != orderItems.size()) {
            throw new RuntimeException("创建订单明细失败");
        }

        // 扣减库存
        for (OrderItem item : orderItems) {
            Fruit fruit = fruitDao.findById(item.getFruitId());
            int newStock = fruit.getStockQuantity() - item.getQuantity().intValue();
            fruitDao.updateStock(item.getFruitId(), newStock);
        }

        return true;
    }

    // 合法的状态流转映射
    private static final Map<String, List<String>> VALID_TRANSITIONS = new HashMap<>();
    static {
        VALID_TRANSITIONS.put("待支付", Arrays.asList("已支付", "已取消"));
        VALID_TRANSITIONS.put("已支付", Arrays.asList("配送中", "已取消"));
        VALID_TRANSITIONS.put("配送中", Arrays.asList("已完成", "已取消"));
        VALID_TRANSITIONS.put("已完成", Arrays.asList());
        VALID_TRANSITIONS.put("已取消", Arrays.asList());
    }

    /**
     * 更新订单状态（含状态流转验证）
     */
    public boolean updateOrderStatus(Integer orderId, String status) {
        if (orderId == null || status == null || status.isEmpty()) {
            throw new RuntimeException("参数不能为空");
        }

        // 验证订单是否存在
        Order order = orderDao.findById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        String currentStatus = order.getStatus();

        // 状态没有变化，直接返回成功
        if (currentStatus.equals(status)) {
            return true;
        }

        // 验证状态流转是否合法
        List<String> allowedNext = VALID_TRANSITIONS.getOrDefault(currentStatus, Arrays.asList());
        if (!allowedNext.contains(status)) {
            throw new RuntimeException("不允许从「" + currentStatus + "」变更到「" + status + "」");
        }

        // 如果订单状态变更为"已取消"，恢复库存
        if ("已取消".equals(status)) {
            List<OrderItem> items = orderItemDao.findByOrderId(orderId);
            for (OrderItem item : items) {
                Fruit fruit = fruitDao.findById(item.getFruitId());
                if (fruit != null) {
                    int newStock = fruit.getStockQuantity() + item.getQuantity().intValue();
                    fruitDao.updateStock(item.getFruitId(), newStock);
                }
            }
        }

        int rows = orderDao.updateStatus(orderId, status);
        return rows > 0;
    }

    /**
     * 删除订单（同时删除订单明细）
     */
    public boolean deleteOrder(Integer orderId) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }

        // 先删除订单明细
        orderItemDao.deleteByOrderId(orderId);

        // 再删除订单
        int rows = orderDao.delete(orderId);
        return rows > 0;
    }

    /**
     * 获取订单明细列表（包含水果名称）
     */
    public List<OrderItem> getOrderItems(Integer orderId) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        List<OrderItem> items = orderItemDao.findByOrderId(orderId);
        // 填充水果名称
        for (OrderItem item : items) {
            Fruit fruit = fruitDao.findById(item.getFruitId());
            if (fruit != null) {
                item.setFruitName(fruit.getName());
            }
        }
        return items;
    }
}
