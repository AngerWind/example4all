package com.tiger.jdbc;

import java.sql.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        String url = "jdbc:mysql://localhost:3306/test?useTimezone=true&serverTimezone=UTC";
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
        PreparedStatement ps = null;
        try  {
            // 创建表
            ps = conn.prepareStatement("create table jdbc_test (id bigint(20), name varchar(256))");
            // 修改表
            // ps = conn.prepareStatement("alter table jdbc_test add primary key pm(id)");
            // 备份数据库
            // ps = conn.prepareStatement("backup database shen to disk='F:/123.bak'");
            // 删除表
            // ps = conn.prepareStatement("drop table jdbc_test");

            // execute可以执行任何类型的sql
            // 如果返回true, 表示第一个参数是ResultSet, 说明该sql为dql
            // 如果返回false, 表示的影响的行, 或者没有返回值, 说明该语句为ddl, dml
            ps.execute();
        } catch (SQLException e) {
            // 如果表存在, 或者删除表失败, 那么会直接报错
            throw new RuntimeException(e);
        }finally {
            if (ps!= null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Test
    public void insert() {
        PreparedStatement ps = null;
        try {
            // 插入数据
            ps = conn.prepareStatement("insert into jdbc_test(id, name) values(?,?)");
            ps.setLong(1, 1);
            ps.setString(2, "zhangsna");
            // excuteUpdate只能执行dml语句, 并返回影响的行数
            int executeUpdate = ps.executeUpdate();
            System.out.println("影响的行数: " + executeUpdate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps!= null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }


    @Test
    public void select() {
        try (PreparedStatement ps = conn.prepareStatement("select name, id from jdbc_test where name like concat('%',?, '%')");) {
            ps.setString(1, "zhang");
            // !!!!! ResultSet是需要关闭的
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()) {
                    // 在使用rs.getLong, rs.getInt, rs.getFloat等方法获取到数据之后, 一定要通过rs.wasNull方法判断获取到的是不是null
                    // 因为这些方法会把null转换为0, 这样你就不知道获取到的具体是0还是null
                    // rs.getInt("id");
                    // rs.wasNull();

                    // 推荐使用getObject方法, 然后传入对应的包装类型, 这样如果数据为null, 那么你就知道了
                    // rs.getObject("id", Integer.class);


                    System.out.printf("name: %s, id: %s\n", rs.getString("name"), rs.getLong("id"));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void update() {
        try (PreparedStatement ps = conn.prepareStatement("update jdbc_test set name = ? where id = ?");) {
            ps.setString(1, "王五");
            ps.setLong(2, 1);
            int executeUpdate = ps.executeUpdate();
            System.out.println("影响的行数: " + executeUpdate);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void delete() {
        try (PreparedStatement ps = conn.prepareStatement("delete from jdbc_test where id = ?");) {
            ps.setLong(1, 1);
            int executeUpdate = ps.executeUpdate();
            System.out.println("影响的行数: " + executeUpdate);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
