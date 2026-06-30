package com.fruitshop.dao;

import com.fruitshop.entity.Supplier;
import com.fruitshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 供应商数据访问层
 */
public class SupplierDao {

    /**
     * 根据ID查询供应商
     */
    public Supplier findById(Integer id) {
        String sql = "SELECT * FROM suppliers WHERE id = ?";
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
     * 查询所有供应商
     */
    public List<Supplier> findAll() {
        String sql = "SELECT * FROM suppliers ORDER BY created_at DESC";
        List<Supplier> list = new ArrayList<>();
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
     * 添加供应商
     */
    public int insert(Supplier supplier) {
        String sql = "INSERT INTO suppliers(company_name, contact_person, phone, email, address, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, supplier.getCompanyName());
            pstmt.setString(2, supplier.getContactPerson());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getEmail());
            pstmt.setString(5, supplier.getAddress());
            pstmt.setTimestamp(6, new Timestamp(supplier.getCreatedAt().getTime()));
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    supplier.setId(keys.getInt(1));
                }
            }
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新供应商信息
     */
    public int update(Supplier supplier) {
        String sql = "UPDATE suppliers SET company_name=?, contact_person=?, phone=?, email=?, address=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getCompanyName());
            pstmt.setString(2, supplier.getContactPerson());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getEmail());
            pstmt.setString(5, supplier.getAddress());
            pstmt.setInt(6, supplier.getId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除供应商
     */
    public int delete(Integer id) {
        String sql = "DELETE FROM suppliers WHERE id = ?";
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
    private Supplier mapRow(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getInt("id"));
        supplier.setCompanyName(rs.getString("company_name"));
        supplier.setContactPerson(rs.getString("contact_person"));
        supplier.setPhone(rs.getString("phone"));
        supplier.setEmail(rs.getString("email"));
        supplier.setAddress(rs.getString("address"));
        supplier.setCreatedAt(rs.getTimestamp("created_at"));
        return supplier;
    }
}
