package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _9_DistinctOperator {

  @Test
  def distinct(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)


    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 2)

    // map(x => (x, null)).reduceByKey((x, _) => x, numPartitions).map(_._1)
    // 上面是distinct的原理, 先reduce?????, reduce不会导致重分区吗
    val value: RDD[Int] = list.distinct()

    context.stop()
  }

}
