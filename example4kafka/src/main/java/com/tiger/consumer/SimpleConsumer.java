package com.tiger.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;

import java.util.Collections;
import java.util.Properties;

/**
 * @author Tiger.Shen
 * @date 2020/8/27 22:40
 */
public class SimpleConsumer {

    /**
     *  offset只在消费者启动的时候从kafka拉取一次，之后会在消费者内存中维护一份，并且将他手动/自动提交到kafka
     */
    @Test
    public void test() {
        Properties properties = new Properties();

        // 1. 设置连接的集群
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // 2. 开启自动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // 3. 自动提交的间隔
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // 4. Key, Value反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.toString());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.toString());

        // 5. 设置消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group_id");


        // 当没有初始的offset（第一次消费时）或者offset对应的数据被删除时（时间过太久，数据被删除）时生效
        // earliest: 将offset重置到最早的地方然后开始消费
        // latest（默认）: 将offset重置到最近的地方开始消费，换句话说只接受启动后生产的消息
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 6. 创建消费者
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);

        // 7. 订阅主题
        kafkaConsumer.subscribe(Collections.singletonList("first topic"));

        // 8. 获取数据, 设置没有拉取到数据的话间隔100ms再请求， 防止空转
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);

        // 9. 解析并打印
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            System.out.println(String.format("%s--%s", consumerRecord.key(), consumerRecord.value()));
        }
        // 10. 关闭连接
        kafkaConsumer.close();

    }
}
