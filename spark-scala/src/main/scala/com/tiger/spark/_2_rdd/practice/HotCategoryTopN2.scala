package com.tiger.spark._2_rdd.practice

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

/**
 * 一次性统计每个品类的点击数, 下单数, 支付数
 */
class HotCategoryTopN2 {

  /**
   * HotCategoryTopN1存在的问题:
   *  1. 分开计算了三个指标, 每个指标都使用了一次reduceByKey
   *  2. 合并指标的时候又使用了一次reduceByKey
   *
   * 优化: 一次性统计三个指标, 只使用一次reduceByKey
   */
  @Test
  def practice(): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("aaa").setMaster("local[*]")
    val context: SparkContext = new SparkContext(conf)

    // 每一条数据都转换为(品类, (点击数, 下单数, 支付数))
    val result: Array[(String, (Int, Int, Int))] = context.textFile("input/user_visit_action.csv")
      .flatMap(str => {
        val strings: Array[String] = str.split(", ")
        // 点击数据
        if (strings(6) != "-1" && strings(7) != "-1") {
          List((strings(6), (1, 0, 0)))
        }
        // 下单数据
        else if (strings(8) != "" && strings(9) != "") {
          strings(8).split("-").map(id => (id, (0, 1, 0)))
        }
        // 支付数据
        else if (strings(10) != "" && strings(11) != "") {
          strings(10).split("-").map(id => (id, (0, 0, 1)))
        }
        // 搜索数据
        else {
          Nil
        }
      })
      .reduceByKey((count1, count2) => {
        (count1._1 + count2._1, count1._2 + count2._2, count1._3 + count2._3)
      })
      .sortBy(_._2, ascending = false)
      .take(10)

    result.foreach(println)

    context.stop()
  }


}
