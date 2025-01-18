package com.tiger.spark._5_streaming._2_operations

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.junit.Test

import scala.collection.mutable

/**
 * @author Tiger.Shen
 * @date 2024/7/11
 * @description
 * @version 1.0
 */
class _02_Window {

  @Test
  def window(): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("RDDStream")
    val ssc = new StreamingContext(conf, Seconds(4))

    // 状态的保存, 需要设置checkpoint地址, 一般设置为hdfs
    ssc.checkpoint("cp")

    val rddQueue1 = new mutable.Queue[RDD[String]]()
    val inputStream1: InputDStream[String] = ssc.queueStream(rddQueue1, oneAtATime = true)

    val mapDStream: DStream[(String, Int)] = inputStream1.flatMap(str => str.split(","))
      .map(word => (word, 1))


    // 计算过去两个RDD的wordCount, 每个4秒计算一次
    val window: DStream[(String, Int)] = mapDStream.window(Seconds(8), Seconds(4))
    window.reduceByKey((v1, v2) => v1 + v2).print()

    // countByWindow就是先window, 然后执行count
    val countDStream: DStream[Long] = mapDStream.countByWindow(Seconds(8), Seconds(4))
    countDStream.print()

    // countByValueAndWindow就是先window, 然后执行countByValue, countByValue就是count(distinct)
    val countByValueAndWindow: DStream[((String, Int), Long)] = mapDStream.countByValueAndWindow(Seconds(8), Seconds(4))
    countByValueAndWindow.print()

    // reduceByWindow就是先window, 然后执行reduce
    val reduceByWindow: DStream[(String, Int)] = mapDStream.reduceByWindow((v1, v2) => (v1._1, v1._2 + v2._2), Seconds(8), Seconds(4))
    reduceByWindow.print()

    // groupByKeyAndWindow就是先window, 然后执行groupByKey
    val groupByKeyAndWindow: DStream[(String, Iterable[Int])] = mapDStream.groupByKeyAndWindow(Seconds(8), Seconds(4))
    groupByKeyAndWindow.print()

    // reduceByKeyAndWindow就是先window, 然后执行reduceByKey
    val reduceByKeyAndWindow: DStream[(String, Int)] = mapDStream.reduceByKeyAndWindow((v1, v2) => v1 + v2, Seconds(8), Seconds(4))
    reduceByKeyAndWindow.print()

    ssc.start()
    // 开始生成数据
    new DataGenetor(rddQueue1, ssc).start()

    ssc.awaitTermination()
  }

  @Test
  def reduceByKeyAndWindow(): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("RDDStream")
    val ssc = new StreamingContext(conf, Seconds(4))

    // 状态的保存, 需要设置checkpoint地址, 一般设置为hdfs
    ssc.checkpoint("cp")

    val rddQueue1 = new mutable.Queue[RDD[String]]()
    val inputStream1: InputDStream[String] = ssc.queueStream(rddQueue1, oneAtATime = true)

    val mapDStream: DStream[(String, Int)] = inputStream1.flatMap(str => str.split(","))
      .map(word => (word, 1))

    // reduceByKeyAndWindow
    // 第一个函数是对新的RDD进行聚合, 第二个函数是将结果减去旧的RDD的结果
    // 这个函数可以提高性能
    mapDStream.reduceByKeyAndWindow((v1, v2) => v1 + v2, (v1, v2) => v1 - v2, Seconds(8), Seconds(4))

    ssc.start()

    // 开始生成数据
    new DataGenetor(rddQueue1, ssc).start()
    ssc.awaitTermination()
  }

}
