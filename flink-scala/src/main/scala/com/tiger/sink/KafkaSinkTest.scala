package com.tiger.sink

import com.tiger.SensorReading
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011


/**
 * @author Tiger.Shen
 * @date 2020/12/27 16:17
 */
object KafkaSinkTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // 从文件中读取
    val inputPath = "src\\main\\resources\\sensor.txt"
    val stream2 = env.readTextFile(inputPath)

    val dataStream = stream2
      .map(data => {
        val arr = data.split(",")
        SensorReading(arr(0), arr(1).toLong, arr(2).toDouble)
      })

    val flinkKafkaProducer: FlinkKafkaProducer011[SensorReading]
    = new FlinkKafkaProducer011[SensorReading](
      "localhost:9092",
      "topicName",
      (element: SensorReading) => element.toString().getBytes()
    )


    dataStream.addSink(flinkKafkaProducer)

    env.execute()

  }

}
