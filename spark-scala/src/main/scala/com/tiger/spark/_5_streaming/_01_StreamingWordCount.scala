package com.tiger.spark._5_streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @author Tiger.Shen
 * @version 1.0
 */
object _01_StreamingWordCount {

  def main(args: Array[String]): Unit = {

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

    //启动 SparkStreamingContext, 并等待关闭
    ssc.start()
    ssc.awaitTermination()

  }

}
