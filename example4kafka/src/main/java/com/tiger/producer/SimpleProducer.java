package com.tiger.producer;


import com.tiger.interceptor.CounterInterceptor;
import com.tiger.interceptor.TimeIntercepter;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author Tiger.Shen
 * @date 2020/8/24 21:58
 */

@Repository
@AllArgsConstructor
public class SimpleProducer {

    @Test
    public void produce() {

        // 1. 创建kafka生产者的配置信息
        Properties properties = new Properties();

        // 2. 指定连接的kafka集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
        // 3. ACK应答等级
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        // 4. 重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 5. 批次大小, 16K
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 6. 等待的时间 1ms, 即要么等到16K发送，要么等1ms发送
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // 7. RecordAccumulator缓冲区大小， 32M
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // 8. Key，Value的序列化类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.toString());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.toString());

        // 设置自定义分区策略
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, SimpleProducer.class.toString());

        // 设置kafka拦截器
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, Arrays.asList(
                TimeIntercepter.class.toString(),
                CounterInterceptor.class.toString()));

        // 9. 创建生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

        // 10. 发送数据
        // 不带回调函数的发送方法
        kafkaProducer.send(new ProducerRecord<String, String>("topic", "message"));
        kafkaProducer.send(new ProducerRecord<String, String>("topic", "key", "message"));
        kafkaProducer.send(new ProducerRecord<String, String>("topic", 1, "key", "message"));

        // 带回调函数的方法
        kafkaProducer.send(new ProducerRecord<String, String>("topic", "message"), new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (metadata != null) {
                    System.out.println(metadata.offset());
                    System.out.println(metadata.partition());
                    System.out.println(metadata.timestamp());
                } else {
                    System.out.println(exception.getStackTrace());
                }
            }
        });

        // 11.  关闭连接
        kafkaProducer.close();
    }

}
