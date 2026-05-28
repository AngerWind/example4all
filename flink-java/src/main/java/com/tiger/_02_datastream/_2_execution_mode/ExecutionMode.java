package com.tiger._02_datastream._2_execution_mode;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;


public class ExecutionMode {

    @Test
    public void oldVersion() {
        // 获取批执行环境, 使用的是DataSet API
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 获取流执行环境, 使用的是DataStream API
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
    }

    @Test
    public void newVersion() {
        // 从flink1.12起, 已经实现了流批一体
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 获取流执行环境, 并设置执行模式为批处理
        // 也可以通过命令行设置bin/flink run -Dexection.runtime-mode=BATCH来指定执行批处理
        env.setRuntimeMode(RuntimeExecutionMode.BATCH);
    }
}
