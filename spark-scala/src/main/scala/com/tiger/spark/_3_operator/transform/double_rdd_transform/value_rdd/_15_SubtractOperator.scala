package com.tiger.spark._3_operator.transform.double_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _15_SubtractOperator {

  @Test
  def subtract(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)


    val list1: RDD[Int] = context.makeRDD(List(1, 2, 3, 4, 5, 6), 4)
    val list2: RDD[Int] = context.makeRDD(List(3, 4, 5, 6, 7), 4)

    // 求差集, 两个rdd的类型必须一致
    val result: RDD[Int] = list1.union(list2)

    result.collect().foreach(println)

    context.stop()
  }
}
