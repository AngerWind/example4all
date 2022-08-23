package com.tiger.spark._1_word_count

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {

  def main(args: Array[String]): Unit = {

    // 1. 创建spark config
    // 这里的local表示使用本地线程模拟分布式, 默认为1个线程, 可以使用local[2]表示使用两个线程来模拟, local[*]表示使用当前cpu核数的线程数来模拟
    val sparkConf: SparkConf = new SparkConf().setMaster("local").setAppName("WordCount")

    // 2. 创建spark上下文
    val context: SparkContext = new SparkContext(sparkConf)

    // 3. 通过textFile按行读取文件
    val fildRDD: RDD[String] = context.textFile("input/")

    // 4. 将行通过空格进行分词
    val words: RDD[String] = fildRDD.flatMap(_.split(" "))

    // 5. 将单词转换为元组
    val tuple: RDD[(String, Int)] = words.map((_, 1))

    // 6. 将元组按照key进行分组, 这里进行了隐式转换, 将RDD类型转换为PairRDDFunctions
    val reduce: RDD[(String, Int)] = tuple.reduceByKey(_ + _)

    // 7. 将聚合结果采集到内存
    val result: Array[(String, Int)] = reduce.collect()

    // 8. 打印结果
    result.foreach(println)

    // 9. 关闭连接
    context.stop()





    
  }
}
