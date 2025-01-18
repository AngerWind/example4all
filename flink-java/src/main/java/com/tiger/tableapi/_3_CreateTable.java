package com.tiger.tableapi;

import static org.apache.flink.table.api.Expressions.$;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

public class _3_CreateTable {

    @Test
    public void test() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .withBuiltInCatalogName("default_catalog") // 指定默认使用的catalog, 一个catalog下面可以有多个数据库
                .withBuiltInDatabaseName("default_database") // 指定默认使用的database
                .inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);

        tableEnv.executeSql("create table `event` (`user` STRING, `url` string, `timestamp` bigint)"
                + "with ('connector' = 'filesystem', 'path' = 'input/click.txt', 'format' = 'csv')");
        tableEnv.executeSql("create table `console` (`user` string, url string, `timestamp` bigint)"
                + "with ('connector' = 'print')");

        /**
         * 在tableEnv中有两种执行sql的方式
         */
        // 通过executeSql可以执行DDL, DML, DQL
        TableResult result = tableEnv.executeSql("select * from event");


        // 通过sqlQuery会获得一个table对象
        Table table = tableEnv.sqlQuery("select * from event");
        table.executeInsert("console"); // 输出到控制台
        table.execute().print(); // 拉取到client端并打印

    }

    @Test
    public void test1() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .withBuiltInCatalogName("default_catalog") // 指定默认使用的catalog, 一个catalog下面可以有多个数据库
                .withBuiltInDatabaseName("default_database") // 指定默认使用的database
                .inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);


        // 通过sql创建表
        tableEnv.executeSql("create table `event` (`user` STRING, `url` string, `timestamp` bigint)"
                + "with ('connector' = 'filesystem', 'path' = 'input/click.txt', 'format' = 'csv')");

        // 这里写的output1表示的是一个目录, 因为多并行度会写多个文件
        tableEnv.executeSql("create table `result1` (`user` string, url string, `timestamp` bigint)"
                + "with ('connector' = 'filesystem', 'path' = 'output1', 'format' = 'csv')");
        tableEnv.executeSql("create table `result2` (`user` string, url string, `timestamp` bigint)"
                + "with ('connector' = 'filesystem', 'path' = 'output2', 'format' = 'csv')");

        // 获取tableEnv中注册的表
        Table event = tableEnv.from("`event`");

        // 对表通过table api进行转换
        Table maryTable1 = event.select($("user"), $("url"), $("timestamp"))
                .where($("user").isEqual("Mary"));

        // 转换出来的表并没有在env中注册, 无法通过sql来操作
        // 需要通过createTemporaryView注册到表环境之后才能过通过sql来操作
        tableEnv.createTemporaryView("mary", maryTable1);

        // 通过sql查询刚刚注册的表mary
        Table maryTable2 = tableEnv.sqlQuery("select * from mary");
        // 对mary表进行查询并打印
        tableEnv.executeSql("select * from mary").print();

        // 通过table api 输出到表result
        maryTable2.executeInsert("`result1`");

        // 直接通过sql输出到表result
        tableEnv.executeSql("insert into `result2` select * from mary");

        // 创建一张打印到控制台的表
        tableEnv.executeSql("create table `console` (`user` string, url string, `timestamp` bigint)"
                + "with ('connector' = 'print')");
        // tableEnv.executeSql("insert into `console` select * from mary");
        maryTable2.executeInsert("console");


        // sleep 1s, 防止main线程结束导致flink写文件未完成
        Thread.sleep(1000);
    }
}
