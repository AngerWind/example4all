package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.key_value_rdd

import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.junit.Test

class _18_ReduceByKeyOperator {

  @Test
  def reduceByKey(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    val list1: RDD[(String, Int)] = context.makeRDD(List(("a", 1), ("a", 2), ("a", 3), ("b", 4)), 2)

    // 先进行区间内聚合, 然后进行区间聚合
    // 需要传入(V, V) => V类型的函数
    val result: RDD[(String, Int)] = list1.reduceByKey((x, y) => x + y)

    // 可以传入一个Partitioner对reduce之后的key 进行分区
    val value: RDD[(String, Int)] = list1.reduceByKey(new Partitioner {
      override def numPartitions: Int = 2

      override def getPartition(key: Any): Int = {
        key match {
          case i: Int => 1
          case _ => 2
        }
      }
    }, (x, y) => x + y)
    result.glom().collect().foreach(println)
    context.stop()
  }

}
