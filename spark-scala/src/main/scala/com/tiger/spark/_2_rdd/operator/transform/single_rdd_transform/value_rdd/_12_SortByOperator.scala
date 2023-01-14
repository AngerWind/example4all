package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _12_SortByOperator {

  @Test
  def coalesce(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)


    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4, 5, 6), 4)

    // 根据函数返回的key进行排序, 默认为升序
    // 排序的过程存在shuffle
    // 可以改变分区
    list.sortBy((x: Int) => x, ascending = true, 4)

    context.stop()
  }

}
