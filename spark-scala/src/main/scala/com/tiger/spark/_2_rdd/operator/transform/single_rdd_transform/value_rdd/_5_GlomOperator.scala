package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _5_GlomOperator {

  @Test
  def glom(): Unit ={
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)


    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 2)

    // 需求: 同一个分区取最大值, 不同分区所有最大值求和

    // glom前后数据的分区不变, 也就是说glom只是将同一个分区中的数据进行合并, 但是并不会改变分区数
    // 这个函数会将分区中的所有数据都加载到内存中来
    val glom: RDD[Array[Int]] = list.glom()

    val max: RDD[Int] = glom.map(_.max)
    val sum: Int = max.collect().sum
    println(sum)

    context.stop()
  }
}
