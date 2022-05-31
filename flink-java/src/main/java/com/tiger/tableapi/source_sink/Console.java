package com.tiger.tableapi.source_sink;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.junit.Test;

public class Console {

    @Test
    public void console() {
        TableEnvironment tableEnvironment = TableEnvironment.create(EnvironmentSettings.newInstance().build());


        tableEnvironment.executeSql("create table `event` (`user` STRING, `url` string, `timestamp` bigint)"
                + "with ('connector' = 'filesystem', 'path' = 'input/click.txt', 'format' = 'csv')");

        // 创建到console的表
        tableEnvironment.executeSql("create table `console`  (`user` STRING, `url` string, `timestamp` bigint" +
                ") with (\n" +
                "    'connector' = 'print'\n" +
                ") ");

        tableEnvironment.executeSql("create table `console1`  (`user` STRING, `cnt` bigint" +
                ") with (\n" +
                "    'connector' = 'print'\n" +
                ") ");

        tableEnvironment.executeSql("insert into `console1` select `user`, count(`user`) as cnt from `event` group by `user`");

        // group by的语句不能打印到控制台?????
        // tableEnvironment.executeSql("insert into `console` select `user`, count(`user`) as cnt from `event` group by `user`");

    }
}
