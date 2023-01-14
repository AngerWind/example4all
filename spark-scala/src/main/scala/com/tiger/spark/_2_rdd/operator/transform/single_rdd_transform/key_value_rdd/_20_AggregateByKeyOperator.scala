package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.key_value_rdd

import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.junit.Test

class _20_AggregateByKeyOperator {

  @Test
  def groupByKey1(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    val list1: RDD[(String, Int)] = context.makeRDD(List(("a", 1), ("a", 2), ("a", 3),
      ("b", 4)), 2)

    /**
     * aggerateByKey一共有三个参数
     * 1. 聚合的初始值, 被应用于区间内聚合
     * 2. 区间内聚合函数, 可以返回另外一个类型
     * 3. 区间之间的聚合函数
     *
     * 上述两个函数都允许修改第一个参数, 以防止频繁创建对象造成的垃圾回收
     */
    val aggregateWithinPartition: (StringBuilder, Int) => StringBuilder = (combineValue: StringBuilder, value: Int) => {
      combineValue.append(value)
    }

    val aggregateBetweenPartition: (StringBuilder, StringBuilder) => StringBuilder = (aggregateValue: StringBuilder, value: StringBuilder) => {
      aggregateValue.append(value)
    }

    val result: RDD[(String, StringBuilder)] = list1.aggregateByKey(new StringBuilder("|"))(aggregateWithinPartition, aggregateBetweenPartition)

    result.collect().foreach(println)

    context.stop()
  }

  @Test
  def groupByKey2(): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)

    val rdd = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("b", 3),
      ("b", 4), ("b", 5), ("a", 6)
    ),2)

    // aggregateByKey最终的返回数据结果应该和初始值的类型保持一致
    //val aggRDD: RDD[(String, String)] = rdd.aggregateByKey("")(_ + _, _ + _)
    //aggRDD.collect.foreach(println)

    // 获取相同key的数据的平均值 => (a, 3),(b, 4)
    // 初始值为一个tuple, _1表示总数, _2表示次数
    val newRDD : RDD[(String, (Int, Int))] = rdd.aggregateByKey( (0,0) )(
      ( t, v ) => {
        (t._1 + v, t._2 + 1)
      },
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    )

    val resultRDD: RDD[(String, Int)] = newRDD.mapValues {
      case (num, cnt) => {
        num / cnt
      }
    }
    resultRDD.collect().foreach(println)

    sc.stop()
  }


}
