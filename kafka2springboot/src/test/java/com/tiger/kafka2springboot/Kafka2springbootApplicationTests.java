package com.tiger.kafka2springboot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.io.IOException;


@Slf4j
@SpringBootTest
@EmbeddedKafka(count = 4, ports = {9092, 9093, 9094, 9095})
class Kafka2springbootApplicationTests {


    @Autowired
    KafkaTemplate<Object, Object> kafkaTemplate;

    @KafkaListener(id = "webGroup1", topics = "topic_input")
    public void listen(String input) {
        log.info("input value: {}" , input);
    }

    @Test
    void contextLoads() throws IOException {
        kafkaTemplate.send("2topic_input", "hello world");
        System.in.read();
    }

}
