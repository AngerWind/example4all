package com.tiger

import java.util.{Properties, Random}

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.kafka.common.serialization.StringDeserializer

/**
 * @author Tiger.Shen
 * @date 2020/12/26 15:28
 */
case class SensorReading (id: String, timestamp: Long, temperature: Double)

object SourceTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // 从集合中读取数据
    val list = List(
      SensorReading("sensor_1", 1547718199, 35.8),
      SensorReading("sensor_6", 1547718201, 15.4),
      SensorReading("sensor_7", 1547718202, 6.7),
      SensorReading("sensor_10", 1547718205, 38.1)
    )
    val stream1 = env.fromCollection(list)
    stream1.print()

    // 从文件中读取
    val inputPath = "src\\main\\resources\\sensor.txt"
    val stream2 = env.readTextFile(inputPath)
    stream2.print()

    // 从kafka中读取
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "consumer_group")
    properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("auto.offerset.reset", "latest")

    val stream3 = env.addSource(new FlinkKafkaConsumer011[String]("sensor", new SimpleStringSchema(), properties))

    // 自定义source
    val stream4 = env.addSource(new MySensorSource)

    env.execute("source test")

  }
}

class MySensorSource() extends SourceFunction[SensorReading] {

  var running = true
  val rand = new Random()
  var curTemp = (1 to 10).map(i => ("sensor_" + i, rand.nextDouble()))

  override def run(ctx: SourceFunction.SourceContext[SensorReading]): Unit = {
    while (running) {
      curTemp = curTemp.map(
        data => (data._1, data._2 + rand.nextGaussian())
      )
      curTemp.foreach(
        data => ctx.collect(SensorReading(data._1, System.currentTimeMillis(), data._2))
      )

      Thread.sleep(1000)

    }
  }
  override def cancel(): Unit = running = false
}
