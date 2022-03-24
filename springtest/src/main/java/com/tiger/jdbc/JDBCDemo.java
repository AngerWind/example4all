package com.tiger.jdbc;

import java.sql.*;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JDBCDemo
 * @date 2022/3/1 16:07
 * @description
 */
public class JDBCDemo {

    public static void main(String[] args) {

        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            // 1、注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2、获取数据库的连接对象
            con = DriverManager.getConnection("jdbc:mysql://10.1.0.84:9030/test", "root", "Fh8x3k0LnworNe");

            // 3、定义sql语句
            String sql = "select * from collection_data_account_pre";

            // 4、获取执行sql语句的对象
            stat = con.createStatement();

            // 5、执行sql并接收返回结果
            rs = stat.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (con != null) { // 避免空指针异常
                // 7、释放资源
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stat != null) { // 避免空指针异常
                // 7、释放资源
                try {
                    stat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) { // 避免空指针异常
                // 7、释放资源
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
