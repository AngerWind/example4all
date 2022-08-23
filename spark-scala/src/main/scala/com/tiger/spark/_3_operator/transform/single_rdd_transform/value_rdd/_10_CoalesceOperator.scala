package com.tiger.spark._3_operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test


class _10_CoalesceOperator {

  @Test
  def coalesce(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)


    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4, 5, 6), 4)

    /**
     * coalesce的作用是调整分区, 不仅可以缩小也可以放大,
     * 如果是放大的话, 不进行shuffle是无效的
     */
    // 使用coalesce将上面四个分区缩减为2个分区, 默认不进行shuffle
    // 即之前同一个分区中的数据进行coalesce之后还是在一个分区中
    val value: RDD[Int] = list.coalesce(2)

    // 进行shuffle, 所有分区中的数据打乱重新进行分区
    val value1: RDD[Int] = list.coalesce(2, shuffle = true)

    context.stop()
  }

}
