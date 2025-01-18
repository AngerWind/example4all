package com.tiger.spark._5_streaming._1_create

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @author Tiger.Shen
 * @date 2024/7/10
 * @description
 * @version 1.0
 */
object _04_CreateByKafka {

  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setAppName("ReceiverWordCount").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(3))
    // 定义 Kafka 参数
    val kafkaPara: Map[String, Object] = Map[String, Object](
      // 指定kafka的集群地址
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "linux1:9092,linux2:9092,linux3:9092",
      // 指定consumer group id
      ConsumerConfig.GROUP_ID_CONFIG -> "atguigu",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
    )
    // 读取 Kafka 数据创建 DStream
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] =
      KafkaUtils.createDirectStream[String, String](ssc,
        LocationStrategies.PreferConsistent,
        // 指定kafka的topic
        ConsumerStrategies.Subscribe[String, String](Set("atguigu"), kafkaPara))
    // 将每条消息的 KV 取出
    val valueDStream: DStream[String] = kafkaDStream.map(record => record.value())
    // 计算 WordCount
    valueDStream.flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .print()

    //7.开启任务
    ssc.start()
    ssc.awaitTermination()
  }

}
