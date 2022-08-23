package com.tiger.spark._3_operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _2_MapPartitionOperator {

  /**
   * mapPartition因为会将一个分区的数据发送给算子, 所以比较占用内存
   */
  @Test
  def mapPartition(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 1)
    // mapPartitions将一个分区中的所有数据通过Iterator传入
    // 返回出去的也要是一个Iterator
    // 需要传入一个Iterator[T] => Iterator[U]类型的函数
    val result: RDD[Int] = list.map((x: Int) => {
      println(s"步骤1处理数据: $x")
      x
    }).map((x: Int) => {
      println(s"步骤2处理数据: $x")
      x
    }).mapPartitions((itor: Iterator[Int]) => {
      // 保留条件为真的数据
      println("map partition 开始")
      val iterator: Iterator[Int] = itor.map(_ * 2).filter(_ > 4).iterator
      iterator.foreach(print)
      println("map partition 结束")
      iterator
    })
    result.collect().foreach(println)

    context.stop()
  }
}
