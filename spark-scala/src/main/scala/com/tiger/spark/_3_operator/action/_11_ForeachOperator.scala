package com.tiger.spark._3_operator.action

import org.apache.spark.{SparkConf, SparkContext}

class _11_ForeachOperator {

  def foreach(): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1,2,3,4))

    // foreach 其实是Executor端内存数据打印
    rdd.foreach(println)
    sc.stop()
  }
}
