package com.tiger.spark._2_rdd.operator.transform.double_rdd_transform.key_value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class SubtractByKeyOperator {

  @Test
  def combineByKey(): Unit = {

    // 初始化 SparkContext
    val conf = new SparkConf().setAppName("SubtractByKey Example").setMaster("local")
    val sc = new SparkContext(conf)

    // 创建两个 PairRDD
    val rdd1 = sc.parallelize(Seq((1, "apple"), (2, "banana"), (3, "cherry")))
    val rdd2 = sc.parallelize(Seq((1, "fruit"), (3, "berry")))

    // 使用 subtractByKey 从 rdd1 中移除在 rdd2 中出现的键
    val result = rdd1.subtractByKey(rdd2)

    // 查看结果
    result.collect().foreach(println) // (2,banana)


    sc.stop()
  }

}
