package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.value_rdd

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

import scala.util.Random


class _1_MapOperator {

  @Test
  def map(): Unit = {
    val sparkConf: SparkConf = new SparkConf()
      //.setMaster("spark://192.168.31.8:7077")
      .setMaster("local[*]")
      .setJars(Array[String]("C:\\Users\\Administrator\\Desktop\\example4all\\spark-scala\\target\\spark-scala-1.0-SNAPSHOT.jar"))
      .setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    // 设置分区数为1, 发现需要所有的算子处理完一个数据才会开始处理下一个数据
    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 1)

    new util.HashMap[Int, Int]().entrySet().iterator()

    // 需要传入T => U类型函数
    val result: RDD[Int] = list.map((x: Int) => {
      println(s"步骤1处理数据: $x")
      x
    }).map((x: Int) => {
      println(s"步骤2处理数据: $x")
      x
    })
    result.collect()
      .foreach(println)

    val threadLocal: ThreadLocal[Int] = new ThreadLocal[Int]
    threadLocal.set(10)
    val value: Int = threadLocal.get()
    threadLocal.remove()
    ThreadLocal.withInitial(() => Random.nextInt())
    context.stop()
  }
}
