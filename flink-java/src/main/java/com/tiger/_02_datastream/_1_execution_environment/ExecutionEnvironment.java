package com.tiger._02_datastream._1_execution_environment;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;


public class ExecutionEnvironment {

    @Test
    public void getExecutionEnvironment() {
        /*
            根据环境自适应获取env

            当你在idea中执行的时候, 默认会创建一个LocalStreamEnvironment

            当你通过
                1. webUI提交到Standalone Session, Yarn Session, Kubernetes Session的时候
                2. flink run来提交作业的时候
                3. 编程式SDK (比如flink-client) 提交作业
            都会通过脚本来设置execution.target为remote, 这样就会启动一个RemoteStreamEnvironment
         */
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
