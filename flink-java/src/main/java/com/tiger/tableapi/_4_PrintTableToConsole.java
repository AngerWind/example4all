package com.tiger.tableapi;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.junit.Test;

/**
 * 将表打印到控制台的办法
 */
public class _4_PrintTableToConsole {

    @Test
    public void test1() throws Exception {
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                // 使用流处理模式
                .inStreamingMode().useBlinkPlanner().build();
        TableEnvironment tableEnv = TableEnvironment.create(settings);


        // 通过sql创建表
        tableEnv.executeSql("create table `event` (`user` STRING, `url` string, `timestamp` bigint)"
                + "with ('connector' = 'filesystem', 'path' = 'input/click.txt', 'format' = 'csv')");

        // 对mary表进行查询并打印
        tableEnv.executeSql("select * from mary").print();

        // 创建一张打印到控制台的表
        tableEnv.executeSql("create table `console` (`user` string, url string, `timestamp` bigint)"
                + "with ('connector' = 'print')");
        // 通过console打印到控制台
        tableEnv.from("event").executeInsert("console");
    }
}
