package com.tiger.spark._3_operator.action

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _5_TakeOperator {

  @Test
  def take(): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1,2,3,4), 2)

    // 获取前三个数据
    val ints: Array[Int] = rdd.take(3)
    println(ints.mkString(","))

    sc.stop()
  }

}
