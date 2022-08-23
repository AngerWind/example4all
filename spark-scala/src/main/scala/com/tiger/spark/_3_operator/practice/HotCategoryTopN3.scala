package com.tiger.spark._3_operator.practice

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

import scala.collection.mutable

/**
 * 一次性统计每个品类的点击数, 下单数, 支付数
 *
 * 因为使用的是junit测试, 使用了闭包, 所以HotCategoryTopN3需要混入Serializable!!!!!!!!!!!
 */
class HotCategoryTopN3 extends Serializable{

  /**
   * HotCategoryTopN2存在的问题
   *  1. 聚合所有数据的时候使用了reduceByKey, shuffle导致性能下降
   *
   * 优化: 使用自定义累加器来进行统计
   */
  @Test
  def practice(): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("aaa").setMaster("local[*]")
    val context: SparkContext = new SparkContext(conf)

    // 注册一个累加器
    val accumulator: CountAccumulator = new CountAccumulator
    context.register(accumulator, "countAcc")

    context.textFile("input/user_visit_action.csv")
      .foreach(str => {
        val strings: Array[String] = str.split(",")
        // 点击数据
        if (strings(6) != "-1" && strings(7) != "-1") {
          accumulator.add((strings(6), (1, 0, 0)))
        }
        // 下单数据
        else if (strings(8) != "" && strings(9) != "") {
          strings(8).split("-").foreach(id => accumulator.add((id, (0, 1, 0))))
        }
        // 支付数据
        else if (strings(10) != "" && strings(11) != "") {
          strings(10).split("-").foreach(id => accumulator.add((id, (0, 0, 1))))
        }
      })
    accumulator.value.toArray.sortBy(_._2)(Ordering.Tuple3[Int, Int, Int].reverse).take(10).foreach(println)
    context.stop()
  }

  class CountAccumulator extends AccumulatorV2[(String, (Int, Int, Int)), mutable.Map[String, (Int, Int, Int)]] {

    private val acc: mutable.Map[String, (Int, Int, Int)] = mutable.Map()

    override def isZero: Boolean = acc.isEmpty

    override def copy(): AccumulatorV2[(String, (Int, Int, Int)), mutable.Map[String, (Int, Int, Int)]] = {
      new CountAccumulator
    }

    override def reset(): Unit = acc.clear()

    override def add(v: (String, (Int, Int, Int))): Unit = {
      val count1: (Int, Int, Int) = acc.getOrElse(v._1, (0, 0, 0))
      val count2 = v._2
      acc.update(v._1, (count1._1 + count2._1, count1._2 + count2._2, count1._3 + count2._3))
    }

    override def merge(other: AccumulatorV2[(String, (Int, Int, Int)), mutable.Map[String, (Int, Int, Int)]]): Unit = {
      other.value.foreach({
        case (key, count2) =>
          val count1: (Int, Int, Int) = acc.getOrElse(key, (0, 0, 0))
          acc.update(key, (count1._1 + count2._1, count1._2 + count2._2, count1._3 + count2._3))
      })
    }

    override def value: mutable.Map[String, (Int, Int, Int)] = acc
  }
}
