package com.tiger.spark._5_streaming._1_create

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

/**
 * @author Tiger.Shen
 * @date 2024/7/10
 * @description
 * @version 1.0
 */
object _01_CreateByQueue {


  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("RDDStream")
    val ssc = new StreamingContext(conf, Seconds(4))

    // 创建 RDD 队列
    val rddQueue = new mutable.Queue[RDD[Int]]()
    // 创建 QueueInputDStream, 用于保存RDD
    val inputStream: InputDStream[Int] = ssc.queueStream(rddQueue, oneAtATime = false)
    // 处理队列中的 RDD 数据
    val mappedStream: DStream[(Int, Int)] = inputStream.map((_, 1))
    val reducedStream: DStream[(Int, Int)] = mappedStream.reduceByKey(_ + _)
    // 打印结果
    reducedStream.print()
    // 启动任务
    ssc.start()


    // 循环创建并向 RDD 队列中放入 RDD
    for (i <- 1 to 5) {
      // 创建RDD并放入队列中, 让SparkStreaming读取数据
      rddQueue += ssc.sparkContext.makeRDD(1 to 300, 10)
      Thread.sleep(2000)
    }

    ssc.awaitTermination()
  }


}
