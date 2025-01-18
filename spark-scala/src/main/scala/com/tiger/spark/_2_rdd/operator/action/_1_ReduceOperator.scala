package com.tiger.spark._2_rdd.operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _1_ReduceOperator {

  @Test
  def reduce(): Unit ={
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 1)

    val i: Int = list.reduce(_ + _)
    println(s"sum is $i")
    context.stop()
  }

}
