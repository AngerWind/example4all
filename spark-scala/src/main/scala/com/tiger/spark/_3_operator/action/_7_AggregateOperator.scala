package com.tiger.spark._3_operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _7_AggregateOperator {

  @Test
  def aggregate(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    val list1: RDD[Int] = context.makeRDD(List(1, 2, 3, 4, 5, 6), 2)

    /*
     传入三个参数
     1. 聚合的初始值, 这个初始值会被应用于区间内聚合和区间之间聚合!!!!!!
     2. 分区内聚合的函数 (U, T) => U, 说明可以改变类型
     3. 分区间聚合的函数 (U, U) => U, 不能改变类型
     返回的结果为||456|123     或者||123|456
     结果不一定, 因为有两个分区, 看哪个分区先分区内聚合完谁就在前面
     */
    val str: String = list1.map((int: Int) => int.toString)
      .aggregate("|")(_ + _, _ + _)

    println(str)
  }

}
