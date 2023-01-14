package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.key_value_rdd

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.junit.Test

class _22_CombineByKeyOperator {

  @Test
  def combineByKey(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    val list1: RDD[(String, Int)] = context.makeRDD(List(("a", 1), ("a", 2), ("a", 3), ("b", 4)), 2)

    /**
     * combineByKey一共有三个参数
     * 1. 一个函数, 传入key的第一个值, 获取聚合的初始值, 该初始值被应用于聚合后续的其他值
     * 2. 区间内聚合函数, 可以返回另外一个类型
     * 3. 区间之间的聚合函数
     *
     * 上述两个函数都允许修改第一个参数, 以防止频繁创建对象造成的垃圾回收
     */
    val defaultValueFunction: Int => StringBuilder = (firstValue: Int) => {
      new StringBuilder("|").append(firstValue)
    }
    val aggregateWithinPartition: (StringBuilder, Int) => StringBuilder = (combineValue: StringBuilder, value: Int) => {
      combineValue.append(value)
    }

    val aggregateBetweenPartition: (StringBuilder, StringBuilder) => StringBuilder = (aggregateValue: StringBuilder, value: StringBuilder) => {
      aggregateValue.append(value)
    }

    val result: RDD[(String, StringBuilder)] = list1.combineByKey(defaultValueFunction, aggregateWithinPartition, aggregateBetweenPartition)

    result.collect().foreach(println)

    context.stop()
  }
}
