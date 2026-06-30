package com.fruitshop.dao;

import com.fruitshop.entity.Customer;
import com.fruitshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 顾客数据访问层
 */
public class CustomerDao {

    /**
     * 根据ID查询顾客
     */
    public Customer findById(Integer id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
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
     * 根据手机号查询顾客
     */
    public Customer findByPhone(String phone) {
        String sql = "SELECT * FROM customers WHERE phone = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
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
     * 根据用户名和密码查询顾客（登录）
     */
    public Customer findByNameAndPassword(String name, String password) {
        String sql = "SELECT * FROM customers WHERE name = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, password);
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
     * 根据用户名查询顾客
     */
    public Customer findByName(String name) {
        String sql = "SELECT * FROM customers WHERE name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
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
     * 查询所有顾客
     */
    public List<Customer> findAll() {
        String sql = "SELECT * FROM customers ORDER BY created_at DESC";
        List<Customer> list = new ArrayList<>();
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
     * 添加顾客
     */
    public int insert(Customer customer) {
        String sql = "INSERT INTO customers(name, phone, gender, address, password, role, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getPhone());
            pstmt.setString(3, customer.getGender());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getPassword() != null ? customer.getPassword() : "123456");
            pstmt.setString(6, customer.getRole() != null ? customer.getRole() : "customer");
            pstmt.setTimestamp(7, new Timestamp(customer.getCreatedAt().getTime()));
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    customer.setId(keys.getInt(1));
                }
            }
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新顾客信息
     */
    public int update(Customer customer) {
        String sql = "UPDATE customers SET name=?, phone=?, gender=?, address=?, password=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getPhone());
            pstmt.setString(3, customer.getGender());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getPassword() != null ? customer.getPassword() : "123456");
            pstmt.setInt(6, customer.getId());
            System.out.println("执行SQL: " + sql);
            System.out.println("参数: name=" + customer.getName() + ", phone=" + customer.getPhone() + 
                             ", gender=" + customer.getGender() + ", address=" + customer.getAddress() + 
                             ", password=" + (customer.getPassword() != null ? "***" : "null") + ", id=" + customer.getId());
            int rows = pstmt.executeUpdate();
            System.out.println("更新结果: " + rows + " 行受影响");
            return rows;
        } catch (SQLException e) {
            System.err.println("更新顾客失败: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除顾客
     */
    public int delete(Integer id) {
        String sql = "DELETE FROM customers WHERE id = ?";
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
    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("name"));
        customer.setPhone(rs.getString("phone"));
        customer.setGender(rs.getString("gender"));
        customer.setAddress(rs.getString("address"));
        customer.setPassword(rs.getString("password"));
        customer.setRole(rs.getString("role"));
        customer.setCreatedAt(rs.getTimestamp("created_at"));
        return customer;
    }
}
