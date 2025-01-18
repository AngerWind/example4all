package com.tiger.spark._3_framework.service

import com.tiger.spark._3_framework.application.WordCountApplication.context
import com.tiger.spark._3_framework.dao.WordCountDao
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * @author Tiger.Shen
 * @date 2024/7/12
 * @description
 * @version 1.0
 */
class WordCountService {

  private val dao: WordCountDao = new WordCountDao

  def wordCount(): Array[(String, Int)] = {

    val fildRDD: RDD[String] = dao.readFile()

    // 4. 将行通过空格进行分词
    val words: RDD[String] = fildRDD.flatMap(_.split(" "))

    // 5. 将单词转换为元组
    val tuple: RDD[(String, Int)] = words.map((_, 1))

    // 6. 将元组按照key进行分组, 这里进行了隐式转换, 将RDD类型转换为PairRDDFunctions
    val reduce: RDD[(String, Int)] = tuple.reduceByKey(_ + _)

    // 7. 将聚合结果采集到内存
    val result: Array[(String, Int)] = reduce.collect()

    result
  }

}
