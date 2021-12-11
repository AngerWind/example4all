package com.tiger.kafka;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Consumer
 * @date 2021/8/31 10:57
 * @description
 */
public class Consumer {



    private static final String TOPIC = "alarm_center_dev";
    private static final String BROKER_LIST = "10.2.0.213:9092,10.2.0.212:9092,10.2.0.214:9092";
    private static KafkaConsumer<String, String> consumer = null;

    static {
        Properties configs = initConfig();
        consumer = new KafkaConsumer<String, String>(configs);
        consumer.subscribe(Arrays.asList(TOPIC));

        LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
        lc.getLogger("org.apache.kafka").setLevel(Level.INFO);
    }

    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", BROKER_LIST);
        //
        properties.put("group.id", "1521562116516523");

        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        /**
         * auto.offset.reset :
         * earliest
         * 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
         * latest
         * 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
         * none
         * topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
         */
//        properties.setProperty("enable.auto.commit", "true");
        properties.setProperty("auto.offset.reset", "latest");
        return properties;
    }

    public static void main(String[] args) {
        while (true) {
            int count = 0;
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                if (record.value().contains("$")) {
                    System.out.println(record.value());
                    count++;
                }
                if (count == 2000) {
                    System.out.println("2000");
                    count = 0;
                }
            }
        }
    }
}
