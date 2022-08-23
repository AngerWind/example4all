package com.tiger.spark._3_operator.transform.double_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _16_ZipOperator {

  @Test
  def subtract(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)


    val list1: RDD[Int] = context.makeRDD(List(1, 2, 3, 4, 5, 6), 2)
    val list2: RDD[String] = context.makeRDD(List("3", "4", "5", "7", "8", "9"), 2)

    // 拉链, 两个rdd中元素位置相同的组成一个tuple
    // 两个rdd中分区数相同, 分区中元素个数需要相同
    // 两个rdd可以不同类型
    val result: RDD[(Int, String)] = list1.zip(list2)

    result.collect().foreach(println)

    context.stop()
  }
}
