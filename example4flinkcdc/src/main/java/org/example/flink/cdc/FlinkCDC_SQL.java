package org.example.flink.cdc;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/12
 * @description
 */
public class FlinkCDC_SQL {

    public static void main(String[] args) {

        // 创建环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度
        env.setParallelism(1);
        // Flink-CDC将读取binlog的位置信息以状态的方式保存在CK,如果想要做到断点续传, 需要从Checkpoint或者Savepoint启动程序
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        // 设置超时时间为1分钟
        env.getCheckpointConfig().setCheckpointTimeout(60 * 1000L);

        // 设置任务关闭的时候保留最后一次 checkpoint 数据
        env.getCheckpointConfig().enableExternalizedCheckpoints(
                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        // 指定从 CK 自动重启策略
        env.setRestartStrategy(RestartStrategies.failureRateRestart(
                3, Time.days(1L), Time.minutes(1L)
        ));
        // 设置状态后端
        env.setStateBackend(new HashMapStateBackend());
        env.getCheckpointConfig().setCheckpointStorage(
                "hdfs://hadoop102:8020/flinkCDC"
        );
        // 设置访问HDFS的用户名
        System.setProperty("HADOOP_USER_NAME", "atguigu");



        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        tableEnv.executeSql("create table t1(\n" +
                "    id string primary key NOT ENFORCED,\n" +
                "    name string" +
                ") WITH (\n" + " 'connector' = 'mysql-cdc',\n" +
                " 'hostname' = 'hadoop103',\n" +
                " 'port' = '3306',\n" +
                " 'username' = 'root',\n" +
                " 'password' = '000000',\n" +
                " 'database-name' = 'test',\n" +
                " 'table-name' = 't1'\n" + ")");

        Table table = tableEnv.sqlQuery("select * from t1");

        // 默认情况下, flink cdc会根据配置来判断是否扫描全表
        // 同时数据的形式和DataStream有所不同
        // 通过table api的形式, 打印的数据是一个changelog
        // +I 表示插入
        // -U 表示更新前
        // +U 表示更新后
        // -D 表示删除
        table.execute().print();

    }

}
