package com.tiger.spark._3_framework.utils

import org.apache.spark.SparkContext

/**
 * @author Tiger.Shen
 * @date 2024/7/12
 * @description
 * @version 1.0
 */
object EnvUtil {

  private val scLocal = new ThreadLocal[SparkContext]

  def getSc: SparkContext = {
    scLocal.get()
  }

  def setSc(sc: SparkContext): Unit = {
    scLocal.set(sc)
  }
}
