package com.tiger.spark._5_streaming._2_operations

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.junit.Test

import java.lang.Thread

/**
 * @author Tiger.Shen
 * @date 2024/7/11
 * @description
 * @version 1.0
 */
class _04_StopAndRestart {

  @Test
  def stopAndRestart(): Unit = {

    // 从检查点恢复, 或者重新创建
    // 需要把所有的操作都放在创建ssc的函数中
    val ssc = StreamingContext.getActiveOrCreate("cp", () => {
      val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("StreamWordCount")
      // 初始化 SparkStreamingContext, 并定义每一批数据的时间
      val ssc = new StreamingContext(sparkConf, Seconds(3))
      // 从Socket中获取数据, 作为数据源, 并创建DStream
      // 可以在控制台执行 nc -lk 9999 来启动一个socket服务
      val lineStreams: ReceiverInputDStream[String] = ssc.socketTextStream("192.168.1.200", 9999)
      // 将每行数据进行拆分，得到单词
      val wordStreams: DStream[String] = lineStreams.flatMap(_.split(" "))
      // 将单词映射成元组（word,1）
      val wordAndOneStreams: DStream[(String, Int)] = wordStreams.map((_, 1))
      // 将相同的单词次数做统计
      val wordAndCountStreams: DStream[(String, Int)] = wordAndOneStreams.reduceByKey(_ + _)
      //打印
      wordAndCountStreams.print()

      ssc
    })
    ssc.checkpoint("cp")

    //启动 SparkStreamingContext, 并等待关闭
    ssc.start()

    new Thread(() => {
      // 监听外部程序, 比如zookeeper, redis等
      // 如果监听到确切的信号, 就关闭
      // stop接受两个参数
      // 1. stopSparkContext: 关闭spark streaming context的同时是否关闭spark context
      // 2. stopGracefully: 是否优雅的关闭
      Thread.sleep(10000)
      ssc.stop(true, true)
    }).start()
    ssc.awaitTermination()
  }
}
