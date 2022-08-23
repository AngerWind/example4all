package com.tiger.spark._2_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _1_CreateRDD {

  @Test
  def createRDDFromMemory(): Unit = {
    val list: List[Int] = List(1, 2, 3, 4)

    // 这里的[*]表示使用cpu最大核心数来模拟, 如果是local[2]表示使用两个核心即两个线程来模拟, local表示一个线程
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create_rdd_from_memory")
    val context: SparkContext = new SparkContext(sparkConf)

    // 通过context从内存从创建一个rdd
    val listRDD: RDD[Int] = context.makeRDD(list)
    val result: Array[Int] = listRDD.collect()

    context.stop()
  }

  @Test
  def createRDDFromFile(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("create_rdd_from_file")
    val context: SparkContext = new SparkContext(sparkConf)

    // 使用绝对路径创建
    val fileRDD1: RDD[String] = context.textFile("C:\\Users\\Administrator\\Desktop\\data.txt")

    // 通过context使用相对路径创建一个rdd
    val fileRDD: RDD[String] = context.textFile("input/data.txt")

    // 通过相对路径指定目录, 即可读取该目录下面所有文件
    val fileRDD2: RDD[String] = context.textFile("input/")

    // 通过通配符指定input下面所有data开头的txt文件
    val fileRDD3: RDD[String] = context.textFile("input/data*.txt")

    // 通过wholeTextFiles读取文件返回的是一个tuple, _1是绝对路径, _2表示读取到的行
    val tupleRDD: RDD[(String, String)] = context.wholeTextFiles("input/")
    tupleRDD.collect().foreach(println)

    // 指定读取hdfs文件
    val hdfsRDD: RDD[String] = context.textFile("hdfs://hadoop102:8080/data.txt")

    context.stop()
  }

}
