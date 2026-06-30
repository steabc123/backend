package com.fruitshop.dao;

import com.fruitshop.entity.OrderItem;
import com.fruitshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单明细数据访问层
 */
public class OrderItemDao {

    /**
     * 根据ID查询订单明细
     */
    public OrderItem findById(Integer id) {
        String sql = "SELECT * FROM order_items WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据订单ID查询订单明细列表
     */
    public List<OrderItem> findByOrderId(Integer orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ? ORDER BY created_at ASC";
        List<OrderItem> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 添加订单明细
     */
    public int insert(OrderItem orderItem) {
        String sql = "INSERT INTO order_items(order_id, fruit_id, quantity, unit_price, subtotal, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, orderItem.getOrderId());
            pstmt.setInt(2, orderItem.getFruitId());
            pstmt.setBigDecimal(3, orderItem.getQuantity());
            pstmt.setBigDecimal(4, orderItem.getUnitPrice());
            pstmt.setBigDecimal(5, orderItem.getSubtotal());
            pstmt.setTimestamp(6, new Timestamp(orderItem.getCreatedAt().getTime()));
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    orderItem.setId(keys.getInt(1));
                }
            }
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 批量添加订单明细
     */
    public int[] batchInsert(List<OrderItem> orderItems) {
        String sql = "INSERT INTO order_items(order_id, fruit_id, quantity, unit_price, subtotal, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (OrderItem item : orderItems) {
                pstmt.setInt(1, item.getOrderId());
                pstmt.setInt(2, item.getFruitId());
                pstmt.setBigDecimal(3, item.getQuantity());
                pstmt.setBigDecimal(4, item.getUnitPrice());
                pstmt.setBigDecimal(5, item.getSubtotal());
                pstmt.setTimestamp(6, new Timestamp(item.getCreatedAt().getTime()));
                pstmt.addBatch();
            }
            
            return pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new int[0];
    }

    /**
     * 删除订单明细
     */
    public int delete(Integer id) {
        String sql = "DELETE FROM order_items WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据订单ID删除所有明细
     */
    public int deleteByOrderId(Integer orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 映射结果集到对象
     */
    private OrderItem mapRow(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getInt("id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setFruitId(rs.getInt("fruit_id"));
        orderItem.setQuantity(rs.getBigDecimal("quantity"));
        orderItem.setUnitPrice(rs.getBigDecimal("unit_price"));
        orderItem.setSubtotal(rs.getBigDecimal("subtotal"));
        orderItem.setCreatedAt(rs.getTimestamp("created_at"));
        return orderItem;
    }
}
