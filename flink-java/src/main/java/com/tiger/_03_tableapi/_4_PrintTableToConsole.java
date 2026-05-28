package com.tiger._03_tableapi;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

/**
 * 将表打印到控制台的办法
 */
public class _4_PrintTableToConsole {

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


        // 对event表进行查询并打印
        // 方式1
        TableResult tableResult = tableEnv.executeSql("select * from event where user = 'Mary'");
        tableResult.print();


        // 方式2
        TableResult tableResult1 = tableEnv.sqlQuery("select * from event").execute();
        tableResult1.print();


        // 方式3
        tableEnv.executeSql("create table `console` (`user` string, url string, `timestamp` bigint)"
            + "with ('connector' = 'print')");
        tableEnv.executeSql("insert into console select * from event");
    }
}
