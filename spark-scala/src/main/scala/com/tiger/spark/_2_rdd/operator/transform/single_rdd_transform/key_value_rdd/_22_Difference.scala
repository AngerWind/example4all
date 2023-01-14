package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.key_value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _22_Difference {

  /**
   * 说明reduceByKey, aggregateByKey, foldByKey, combineByKey的不同
   */
  @Test
  def difference(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc: SparkContext = new SparkContext(sparkConf)


    val rdd: RDD[(String, Int)] = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("b", 3),
      ("b", 4), ("b", 5), ("a", 6)
    ), 2)

    /*
    下面四个函数都是计算相同key的sum,
    其实reduceByKey, aggregateByKey, foldByKey的不同在于对combineByKey调用传入的第一个参数的不同
    combineByKey :
            combineByKeyWithClassTag(
                createCombiner,  // 传入相同key的第一个值, 处理后返回一个初始值, 用于后续其他值的聚合
                mergeValue,      // 表示分区内数据的处理函数
                mergeCombiners,  // 表示分区间数据的处理函数
                )
    reduceByKey:
             combineByKeyWithClassTag[V](
                 (v: V) => v, // 原样返回第一个值
                 func, // 分区内计算规则
                 func, // 分区间计算规则
                 )

    aggregateByKey :
            combineByKeyWithClassTag[U](
                (v: V) => cleanedSeqOp(createZero(), v), // 初始值和第一个key的value值进行的分区内数据操作
                cleanedSeqOp, // 分区内计算规则
                combOp,       // 分区间计算规则
                )

    foldByKey:
            combineByKeyWithClassTag[V](
                (v: V) => cleanedFunc(createZero(), v), // 初始值和第一个key的value值进行的分区内数据操作
                cleanedFunc,  // 分区内计算规则
                cleanedFunc,  // 分区间计算规则
                )

     四个函数都需要分区内聚合函数和分区间聚合函数
     combineByKey的还需要一个函数, 传入相同key的第一个值,返回一个新值用于与后续key的聚合
     reduceByKey对这个函数的传入是(v: V) => v, 相当于传入的第一个值原样返回
     aggregateByKey对这个函数的传入是(v: V) => cleanedFunc(createZero(), v), 先将初始值和相同key的第一个值进行分区内聚合, 返回

         */
    rdd.reduceByKey(_ + _) // wordcount
    rdd.aggregateByKey(0)(_ + _, _ + _) // wordcount
    rdd.foldByKey(0)(_ + _) // wordcount
    rdd.combineByKey(v => v, (x: Int, y) => x + y, (x: Int, y: Int) => x + y) // wordcount

    sc.stop();
  }

}
