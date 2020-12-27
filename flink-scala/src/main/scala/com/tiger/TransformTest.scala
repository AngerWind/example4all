package com.tiger

import org.apache.flink.streaming.api.scala._

/**
 * @author Tiger.Shen
 * @date 2020/12/26 19:53
 */
object TransformTest {

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

    // 简单操作: 分组， 聚合，输出每个传感器当前最低温度
    val simpleStream = dataStream.keyBy("id") // 根据id进行分组
      .minBy("temperature")
    simpleStream.print()

    // reduce操作: 输出当前最小的温度值和最后一个数据的时间戳
    val resultStream = dataStream
      .keyBy("id")
      .reduce((curData, nextData) => SensorReading(curData.id, nextData.timestamp, curData.temperature.min(nextData.temperature)))
    resultStream.print()

    // split操作: 将传感器温度数据分成低温、高温两条流
    val splitStream = dataStream
        .split( data => {
          if (data.temperature > 30.0) Seq("high") else Seq("low")
        })
    val hightStream = splitStream.select("high")
    val lowStream = splitStream.select("low")
    val allStream = splitStream.select("high", "low")


    // connect合流操作：合流的两个数据类型可以不一致， 合流之后的数据类型是他们的父类
    // 合流之后虽然是一条流，但是内部还是分开的，类似一国两制，需要通过map方法合并成真正的一条流
    val warningStream = hightStream.map( dataStream => (dataStream.id, dataStream.temperature))
    val connectedStream = lowStream.connect(warningStream).map( data1 => (data1.id, "health"), data2 => (data2._1, data2._2, "warnning"))


    // union合流操作, 两条流需要数据类型一致， union的参数可以是多个
    val unionStream = hightStream.union(lowStream)


    env.execute()

  }

}
