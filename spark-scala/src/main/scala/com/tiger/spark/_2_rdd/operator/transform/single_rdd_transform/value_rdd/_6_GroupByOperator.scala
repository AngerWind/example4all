package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _6_GroupByOperator {

  @Test
  def groupBy(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    // 设置分区数为1, 发现需要所有的算子处理完一个数据才会开始处理下一个数据
    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 1)

    // 实现一个分组函数, 按照奇偶性来分组
    // 传入的是当前的数据, 返回一个key, 相同的key放在一个分组
    // 分组将会导致shuffle
    def groupByFunction(data: Int): Int = {
      data % 2
    }

    // 返回的类型是RDD[(K, Iterable[T])]
    val group: RDD[(Int, Iterable[Int])] = list.groupBy(groupByFunction)

    group.collect().foreach(println)

    context.stop()
  }

}
