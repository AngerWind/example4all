package com.tiger.spark._2_rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _2_CollectOperator {

  @Test
  def collect(): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1,2,3,4))


    // collect方法会将不同分区的数据按照分区顺序采集到Driver端内存中，形成数组
    val ints: Array[Int] = rdd.collect()
    println(ints.mkString(","))

    sc.stop()
  }
}
