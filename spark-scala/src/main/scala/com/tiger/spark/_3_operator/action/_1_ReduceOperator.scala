package com.tiger.spark._3_operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _1_ReduceOperator {

  @Test
  def reduce(): Unit ={
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    // 设置分区数为1, 发现需要所有的算子处理完一个数据才会开始处理下一个数据
    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 1)

    val i: Int = list.reduce(_ + _)
    println(s"sum is $i")
    context.stop()
  }

}
