package com.tiger.spark._2_rdd.persist

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.junit.Test

class CheckPointOperator {

  @Test
  def checkpoint(): Unit = {
    val sparkConf: SparkConf = new SparkConf()
      .setMaster("local")
      .setAppName("WordCount")
    val context: SparkContext = new SparkContext(sparkConf)

    // 设置检查点路径, 一般是放在分布式存储中, 但是也可以放在本地文件系统中
    //    context.setCheckpointDir("hdfs://hadoop102/spark/checkpoint")
    context.setCheckpointDir("cp")

    val fildRDD: RDD[String] = context.makeRDD(List("hello world", "hello spark"))
    val words: RDD[String] = fildRDD.flatMap(_.split(" "))
    val tuple: RDD[(String, Int)] = words.mapPartitions(iterator => {
      println("word")
      iterator.map(word => (word, 1)).iterator
    })

    // 在执行map之后, 调用cache/persist方法进行持久化
    tuple.checkpoint()

    // 操作1
    val reduce: RDD[(String, Int)] = tuple.reduceByKey(_ + _)
    val result: Array[(String, Int)] = reduce.collect()
    println(result.mkString("Array(", ", ", ")"))

    // 操作2
    println(tuple.groupByKey().collect().mkString("Array(", ", ", ")"))

    context.stop()
  }

  @Test
  def checkpoint2(): Unit = {
    val sparkConf: SparkConf = new SparkConf()
      .setMaster("local")
      .setAppName("WordCount")
    val context: SparkContext = new SparkContext(sparkConf)

    // 设置检查点路径, 一般是放在分布式存储中, 但是也可以放在本地文件系统中
    //    context.setCheckpointDir("hdfs://hadoop102/spark/checkpoint")
    context.setCheckpointDir("cp")

    val fildRDD: RDD[String] = context.makeRDD(List("hello world", "hello spark"))
    val words: RDD[String] = fildRDD.flatMap(_.split(" "))
    val tuple: RDD[(String, Int)] = words.mapPartitions(iterator => {
      println("word")
      iterator.map(word => (word, 1)).iterator
    })

    // 执行checkpoint之前执行cache
    tuple.cache()

    // 在执行map之后, 调用cache/persist方法进行持久化
    tuple.checkpoint()

    // 操作1
    val reduce: RDD[(String, Int)] = tuple.reduceByKey(_ + _)
    val result: Array[(String, Int)] = reduce.collect()
    println(result.mkString("Array(", ", ", ")"))

    // 操作2
    println(tuple.groupByKey().collect().mkString("Array(", ", ", ")"))

    context.stop()
  }

  @Test
  def checkpoint3(): Unit = {
    val sparkConf: SparkConf = new SparkConf()
      .setMaster("local")
      .setAppName("WordCount")
    val context: SparkContext = new SparkContext(sparkConf)

    // 设置检查点路径, 一般是放在分布式存储中, 但是也可以放在本地文件系统中
    //    context.setCheckpointDir("hdfs://hadoop102/spark/checkpoint")
    context.setCheckpointDir("cp")

    val fildRDD: RDD[String] = context.makeRDD(List("hello world", "hello spark"))
    val words: RDD[String] = fildRDD.flatMap(_.split(" "))
    val tuple: RDD[(String, Int)] = words.mapPartitions(iterator => {
      println("word")
      iterator.map(word => (word, 1)).iterator
    })

    // 执行checkpoint之前执行cache
//    tuple.cache()
    tuple.checkpoint()

    println(tuple.toDebugString)

    // 操作1
    val reduce: RDD[(String, Int)] = tuple.reduceByKey(_ + _)
    val result: Array[(String, Int)] = reduce.collect()
    println(result.mkString("Array(", ", ", ")"))

    // 操作2
    println(tuple.groupByKey().collect().mkString("Array(", ", ", ")"))

    println(tuple.toDebugString)

    context.stop()
  }
}
