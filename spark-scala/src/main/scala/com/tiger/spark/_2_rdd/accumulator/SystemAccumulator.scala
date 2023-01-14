package com.tiger.spark._2_rdd.accumulator

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

object SystemAccumulator {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("xxx")
    val context: SparkContext = SparkContext.getOrCreate(conf)

    val rdd: RDD[Int] = context.makeRDD(List(1, 2, 3, 4, 5, 6), 2)

    // 获取系统累加器
    val cnt: LongAccumulator = context.longAccumulator("cnt")
    // 累加器进行聚合
    rdd.foreach(x => cnt.add(x))

    // 获取累加器的值
    println(cnt.value)

    context.stop()
  }

}
