package com.fruitshop.dao;

import com.fruitshop.entity.Fruit;
import com.fruitshop.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 水果数据访问层
 */
public class FruitDao {

    /**
     * 根据ID查询水果
     */
    public Fruit findById(Integer id) {
        String sql = "SELECT * FROM fruits WHERE id = ?";
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
     * 查询所有水果
     */
    public List<Fruit> findAll() {
        String sql = "SELECT * FROM fruits ORDER BY created_at DESC";
        List<Fruit> list = new ArrayList<>();
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
     * 根据分类查询水果
     */
    public List<Fruit> findByCategory(String category) {
        String sql = "SELECT * FROM fruits WHERE category = ? ORDER BY created_at DESC";
        List<Fruit> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
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
     * 添加水果
     */
    public int insert(Fruit fruit) {
        String sql = "INSERT INTO fruits(name, category, price, stock_quantity, supplier_id, origin, shelf_life_days, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, fruit.getName());
            pstmt.setString(2, fruit.getCategory());
            pstmt.setBigDecimal(3, fruit.getPrice());
            pstmt.setInt(4, fruit.getStockQuantity());
            pstmt.setInt(5, fruit.getSupplierId());
            pstmt.setString(6, fruit.getOrigin());
            pstmt.setInt(7, fruit.getShelfLifeDays());
            pstmt.setTimestamp(8, new Timestamp(fruit.getCreatedAt().getTime()));
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    fruit.setId(keys.getInt(1));
                }
            }
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新水果信息
     */
    public int update(Fruit fruit) {
        String sql = "UPDATE fruits SET name=?, category=?, price=?, stock_quantity=?, supplier_id=?, origin=?, shelf_life_days=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fruit.getName());
            pstmt.setString(2, fruit.getCategory());
            pstmt.setBigDecimal(3, fruit.getPrice());
            pstmt.setInt(4, fruit.getStockQuantity());
            pstmt.setInt(5, fruit.getSupplierId());
            pstmt.setString(6, fruit.getOrigin());
            pstmt.setInt(7, fruit.getShelfLifeDays());
            pstmt.setInt(8, fruit.getId());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除水果
     */
    public int delete(Integer id) {
        String sql = "DELETE FROM fruits WHERE id = ?";
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
     * 更新库存
     */
    public int updateStock(Integer id, Integer quantity) {
        String sql = "UPDATE fruits SET stock_quantity = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 映射结果集到对象
     */
    private Fruit mapRow(ResultSet rs) throws SQLException {
        Fruit fruit = new Fruit();
        fruit.setId(rs.getInt("id"));
        fruit.setName(rs.getString("name"));
        fruit.setCategory(rs.getString("category"));
        fruit.setPrice(rs.getBigDecimal("price"));
        fruit.setStockQuantity(rs.getInt("stock_quantity"));
        fruit.setSupplierId(rs.getInt("supplier_id"));
        fruit.setOrigin(rs.getString("origin"));
        fruit.setShelfLifeDays(rs.getInt("shelf_life_days"));
        fruit.setCreatedAt(rs.getTimestamp("created_at"));
        return fruit;
    }
}
