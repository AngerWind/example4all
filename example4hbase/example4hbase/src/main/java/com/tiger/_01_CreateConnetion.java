package com.tiger;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/1
 * @description
 */

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.AsyncConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.junit.jupiter.api.Test;

public class _01_CreateConnetion {

    @Test
    public void create1() throws IOException, ExecutionException, InterruptedException {
        // 设置hbase的地址
        Configuration conf = new Configuration();

        // 设置zookeeper的地址, 而不是hbase的地址
        conf.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104");

        // 创建 hbase 的连接, 使用同步的方式
        Connection connection = ConnectionFactory.createConnection(conf);

        // 创建连接, 获取异步执行的客户端
        CompletableFuture<AsyncConnection> asyncConnectionFutere = ConnectionFactory.createAsyncConnection(conf);
        AsyncConnection asyncConnection = asyncConnectionFutere.get();

        connection.close();
        asyncConnection.close();
    }

    /**
     * 配置属性也可以放在hbase-site.xml中, 代码会自动读取
     *
     * 对hbase的操作是通过Connection中的Admin和Table属性来操作hbase的
     *
     * Connection是线程安全的, 并且是重量级的,
     * Admin和Table是是轻量级的, 但是是线程不安全的
     *
     * 所以每个应用只创建一个连接, 然后通过Connection来创建Admin和Table
     * 每个线程都使用单独创建的Admin和Table
     */
    @Test
    public void create2() throws IOException, ExecutionException, InterruptedException {

        // 创建 hbase 的连接, 使用同步的方式
        Connection connection = ConnectionFactory.createConnection();

        // 创建连接, 获取异步执行的客户端
        CompletableFuture<AsyncConnection> asyncConnectionFutere = ConnectionFactory.createAsyncConnection();
    }
}