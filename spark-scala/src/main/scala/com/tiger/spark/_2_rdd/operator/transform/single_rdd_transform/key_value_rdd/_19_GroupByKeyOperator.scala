package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.key_value_rdd

import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.junit.Test

class _19_GroupByKeyOperator {

  @Test
  def groupByKey(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    val list1: RDD[(String, Int)] = context.makeRDD(List(("a", 1), ("a", 2), ("a", 3), ("b", 4)), 2)


    // 相同的key分在同一个组, 可以修改分区数量
    val result1: RDD[(String, Iterable[Int])] = list1.groupByKey(3)
    // groupByKey与groupBy的区别在于, groupBy需要传入一个T => K类型的函数来计算key, 并且返回的类型不一样
    val groupByResult: RDD[(String, Iterable[(String, Int)])] = list1.groupBy((tuple: (String, Int)) => tuple._1)

    // 可以自定义分区器
    val result2: RDD[(String, Iterable[Int])] = list1.groupByKey(new Partitioner {
      override def numPartitions: Int = 1

      override def getPartition(key: Any): Int = {
        0
      }
    })

    result1.collect().foreach(println)

    context.stop()
  }
}
