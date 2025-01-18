package com.tiger.spark._3_framework.application

import com.tiger.spark._3_framework.common.SparkApplication
import com.tiger.spark._3_framework.controller.WordCountController
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author Tiger.Shen
 * @date 2024/7/12
 * @description
 * @version 1.0
 */
object WordCountApplication extends App with SparkApplication {

  start() {
    new WordCountController().execute()
  }

}
