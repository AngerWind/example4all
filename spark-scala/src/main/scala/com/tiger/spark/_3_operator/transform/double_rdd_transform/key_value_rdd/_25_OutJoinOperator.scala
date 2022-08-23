package com.tiger.spark._3_operator.transform.double_rdd_transform.key_value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _25_OutJoinOperator {

  @Test
  def outJoin(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd1: RDD[(String, Int)] = sc.makeRDD(List( ("a", 1), ("b", 2), ("c", 3) ))
    val rdd2: RDD[(String, Int)] = sc.makeRDD(List( ("a", 4), ("b", 5),("c", 6) ))

    // 左外连接, 右外连接, 与sql中一样
    // 注意返回的是option
    val leftJoinRDD: RDD[(String, (Int, Option[Int]))] = rdd1.leftOuterJoin(rdd2)
    val rightJoinRDD: RDD[(String, (Option[Int], Int))] = rdd1.rightOuterJoin(rdd2)

    leftJoinRDD.collect().foreach(println)
    rightJoinRDD.collect().foreach(println)

    sc.stop()

  }

}
