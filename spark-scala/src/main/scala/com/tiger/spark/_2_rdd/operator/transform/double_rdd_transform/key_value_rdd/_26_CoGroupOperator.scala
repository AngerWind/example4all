package com.tiger.spark._2_rdd.operator.transform.double_rdd_transform.key_value_rdd

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

class _26_CoGroupOperator {

  def coGroup(): Unit = {

    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd1: RDD[(String, Int)] = sc.makeRDD(List( ("a", 1), ("b", 2), ("c", 3) ))
    val rdd2: RDD[(String, Int)] = sc.makeRDD(List( ("a", 4), ("b", 5),("c", 6),("c", 7) ))

    // 两个rdd中相同的key放在一起
    val cgRDD: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd1.cogroup(rdd2)
    cgRDD.collect().foreach(println)

    sc.stop()

  }
}
