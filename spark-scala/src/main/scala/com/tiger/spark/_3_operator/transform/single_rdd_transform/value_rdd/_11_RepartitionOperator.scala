package com.tiger.spark._3_operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _11_RepartitionOperator {


  @Test
  def coalesce(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)


    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4, 5, 6), 4)

    // 底层调用的就是coalesce(numPartitions, shuffle = true)
    // 所以效果可以参考coalesce
    val value: RDD[Int] = list.repartition(2)

    context.stop()
  }
}
