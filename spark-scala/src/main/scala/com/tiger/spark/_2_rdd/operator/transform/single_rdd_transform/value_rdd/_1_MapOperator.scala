package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.value_rdd

import java.util
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

import scala.collection.mutable.ListBuffer
import scala.util.Random

class _1_MapOperator {

  @Test
  def map(): Unit = {
    val sparkConf: SparkConf = new SparkConf()
    //.setMaster("spark://192.168.31.8:7077")
      .setMaster("local[*]")
      .setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    // 设置分区数为1, 发现需要所有的算子处理完一个数据才会开始处理下一个数据
    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 1)

    // 需要传入T => U类型函数
    val result: RDD[Int] = list
      .map((x: Int) => {
        println(s"步骤1处理数据: $x")
        x
      })
      .map((x: Int) => {
        println(s"步骤2处理数据: $x")
        x
      })

    result.collect().foreach(println)
    context.stop()
  }
}
