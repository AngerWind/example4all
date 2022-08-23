package com.tiger.spark._3_operator.transform.double_rdd_transform.key_value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _24_JoinOperator {

  @Test
  def join(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd1: RDD[(String, Int)] = sc.makeRDD(List( ("a", 1), ("a", 2), ("c", 3) ))
    val rdd2: RDD[(String, Int)] = sc.makeRDD(List( ("a", 5), ("c", 6),("a", 4) ))

    // 内连接, 与sql中类似
    // 两个rdd的key的类型需要相同
    val joinRDD: RDD[(String, (Int, Int))] = rdd1.join(rdd2)

    joinRDD.collect().foreach(println)

    sc.stop()
  }

}
