package com.tiger.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Properties;

public class Producer {

    static Logger log = Logger.getLogger(Producer.class);

    private static final String TOPIC = "jrocket.fat.incr.public.loan_applications";
    private static final String BROKER_LIST = "10.2.0.15:9092";
    private static KafkaProducer<String, String> producer = null;

    /*
    初始化生产者
     */
    static {
        Properties configs = initConfig();
        producer = new KafkaProducer<String, String>(configs);
    }

    /*
    初始化配置
     */
    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public static void main(String[] args) {
        //消息实体
        ProducerRecord<String, String> record = null;
        for (int i = 0; i < 10; i++) {
            String message = "hello kafka message: " + i;
            record = new ProducerRecord<String, String>(TOPIC, String.valueOf(i), message);
            producer.send(record);
            System.out.println(message);
            // 发送消息并异步接收消息确认
//            producer.send(record, new Callback() {
//                @Override public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                    if (null != e) {
//                        log.info("send error" + e.getMessage());
//                    } else {
//                        System.out.println(String
//                            .format("offset:%s,partition:%s", recordMetadata.offset(), recordMetadata.partition()));
//                    }
//                }
//            });
        }
        producer.close();
    }
}
