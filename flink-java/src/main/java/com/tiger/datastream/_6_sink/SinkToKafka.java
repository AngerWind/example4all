package com.tiger.datastream._6_sink;

import com.tiger.pojo.Event;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.junit.Test;

import java.util.Properties;

public class SinkToKafka {

    /**
     * 输入到kakfa的数据集在根目录input/click.txt
     * 发送消息到kafka可以使用bin/kafka-console-producer.sh --bootstrap-server hadoop102:9092 --topic send
     * 从kafka中接收消息可以使用bin/kafka-console-consumer.sh --bootstrap-server hadoop102:9092 --topic receive
     */
    @Test
    public void sinkToKafka() throws Exception {
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 1. 从kafka中读取数据
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "hadoop102:9092");
        properties.setProperty("group.id", "consumer-group");
        properties.setProperty("auto.offset.reset", "latest");
        DataStreamSource<String> stream =
                env.addSource(new FlinkKafkaConsumer<String>("send", new SimpleStringSchema(), properties));

        // 2. 使用flink进行转换处理
        SingleOutputStreamOperator<String> mapOperator = stream.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                String[] split = value.split(",");
                return new Event(split[0].trim(), split[1].trim(), Long.valueOf(split[2])).toString();
            }
        });

        // 3. 将结果写入到kafka中
        // 第三个参数表示数据流到kafka的序列化器
        stream.addSink(new FlinkKafkaProducer<String>("localhost:9092", "receive", new SimpleStringSchema()));
        env.execute();
    }
}
