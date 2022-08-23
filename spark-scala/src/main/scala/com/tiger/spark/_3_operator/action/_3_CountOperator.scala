package com.tiger.spark._3_operator.action

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _3_CountOperator {

  @Test
  def count(): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1,2,3,4))

    // count : 数据源中数据的个数
    val cnt = rdd.count()
    println(cnt)

    sc.stop()
  }

}
