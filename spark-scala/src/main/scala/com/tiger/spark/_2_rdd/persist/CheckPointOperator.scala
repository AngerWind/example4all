package com.tiger.spark._2_rdd.persist

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.junit.Test

class CheckPointOperator {

  @Test
  def checkPoint(): Unit = {
    val sparkConf: SparkConf = new SparkConf()
      //.setMaster("spark://192.168.31.8:7077")
      .setMaster("local[*]")
      .setJars(Array[String]("C:\\Users\\Administrator\\Desktop\\example4all\\spark-scala\\target\\spark-scala-1.0-SNAPSHOT.jar"))
      .setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)

    context.setCheckpointDir("hdfs://hadoop102/spark/checkpoint")

    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 1)


    list.checkpoint()
    list.localCheckpoint()

    val result: RDD[Int] = list.map(x => x)
    result.collect()
      .foreach(println)
    context.stop()
  }

}
