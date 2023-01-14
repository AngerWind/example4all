package com.tiger.spark._2_rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}

class _9_CountByKeyOperator {

  def countByKey(): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(("a", 1),("a", 2),("a", 3)))

    val stringToLong: collection.Map[String, Long] = rdd.countByKey()
    println(stringToLong)

    sc.stop()
  }
}
