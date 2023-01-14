package com.tiger.spark._2_rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _4_FirstOperator {

  @Test
  def first(): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1,2,3,4))

    // first : 获取数据源中数据的第一个
    val first = rdd.first()
    println(first)

    sc.stop()
  }

}
