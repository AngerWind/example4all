package com.tiger.kafka2springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * count： 创建的节点数, 默认1
 * ports： 节点的端口, 默认随机端口, 启动时日志打印具体端口号
 */
@EmbeddedKafka(count = 4,ports = {9092,9093,9094,9095})
@SpringBootApplication
@RestController
@Slf4j
public class Kafka2springbootApplication {


    @Bean
    public KafkaAdmin admin(KafkaProperties properties){
        KafkaAdmin admin = new KafkaAdmin(properties.buildAdminProperties());
        admin.setAutoCreate(true);
        admin.setFatalIfBrokerNotAvailable(true);
        return admin;
    }
    public static void main(String[] args) {

        SpringApplication.run(Kafka2springbootApplication.class, args);
    }

    @Autowired
    private KafkaTemplate<Object, Object> template;


    @GetMapping("/send/{input}")
    public void sendFoo(@PathVariable String input) {
        ListenableFuture<SendResult<Object, Object>> sendResult = this.template.send("topic_input", input);
        try {
            SendResult<Object, Object> objectObjectSendResult = sendResult.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(id = "webGroup", topics = "topic_input")
    public void listen(String input) {
        log.info("1input value: {}" , input);
    }


}
