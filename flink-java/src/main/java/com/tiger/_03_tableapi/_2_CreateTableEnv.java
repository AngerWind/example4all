package com.tiger._03_tableapi;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

/**
 * 两种创建表环境的env
 */
public class _2_CreateTableEnv {

    @Test
    public void test() {

        EnvironmentSettings settings = EnvironmentSettings.newInstance()
            .withBuiltInCatalogName("default_catalog") // 指定默认使用的catalog, 一个catalog下面可以有多个数据库
            .withBuiltInDatabaseName("default_database") // 指定默认使用的database
            .inStreamingMode().build();

        // 通过流式环境创建表环境 (推荐 )
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv, settings);

        // 直接创建表环境
        TableEnvironment tableEnv1 = TableEnvironment.create(settings);


        /*
             TableEnvironment和StreamTableEnvironment的区别:
                1. StreamTableEnvironment能够进行sql/table 到 stream的转换, 而TableEnvironment你只能使用纯sql
                   即TableEnvironment中没有toDataStream()等等方法让你从table转换为stream
                2. StreamTableEnvironment只能使用流处理, 而TableEnvironment可以使用流处理和批处理
         */

    }
}
