package com.tiger.spark._3_operator.practice

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

/**
 * 分开统计每个品类的点击数, 下单数, 支付数
 */
class HotCategoryTopN1 {

  @Test
  def practice(): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("aaa").setMaster("local[*]")
    val context: SparkContext = new SparkContext(conf)

    val textFile: RDD[Array[String]] = context.textFile("input/user_visit_action.csv")
      .map(str => str.split(","))
    textFile.cache()

    // 计算点击数
    val clickCount: RDD[(String, Int)] = textFile.filter(strings => strings(6) != "-1" && strings(7) != "-1")
      .map(strings => (strings(6), 1))
      .reduceByKey(_ + _)

    // 计算下单数
    val orderCount: RDD[(String, Int)] = textFile.filter(strings => strings(8) != "" && strings(9) != "")
      .flatMap(strings => strings(8).split(","))
      .map((_, 1))
      .reduceByKey(_ + _)

    // 计算支付数
    val payCount: RDD[(String, Int)] = textFile.filter(strings => strings(10) != "" && strings(11) != "")
      .flatMap(strings => strings(10).split(","))
      .map((_, 1))
      .reduceByKey(_ + _)

    // 合并结果
    // 下面三种结果都转换为统一的格式: (品类, (点击数, 下单数, 支付数)),  然后进行聚合
    val clickFormated: RDD[(String, (Int, Int, Int))] = clickCount.map({
      case (str, i) => (str, (i, 0, 0))
    })
    val orderFormated: RDD[(String, (Int, Int, Int))] = orderCount.map({
      case (str, i) => (str, (0, i, 0))
    })
    val payFormated: RDD[(String, (Int, Int, Int))] = orderCount.map({
      case (str, i) => (str, (0, 0, i))
    })

    // 结果按照_2进行排序
    // Tuple3的排序方法在scala.math.Ordering.Tuple3Ordering#compare
    // 就是先根据_1排序, _1相同比较_2, _2相同比较_3
    val result: Array[(String, (Int, Int, Int))] = clickFormated
      .union(orderFormated)
      .union(payFormated)
      .reduceByKey((count1, count2) => {
        (count1._1 + count2._1, count1._2 + count2._2, count1._3 + count2._3)
      })
      .sortBy(_._2, ascending = false).take(10)

    // 打印结果
    result.foreach(println)

    context.stop()
  }


}
