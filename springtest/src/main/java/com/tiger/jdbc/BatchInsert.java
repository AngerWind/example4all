package com.tiger.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/10
 * @description
 */
public class BatchInsert {

    Connection conn;
    int total = 200000;
    int batchSize = 10000;

    @Before
    public void init() {
        // String url = "jdbc:mysql://localhost:3306/test?useTimezone=true&serverTimezone=UTC";
        String url = "jdbc:mysql://localhost:3306/test?useTimezone=true&serverTimezone=UTC&rewriteBatchedStatements=true";
        String username = "root";
        String password = "871403165";

        // 2.获取与数据库的链接
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        PreparedStatement ps = null;
        try {
            // 创建表
            ps = conn.prepareStatement("create table if not exists jdbc_test (id bigint(20), name varchar(256))");
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Person {
        private Long id;
        private String name;
    }

    @SneakyThrows
    @Test
    // 247243
    // 247377
    public void singleInsertWithAutoCommit() {
        PreparedStatement ps = null;
        Date start = new Date();
        System.out.println("开始时间: " + start);
        try {
            // 插入数据
            ps = conn.prepareStatement("insert into jdbc_test(id, name) values(?,?)");
            List<Person> list = generateData(total);
            for (int i = 0; i < total; i++) {
                ps.setLong(1, list.get(i).getId());
                ps.setString(2, list.get(i).getName());
                ps.execute();
            }
            Date end = new Date();
            System.out.println("结束时间: " + end);
            System.out.println("耗时: " + (end.getTime() - start.getTime()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @SneakyThrows
    @Test
    // 53379
    // 51968
    public void singleInsertWithoutAutoCommit() {
        PreparedStatement ps = null;
        conn.setAutoCommit(false);
        Date start = new Date();
        System.out.println("开始时间: " + start);
        try {
            // 插入数据
            ps = conn.prepareStatement("insert into jdbc_test(id, name) values(?,?)");
            List<Person> list = generateData(total);
            for (int i = 0; i < total; i++) {
                ps.setLong(1, list.get(i).getId());
                ps.setString(2, list.get(i).getName());
                ps.execute();
            }
            conn.commit();
            Date end = new Date();
            System.out.println("结束时间: " + end);
            System.out.println("耗时: " + (end.getTime() - start.getTime()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @SneakyThrows
    @Test
    // 53396
    // 52851
    public void singleInsertWithBatchAutoCommit() {
        PreparedStatement ps = null;
        conn.setAutoCommit(false);
        Date start = new Date();
        System.out.println("开始时间: " + start);
        try {
            // 插入数据
            ps = conn.prepareStatement("insert into jdbc_test(id, name) values(?,?)");
            List<Person> list = generateData(total);
            for (int i = 0; i < total; i++) {
                ps.setLong(1, list.get(i).getId());
                ps.setString(2, list.get(i).getName());
                ps.execute();
                if (i != 0 && i % batchSize == 0) {
                    conn.commit();
                }
            }
            conn.commit();
            Date end = new Date();
            System.out.println("结束时间: " + end);
            System.out.println("耗时: " + (end.getTime() - start.getTime()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Test
    // 225905
    // 1449
    public void batchInsertWithAutoCommit() {
        PreparedStatement ps = null;
        try {
            Date start = new Date();
            System.out.println("开始时间: " + start);
            // 插入数据
            ps = conn.prepareStatement("insert into jdbc_test(id, name) values(?,?)");
            List<Person> list = generateData(total);
            for (int i = 0; i < total; i++) {
                ps.setLong(1, list.get(i).getId());
                ps.setString(2, list.get(i).getName());
                ps.addBatch();
                if (i != 0 && i % batchSize == 0) {
                    ps.executeBatch();// 将容器中的sql语句提交
                    ps.clearBatch();// 清空容器，为下一次打包做准备
                }
            }
            // 为防止有sql语句漏提交【如i结束时%500！=0的情况】，需再次提交sql语句
            ps.executeBatch();
            ps.clearBatch();


            Date end = new Date();
            System.out.println("结束时间: " + end);
            System.out.println("耗时: " + (end.getTime() - start.getTime()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }


    @Test
    // 29680
    // 1447
    public void batchInsertWithoutAutoCommit() {
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            Date start = new Date();
            System.out.println("开始时间: " + start);
            // 插入数据
            ps = conn.prepareStatement("insert into jdbc_test(id, name) values(?,?)");
            List<Person> list = generateData(total);
            for (int i = 0; i < total; i++) {
                ps.setLong(1, list.get(i).getId());
                ps.setString(2, list.get(i).getName());
                ps.addBatch();
                if (i != 0 && i % batchSize == 0) {
                    ps.executeBatch();// 将容器中的sql语句提交
                    ps.clearBatch();// 清空容器，为下一次打包做准备
                }
            }
            // 为防止有sql语句漏提交【如i结束时%500！=0的情况】，需再次提交sql语句
            ps.executeBatch();
            ps.clearBatch();
            conn.commit();
            conn.setAutoCommit(true); // 回复自动提交

            Date end = new Date();
            System.out.println("结束时间: " + end);
            System.out.println("耗时: " + (end.getTime() - start.getTime()));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Test
    // 28528
    // 1463
    public void batchInsertWithBatchAutoCommit() {
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            Date start = new Date();
            System.out.println("开始时间: " + start);
            // 插入数据
            ps = conn.prepareStatement("insert into jdbc_test(id, name) values(?,?)");
            List<Person> list = generateData(total);
            for (int i = 0; i < total; i++) {
                ps.setLong(1, list.get(i).getId());
                ps.setString(2, list.get(i).getName());
                ps.addBatch();
                if (i != 0 && i % batchSize == 0) {
                    ps.executeBatch();// 将容器中的sql语句提交
                    ps.clearBatch();// 清空容器，为下一次打包做准备
                    conn.commit();
                }
            }
            // 为防止有sql语句漏提交【如i结束时%500！=0的情况】，需再次提交sql语句
            ps.executeBatch();
            ps.clearBatch();
            conn.commit();
            conn.setAutoCommit(true); // 回复自动提交

            Date end = new Date();
            System.out.println("结束时间: " + end);
            System.out.println("耗时: " + (end.getTime() - start.getTime()));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }


    private List<Person> generateData(int total) {
        ArrayList<Person> list = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            list.add(new Person((long)i, "name" + i));
        }
        return list;
    }
}
