package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

class _7_FilterOperator {

  def filter(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)


    val list: RDD[String] = context.makeRDD(List("hello world", "hello spark"), 1)

    // 过滤数据
    list.filter(_.startsWith("hello")).collect()
      .foreach(println)

    context.stop()
  }

}
