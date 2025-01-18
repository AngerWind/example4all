package com.tiger.spark._3_framework.dao

import com.tiger.spark._3_framework.application.WordCountApplication.context
import com.tiger.spark._3_framework.utils.EnvUtil
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * @author Tiger.Shen
 * @date 2024/7/12
 * @description
 * @version 1.0
 */
class WordCountDao {

  def readFile(): RDD[String] = {
    val sc: SparkContext = EnvUtil.getSc
    if (sc == null) {
      SparkContext.getOrCreate()
    }

    // 3. 通过textFile按行读取文件
    val fildRDD: RDD[String] = sc.textFile("input/")
    fildRDD
  }



}
