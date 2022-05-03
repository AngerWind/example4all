package com.tiger.datastream._1_execution_environment;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;


public class ExecutionEnvironment {

    @Test
    public void getExecutionEnvironment() {
        // 根据环境自适应获取env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    }

    @Test
    public void getLocalExecutionEnvironment() {
        // 根据本地env
        // 可以传入一个并行度, 默认为cpu逻辑核心数
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(12);
    }

    @Test
    public void getRemoteExecutionEnvironment() {
        // 根据远程env
        // 需要传入JobManager的主机地址, 端口号, 并指定要在集群中运行的jar包
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createRemoteEnvironment("host", 1234, "path/to/jarFile.jar");
    }
}
