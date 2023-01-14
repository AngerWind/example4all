package com.tiger.spark._2_rdd.accumulator

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.util.{AccumulatorV2, LongAccumulator}

import scala.collection.mutable

object CustomAccumulator {

  def main(args: Array[String]): Unit = {
    def main(args: Array[String]): Unit = {
      val conf: SparkConf = new SparkConf().setMaster("local").setAppName("xxx")
      val context: SparkContext = SparkContext.getOrCreate(conf)

      // 定义某人的视频的播放量, 求某人的视频总播放量
      val rdd: RDD[(String, Int)] = context.makeRDD(List(("zhangsan", 18), ("zhangsan", 20), ("lisi", 100),
        ("wanwu", 13), ("wanwu", 213), ("zhangsan", 11)), 2)

      // 创建并注册自定义的累加器
      val accumulator: CountAccumulator = new CountAccumulator()
      context.register(accumulator, "cntAccu")

      // 使用累加器的add方法进行累加
      rdd.foreach(kv => accumulator.add(kv))

      // 使用累加器的value方法获得累加后的结果
      println(accumulator.value)




      context.stop()
    }
  }

  /**
   * 继承Accumulator需要两个泛型
   * IN: 输入的累加的值
   * OUT: 聚合完毕之后的结果类型
   */
  class CountAccumulator extends AccumulatorV2[(String, Int), Map[String, Long]] {
    // 定义累加的中间状态
    private val accu: mutable.Map[String, Long]= mutable.Map()

    // 判断是否是初始状态
    override def isZero: Boolean = accu.isEmpty

    // 创建一个新的累加器
    override def copy(): AccumulatorV2[(String, Int), Map[String, Long]] = new CountAccumulator

    // 重置当前的累加器
    override def reset(): Unit = accu.clear()

    // 进行分区内的累加
    override def add(v: (String, Int)): Unit = {
      accu(v._1) = accu.getOrElse(v._1, 0L) + v._2.toLong
    }

    // 当前的累加器聚合另一个分区的累加器的结果
    override def merge(other: AccumulatorV2[(String, Int), Map[String, Long]]): Unit = {
      for ((key, value) <- other.value) {
        accu(key) = accu.getOrElse(key, 0L) + value
      }
    }

    // 返回最终的结果
    override def value: Map[String, Long] = accu.toMap
  }
}
