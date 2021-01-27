package com.tiger.kafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


import java.util.Arrays;
import java.util.Properties;

public class Consumer {



    private static final String TOPIC = "test.tiger";
    private static final String BROKER_LIST = "10.2.0.211:9092,10.2.0.212:9092,10.2.0.214:9092";
    private static KafkaConsumer<String, String> consumer = null;

    static {
        Properties configs = initConfig();
        consumer = new KafkaConsumer<String, String>(configs);
        consumer.subscribe(Arrays.asList(TOPIC));
    }

    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", BROKER_LIST);
        //
        properties.put("group.id", "4512");

        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.setProperty("enable.auto.commit", "true");
//        properties.setProperty("auto.offset.reset", "earliest");
        return properties;
    }

    public static void main(String[] args) {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf(record.value());
                System.out.println();
            }
        }
    }
}
