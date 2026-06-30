package com.fruitshop.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 */
public class DBUtil {
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    /**
     * 获取数据源
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 关闭连接
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
