package com.tiger.spark._3_operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _3_MapPartitionsWithIndexOperator {

  @Test
  def mapPartitionsWithIndex(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 1)
    // mapPartitionsWithIndex与mapPartitions相同, 只不过传入了分区的索引
    // 需要传入一个(Int, Iterator[T]) => Iterator[U]类型的函数
    val result: RDD[Int] = list.mapPartitionsWithIndex((index: Int, itor: Iterator[Int]) => {
      println(s"当前传入数据的分区是$index")
      // 保留条件为真的数据
      itor.map(_ * 2).filter(_ > 4).iterator
    })
    result.collect().foreach(println)

    context.stop()
  }

}
