package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.key_value_rdd

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.junit.Test

class _23_SortByKeyOperator {

  @Test
  def combineByKey(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    val list1: RDD[(String, Int)] = context.makeRDD(List(("a", 1), ("a", 2), ("a", 3), ("b", 4)), 2)

    // 按照字典序降序排列
    val result: RDD[(String, Int)] = list1.sortByKey(ascending = false)
    result.collect().foreach(println)

    context.stop()
  }
}
