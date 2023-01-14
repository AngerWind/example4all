package com.tiger.spark._2_rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _8_FoldOperator {

  @Test
  def fold(): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1,2,3,4),2)


    // aggregateByKey : 初始值只会参与分区内计算
    // aggregate : 初始值会参与分区内计算,并且和参与分区间计算
    val result = rdd.fold(10)(_+_)

    println(result)

    sc.stop()
  }

}
