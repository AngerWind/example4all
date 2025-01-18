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
class _03_Output {

  @Test
  def output(): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("RDDStream")
    val ssc = new StreamingContext(conf, Seconds(4))
    // 状态的保存, 需要设置checkpoint地址, 一般设置为hdfs
    ssc.checkpoint("cp")

    val rddQueue1 = new mutable.Queue[RDD[String]]()
    val inputStream1: InputDStream[String] = ssc.queueStream(rddQueue1, oneAtATime = true)


    // 计算出当前批次数据的wordcount
    val wordcountPerBatch: DStream[(String, Int)] = inputStream1.flatMap(str => str.split(","))
      .map(word => (word, 1))
      .reduceByKey((v1, v2) => v1 + v2)

    // 当前批次的wordcount和历史数据的wordcount进行汇总集合
    def updateStateFunc(values: Seq[Int], old: Option[Int]): Option[Int] = {
      // 每个key的count, 和之前的count进行聚合
      val newValue = values.sum + old.getOrElse(0)
      Some(newValue)
    }

    val result: DStream[(String, Int)] = wordcountPerBatch.updateStateByKey(updateStateFunc)

    /**
     * 对聚合的结果进行输出
     */
    // 打印到控制台
    result.print()
    // 以文本文件的形式, 保存到checkpoint, 文件名为: prefix-TIME_IN_MS[.suffix]
    result.saveAsTextFiles("text", "txt")
    // 以对象的形式, 保存到checkpoint, 文件名为: prefix-TIME_IN_MS[.suffix]
    result.saveAsObjectFiles("obj", "obj")
    // 以hadoop file的形式, 保存到checkpoint, 文件名为: prefix-TIME_IN_MS[.suffix]
    result.saveAsObjectFiles("hadoop")

    ssc.start()
    // 开始生成数据
    new DataGenetor(rddQueue1, ssc).start()

    ssc.awaitTermination()
  }

  @Test
  def foreachRDD(): Unit = {

  }

}
