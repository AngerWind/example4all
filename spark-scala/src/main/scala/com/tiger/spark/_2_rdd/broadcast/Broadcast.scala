package com.tiger.spark._2_rdd.broadcast

import org.apache.spark.{SparkConf, SparkContext, broadcast}
import org.junit.Test

/**
  * @author Tiger.Shen
  * @date 2024/7/7
  * @description
  * @version 1.0
  */
class Broadcast {

  @Test
  def broadcast(): Unit = {

    val sparkConf: SparkConf =
      new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    val rdd1 = context.makeRDD(List(("a", 1), ("b", 2), ("c", 3), ("d", 4)), 4)
    val adder: Int = 10


    // 声明广播变量
    context.broadcast(adder)

    rdd1.map{
      case (key, value) => (key, value + adder)
    }.collect().foreach(println)

    context.stop()

  }

}
