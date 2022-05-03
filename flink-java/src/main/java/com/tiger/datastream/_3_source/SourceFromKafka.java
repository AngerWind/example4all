package com.tiger.datastream._3_source;

import lombok.SneakyThrows;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.junit.Test;

import java.util.Properties;

public class SourceFromKafka {

    @SneakyThrows
    @Test
    public void sourceFromKafka() {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        // 指定kafka地址
        properties.setProperty("bootstrap.servers", "hadoop102:9092");
        // 指定消费者组
        properties.setProperty("group.id", "consumer-group");
        // 指定offset
        properties.setProperty("auto.offset.reset", "latest");

        // 第一个参数是指定的topic, 也可以是一个正则表达式, 或者一个list
        // 当从多个 topic 中读取数据时，Kafka 连接器将会处理所有 topic 的分区，将这些分区的数据放到一条流中去。
        // 第二个参数指定了如何将从kafka中接收到的字节数组转换为java/scala中的类
        // 第三个参数指定了kafka的一些其他属性
        DataStreamSource<String> stream =
                env.addSource(new FlinkKafkaConsumer<String>("clicks", new SimpleStringSchema(), properties));

    }
}
