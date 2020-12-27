package com.tiger.sink

import com.tiger.SensorReading
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

/**
 * @author Tiger.Shen
 * @date 2020/12/27 16:54
 */
object RedisSinkTest {

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

    // 定义一个FlinkJedisConfigBase
    val conf = new FlinkJedisPoolConfig.Builder()
      .setHost("localhost")
      .setPort(6379)
      .build()
    dataStream.addSink(new RedisSink[SensorReading](conf, new MyRedisMapper))
    env.execute()

  }
}

// 定义一个RedisMapper
class MyRedisMapper extends RedisMapper[SensorReading]{
  // 定义保存数据写入redis的命令，HSET 表名 key value
  override def getCommandDescription: RedisCommandDescription = {
    new RedisCommandDescription(RedisCommand.HSET, "sensor_temp")
  }

  // 将温度值指定为value
  override def getValueFromData(data: SensorReading): String = data.temperature.toString

  // 将id指定为key
  override def getKeyFromData(data: SensorReading): String = data.id
}
