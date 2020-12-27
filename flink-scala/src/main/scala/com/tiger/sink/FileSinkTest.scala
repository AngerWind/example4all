package com.tiger.sink

import com.tiger.SensorReading
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.scala._


/**
 * @author Tiger.Shen
 * @date 2020/12/27 15:52
 */
class FileSinkTest {

}

object FileSinkTest {
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

    dataStream.print()
    dataStream.writeAsCsv("src\\main\\resources\\out.txt")

    // 添加文件sink
    val fileSink: StreamingFileSink[SensorReading] = StreamingFileSink.forRowFormat(
      new Path("src\\main\\resources"),
      new SimpleStringEncoder[SensorReading]()
    ).build()
    dataStream.addSink(fileSink)


    env.execute()

  }
}
