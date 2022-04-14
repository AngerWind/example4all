package com.tiger.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JDBCTest
 * @date 2022/4/6 16:41
 * @description
 */
public class JDBCTest {

    Connection conn;

    @Before
    public void init() {
        String url = "jdbc:mysql://localhost:3306/tenma_test";
        String username = "root";
        String password = "871403165";

        // 2.获取与数据库的链接
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @After
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Test
    public void ddl() {
        try (PreparedStatement ps = conn.prepareStatement("create database vvv")) {
            // PreparedStatement ps = conn.prepareStatement("create table xxx");//创建表
            // PreparedStatement ps = conn.prepareStatement("backup database shen to disk='F:/123.bak'");//备份数据库

            // 如果执行的是ddl语句
            boolean b = ps.execute();
            if (b) {
                System.out.println("创建成功！");
            } else {
                System.out.println("失败");
            }
        } catch (SQLException e) {
            // TODO: handle exception
        }
    }

    @Test
    public void dql() {
        try (PreparedStatement ps = conn.prepareStatement("select * from dag_xxx where id = ? and code = ?");){
            ps.setString(1, "11");
            ps.setString(2, "22");
            try (ResultSet resultSet = ps.executeQuery()) {

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
