package com.tiger.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @author Tiger.Shen
 * @date 2020/8/29 21:52
 */
public class CounterInterceptor implements ProducerInterceptor {

    int fail;

    int success;
    public ProducerRecord onSend(ProducerRecord record) {
        return record;
    }

    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            fail++;
        } else {
            success++;
        }
    }

    public void close() {
        System.out.print(String.format("%d--%d", success, fail));
    }

    public void configure(Map<String, ?> configs) {

    }
}
