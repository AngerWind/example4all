package com.example.example4rocketmq.producer;

import com.google.common.base.Strings;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import  org.junit.jupiter.api.*;
import org.junit.platform.commons.annotation.Testable;
import org.rocksdb.Transaction;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/6/6
 * @description
 */
public class SyncProducerTest {

    DefaultMQProducer producer;

    @BeforeEach
    public void init () throws MQClientException {
        producer = new DefaultMQProducer();
        // 指定nameserver地址
        producer.setNamesrvAddr("localhost:9876");
        // 设置namespace, 那么对消息发送的topic, 都会修改为namespace%topic的格式
        producer.setNamespaceV2("test");

        // 设置重试
        producer.setRetryTimesWhenSendFailed(3);
        producer.setRetryTimesWhenSendAsyncFailed(3);

        producer.start();
    }

    @AfterEach
    public void close() {
        producer.shutdown();
    }

    @Test
    public void testProducer() throws MQClientException {
        List<MessageQueue> queues = producer.fetchPublishMessageQueues("topic"); // 获取单个topic的所有queue
        MessageQueue queue = queues.get(0);
        int queueId = queue.getQueueId();
        String topic = queue.getTopic();
        String brokerName = queue.getBrokerName();



        long batchMaxBytes = producer.getBatchMaxBytes(); // 获取批量消息的最大限制
        producer.batchMaxBytes();

    }
    
    public List<Message> buildMessage(int num) {
        ArrayList<Message> list = new ArrayList<>(num);
        String topic = "topic1";
        String tag = "tag1";
        int flag = 1;
        boolean waitStoreMsgOk = true;

        for (int i = 0; i < num; i++) {
            String keys = UUID.randomUUID().toString(); // 可以设置唯一id
            byte[] body = ("hello" + i).getBytes();
            list.add(new Message(topic, tag, keys, flag, body, waitStoreMsgOk));
        }

        return list;
    }

    @Test
    public void testSyncProducer() throws Exception {

        Message message = buildMessage(1).get(0);

        // 同步发送单个消息, 指定超时时间3000, 通过MessageQueueSelector指定
        SendResult result = producer.send(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                // 这里的arg就是传入的message.getKeys
                String replaced = ((String)arg).replace("-", "");
                int parseInt = Integer.parseInt(replaced);
                int queueNum = parseInt % mqs.size();
                return mqs.get(queueNum);
            }
        }, message.getKeys(), 3000);

        MessageQueue messageQueue = result.getMessageQueue(); // 消息的队列
        String msgId = result.getMsgId(); // 不保证唯一
        long queueOffset = result.getQueueOffset();
        String transactionId = result.getTransactionId(); // 事务id, 一般针对事务消息
        String offsetMsgId = result.getOffsetMsgId();
        String regionId = result.getRegionId();
        boolean traceOn = result.isTraceOn();


        if (result.getSendStatus().equals(SendStatus.SEND_OK)) {
            System.out.println("发送成功");
        } else if (result.getSendStatus().equals(SendStatus.FLUSH_DISK_TIMEOUT)){
            System.out.println("刷盘超时");
        } else if (result.getSendStatus().equals(SendStatus.FLUSH_SLAVE_TIMEOUT)) {
            System.out.println("同步超时");
        } else if (result.getSendStatus().equals(SendStatus.SLAVE_NOT_AVAILABLE)) {
            System.out.println("slave不可用");
        }
    }

    @Test
    public void testAsync() throws Exception {
        List<MessageQueue> queues = producer.fetchPublishMessageQueues("topic1");
        // 异步发送, 批量消息, 超时时间为3000, 通过MessageQueue直接指定要发送的queue
        producer.send(buildMessage(3), queues.get(0),  new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("发送失败");
            }
        }, 3000);
    }
}
