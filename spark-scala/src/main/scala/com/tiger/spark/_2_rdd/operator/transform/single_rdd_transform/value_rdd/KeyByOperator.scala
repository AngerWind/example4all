package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class KeyByOperator {

  @Test
  def coalesce(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)


    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4, 5, 6), 4)

    // 将value 转换为 key, value的形式, 类似于map的操作
    // 这个操作不会导致shuffle, 只是将一个value类型转换为一个key-value类型, 而key-value的本质就是二元组
    // _*2用于根据指定的value生成对应的key
    val value: RDD[(Int, Int)] = list.keyBy(_ * 2)

    context.stop()
  }

}
