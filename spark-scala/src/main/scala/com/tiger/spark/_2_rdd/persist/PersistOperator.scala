package com.tiger.spark._2_rdd.persist

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class PersistOperator {

  @Test
  def persist(): Unit = {
    val sparkConf: SparkConf = new SparkConf()
      //.setMaster("spark://192.168.31.8:7077")
      .setMaster("local[*]")
      .setJars(Array[String]("C:\\Users\\Administrator\\Desktop\\example4all\\spark-scala\\target\\spark-scala-1.0-SNAPSHOT.jar"))
      .setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    // 设置分区数为1, 发现需要所有的算子处理完一个数据才会开始处理下一个数据
    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 1)

    list.persist(StorageLevel.MEMORY_AND_DISK)

    val result: RDD[Int] = list.map(x => x)
    result.collect()
      .foreach(println)
    context.stop()
  }

}
