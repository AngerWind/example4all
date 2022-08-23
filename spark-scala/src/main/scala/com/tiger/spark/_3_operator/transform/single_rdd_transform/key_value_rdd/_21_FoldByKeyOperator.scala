package com.tiger.spark._3_operator.transform.single_rdd_transform.key_value_rdd

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.junit.Test

class _21_FoldByKeyOperator {

  @Test
  def groupByKey(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    val list1: RDD[(String, Int)] = context.makeRDD(List(("a", 1), ("a", 2), ("a", 3), ("b", 4)), 2)

    /**
     * 如果aggregateByKey的区间内聚合函数与区间之间的聚合函数相同
     * 可以使用foldByKey
     */
    val foldPartition: (Int, Int) => Int = _+_
    val result: RDD[(String, Int)] = list1.foldByKey(0)(foldPartition)

    result.collect().foreach(println)

    context.stop()
  }
}
