package com.tiger.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * 自定义拦截器
 * @author Tiger.Shen
 * @date 2020/8/29 21:51
 */
public class TimeIntercepter implements ProducerInterceptor<String, String> {

    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        // 在消息体前面添加timestamp
        return new ProducerRecord<String, String>(
                record.topic(),
                record.partition(),
                record.key(),
                System.currentTimeMillis() + record.value());
    }

    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    public void close() {

    }

    public void configure(Map<String, ?> configs) {

    }
}
