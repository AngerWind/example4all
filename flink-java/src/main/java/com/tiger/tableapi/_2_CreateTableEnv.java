package com.tiger.tableapi;

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

        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);

        // 直接创建表环境
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
            // 使用流处理模式
            .inStreamingMode().build();
        TableEnvironment tableEnv1 = TableEnvironment.create(settings);

    }
}
