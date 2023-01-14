package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.key_value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, Partitioner, SparkConf, SparkContext}
import org.junit.Test

class _17_PartitionByOperator {


  @Test
  def customPartitioner(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    // 转换为tuple类型
    val list1: RDD[(Int, Int)] = context.makeRDD(List(1, 2, 3, 4, 5, 6), 2).map((_, 1))

    // 自定义分区器, 默认两个分区, 大于3的分区1, 其他分区0
    // 分区从0开始
    val result: RDD[(Int, Int)] = list1.partitionBy(new Partitioner(){
      override def numPartitions: Int = 2

      override def getPartition(key: Any): Int = {
          key match {
            case i: Int if i > 3 => 1
            case _ => 0
          }
      }
    })

    result.collect().foreach(println)
    context.stop()
  }


  @Test
  def partitionBy(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    // 转换为tuple类型
    val list1: RDD[(Int, Int)] = context.makeRDD(List(1, 2, 3, 4, 5, 6), 2).map((_, 1))

    // 这里使用RDD.rddToPairRDDFunctions将RDD类型隐式转换为PairRDDFunctions类型, 才有partitionBy方法
    // 传入一个hash分区器, 之后的分区大小为3
    // 如果传入的分区器与分区个数都相同, 不会有任何作用
    val result: RDD[(Int, Int)] = list1.partitionBy(new HashPartitioner(3))

    result.glom().collect().foreach(println)
    context.stop()
  }

}
