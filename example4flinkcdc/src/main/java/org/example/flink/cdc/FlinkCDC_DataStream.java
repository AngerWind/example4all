package org.example.flink.cdc;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/12
 * @description
 */
public class FlinkCDC_DataStream {

    public static void main(String[] args) throws Exception {
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


        // !!!!!!!!!!!!使用flink cdc 必须开启这个库的binlog!!!!!!!!!!!!!!
        Properties properties = new Properties();
        properties.setProperty("useSSL", "false");
        properties.setProperty("allowPublicKeyRetrieval", "true");
        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                .hostname("127.0.0.1")
                .port(3306)
                // 可以指定多个数据库
                .databaseList("test", "test_route")
                // 可以指定多个表, 在指定表的时候要使用库名.表名的形式
                .tableList("test.t1", "test.t2")
                .username("root")
                .password("123456")
                .jdbcProperties(properties)
                // flink cdc启动的时候, 可以指定一个StartupMode
                // initial: 会先扫描所有监控的表, 将所有数据读取过来, 相当于全量读取数据作为初始化
                //          读取的数据中, op类型为r, 表示是初始化数据
                // EARLIEST_OFFSET: 不做数据的初始化扫描, 而是从最早的binlog开始读取数据
                //                  要注意这种方式, 如果binlog不全的话, 可能会导致数据有问题
                //                  比如在最开始的时候表没有开启binlog, 然后半路开启, 就会导致数据有问题
                // latest-offset: 不做数据的初始化扫描, 而是直接读取最新的binlog
                // specific-offset: 从数据库的binlog中读取数据, 从指定的位置开始读取数据
                // TIMESTAMP: 不做数据的初始化扫描, 而是直接从指定的时间戳开始读取binlog
                .startupOptions(StartupOptions.initial())
                // flink cdc底层是使用debezium来读取binlog的, 他会将读取到的binlog转换为SourceRecord
                // 在古早之前, 需要用户自定义来解析这个SourceRecord, 现在flink cdc已经做了这个功能, 所以不需要用户自定义
                // JsonDebeziumDeserializationSchema会将SourceRecord转换为json字符串
                .deserializer(new JsonDebeziumDeserializationSchema())
                .build();


        /*
         * 打印的json字段中, 有一个op字段用来表示数据的操作类型:
         * r: 表示数据的初始化扫描
         * u: 表示更新数据
         * d: 表示删除数据
         * c: 表示插入数据
         */
        env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "mysql-cdc-source")
                .setParallelism(1)
                .print();
        // 执行任务
        env.execute("Flink CDC Test");
    }
}
