package com.tiger.spark._2_rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _6_TakeOrderedOperator {

  @Test
  def takeOrdered(): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1,2,3,4))

    // takeOrdered : 数据排序后，取N个数据
    val rdd1 = sc.makeRDD(List(4,2,3,1))
    val ints1: Array[Int] = rdd1.takeOrdered(3)
    println(ints1.mkString(","))

    sc.stop()
  }

}
