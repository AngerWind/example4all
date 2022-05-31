package com.tiger.tableapi.source_sink;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.junit.Test;

public class Kafka {

    @Test
    public void console() {
        TableEnvironment tableEnvironment = TableEnvironment.create(EnvironmentSettings.newInstance().build());


        tableEnvironment.executeSql("create table `event` (`user` STRING, `url` string, `timestamp` bigint)"
                + "with ('connector' = 'filesystem', 'path' = 'input/click.txt', 'format' = 'csv')");

        // 创建到kafka的表
        // 在 KafkaTable 的字段中有一个 ts，它的声明中用到了METADATA FROM，
        // 这是表示一个“元数据列”（metadata column），它是由 Kafka 连接器的元数据“timestamp”生成的。
        // 这里的 timestamp 其实就是 Kafka 中数据自带的时间戳，我们把它直接作为元数据提取出来，转换成一个新的字段 ts。
        tableEnvironment.executeSql("CREATE TABLE KafkaTable (\n" +
                "`user` STRING,\n" +
                "`url` STRING,\n" +
                "`ts` TIMESTAMP(3) METADATA FROM 'timestamp'\n" +
                ") WITH (\n" +
                "'connector' = 'kafka',\n" +
                "'topic' = 'events',\n" +
                "'properties.bootstrap.servers' = 'localhost:9092',\n" +
                "'properties.group.id' = 'testGroup',\n" +
                "'scan.startup.mode' = 'earliest-offset',\n" +
                "'format' = 'csv'\n" +
                ")");

    }
}
