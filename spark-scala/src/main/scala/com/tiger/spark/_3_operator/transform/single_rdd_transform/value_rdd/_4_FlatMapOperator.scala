package com.tiger.spark._3_operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _4_FlatMapOperator {

  @Test
  def map(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    // 设置分区数为1, 发现需要所有的算子处理完一个数据才会开始处理下一个数据
    val list: RDD[String] = context.makeRDD(List("hello world", "hello spark"), 1)

    // 需要传入T => TraversableOnce[U]类型的函数
    val result: RDD[String] = list.flatMap((x: String) => {
      x.split(" ")
    })

    result.collect().foreach(println)

    context.stop()


  }
}
