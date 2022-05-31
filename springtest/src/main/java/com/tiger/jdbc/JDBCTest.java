package com.tiger.jdbc;

import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.HashSet;

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
        String url = "jdbc:mysql://localhost:3308/tenma_test?useTimezone=true&serverTimezone=UTC";
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
        try (PreparedStatement ps = conn.prepareStatement("create table delete_in (id bigint(20), name varchar(256))")) {
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
    public void ddl2() {
        try (PreparedStatement ps = conn.prepareStatement("alter table delete_in add primary key pm(id)")) {
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
        try (PreparedStatement ps = conn.prepareStatement("select id from delete_in limit 10000");){
            // ps.setString(1, "11");
            // ps.setString(2, "22");
            StringBuilder sb = new StringBuilder("select * from delete_in where id in (");
            try (ResultSet resultSet = ps.executeQuery()) {
                boolean i = false;
                while (resultSet.next()) {
                    if (i) {
                        sb.append(",");
                    } else {
                        i = true;
                    }
                    long id = resultSet.getLong("id");
                    sb.append(id);
                }
                sb.append(");");
                System.out.println(sb.toString());

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @SneakyThrows
    @Test
    public void dml() {

        HashSet<Long> ids = Sets.newHashSet();
        while (ids.size() < 4000000) {
            ids.add(RandomUtils.nextLong(10000, Long.MAX_VALUE));
        }
        int size = 0;
        StringBuilder sb = new StringBuilder("insert into delete_in values ");
        for (Long i : ids) {
            sb.append("(");
            sb.append(i);
            sb.append(", '");
            sb.append(RandomUtils.nextLong(1000, Long.MAX_VALUE));
            sb.append("')");

            if (size < 10000) {
                sb.append(",");
                size++;
            } else {
                size = 0;
                sb.append(";");
                PreparedStatement preparedStatement = conn.prepareStatement(sb.toString());
                boolean execute = preparedStatement.execute();
                sb = new StringBuilder();
                sb.append("insert into delete_in values ");
                if (execute) {
                    System.out.println("success");
                } else {
                    System.out.println("fail");
                }
            }

        }

    }

}
