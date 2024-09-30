package com.tiger.tableapi.source_sink;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.junit.Test;

public class _01_Print {

    /**
     * print连接器一般只用来写入, 不能用来读取
     *
     * print可以打印仅追加流 和 回撤流
     */
    @Test
    public void writeToConsole() throws InterruptedException {
        TableEnvironment tableEnvironment = TableEnvironment.create(EnvironmentSettings.newInstance().build());


        tableEnvironment.executeSql("create table `event` (`user` STRING, `url` string, `timestamp` bigint)"
                + "with ('connector' = 'filesystem', 'path' = 'input/click.txt', 'format' = 'csv')");

        // 创建到console的表
        tableEnvironment.executeSql("create table `console`  (`user` STRING, `url` string, `timestamp` bigint" +
                ") with (\n" +
                "    'connector' = 'print', \n" +
                "    'print-identifier' = 'AAA'\n" + // 可选的, 用于指定打印前缀
                ") ");
        // 打印追加流到控制台
        tableEnvironment.executeSql("insert into `console` select * From event");


        // 打印回撤流到控制台
        tableEnvironment.executeSql("create table `console1`  (`user` STRING, `cnt` bigint" +
                ") with (\n" +
                "    'connector' = 'print', \n" +
                "    'print-identifier' = 'BBB'\n" + // 可选的, 用于指定打印前缀
                ") ");
        tableEnvironment.executeSql("insert into `console1` select `user`, count(`user`) as cnt from `event` group by `user`");

        // sleep, 等待日志打印
        Thread.sleep(1000 * 60);
    }
}
