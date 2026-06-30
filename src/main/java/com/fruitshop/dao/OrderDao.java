package com.fruitshop.dao;

import com.fruitshop.entity.Order;
import com.fruitshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单数据访问层
 */
public class OrderDao {

    /**
     * 根据ID查询订单
     */
    public Order findById(Integer id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
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
     * 根据订单编号查询订单
     */
    public Order findByOrderNo(String orderNo) {
        String sql = "SELECT * FROM orders WHERE order_no = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderNo);
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
     * 根据顾客ID查询订单列表
     */
    public List<Order> findByCustomerId(Integer customerId) {
        String sql = "SELECT * FROM orders WHERE customer_id = ? ORDER BY created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
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
     * 查询所有订单
     */
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据状态查询订单
     */
    public List<Order> findByStatus(String status) {
        String sql = "SELECT * FROM orders WHERE status = ? ORDER BY created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
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
     * 添加订单
     */
    public int insert(Order order) {
        String sql = "INSERT INTO orders(order_no, customer_id, total_amount, status, payment_method, delivery_address, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, order.getOrderNo());
            pstmt.setInt(2, order.getCustomerId());
            pstmt.setBigDecimal(3, order.getTotalAmount());
            pstmt.setString(4, order.getStatus());
            pstmt.setString(5, order.getPaymentMethod());
            pstmt.setString(6, order.getDeliveryAddress());
            pstmt.setTimestamp(7, new Timestamp(order.getCreatedAt().getTime()));
            pstmt.setTimestamp(8, new Timestamp(order.getUpdatedAt().getTime()));
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    order.setId(keys.getInt(1));
                }
            }
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新订单状态
     */
    public int updateStatus(Integer id, String status) {
        String sql = "UPDATE orders SET status=?, updated_at=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, id);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除订单
     */
    public int delete(Integer id) {
        String sql = "DELETE FROM orders WHERE id = ?";
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
     * 映射结果集到对象
     */
    private Order mapRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setOrderNo(rs.getString("order_no"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        return order;
    }
}
