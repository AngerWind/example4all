package com.tiger.datastream._6_sink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * 自定义到hbase的连接器
 */
public class SinkToCustom {

    @Test
    public void sinkToHbase() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.fromElements("hello", "world").addSink(new RichSinkFunction<String>() {
            // 管理 Hbase 的配置信息,这里因为 Configuration的重名问题，将类以完整路径导入
            public org.apache.hadoop.conf.Configuration configuration;
            // 管理 Hbase 连接
            public Connection connection;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                configuration = HBaseConfiguration.create();
                configuration.set("hbase.zookeeper.quorum", "hadoop102:2181");
                connection = ConnectionFactory.createConnection(configuration);
            }

            @Override
            public void invoke(String value, Context context) throws Exception {
                // 表名为 test
                Table table = connection.getTable(TableName.valueOf("test"));
                // 指定 rowkey
                Put put = new Put("rowkey".getBytes(StandardCharsets.UTF_8));
                // 指定列名
                put.addColumn("info".getBytes(StandardCharsets.UTF_8),
                        value.getBytes(StandardCharsets.UTF_8), // 写入的数据
                        "1".getBytes(StandardCharsets.UTF_8)); // 写入的数据

                table.put(put); // 执行 put 操作
                table.close(); // 将表关闭
            }

            @Override
            public void close() throws Exception {
                super.close();
                connection.close(); // 关闭连接
            }
        });
        env.execute();
    }
}
