package com.tiger.spark._3_framework.common

import com.tiger.spark._3_framework.utils.EnvUtil
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author Tiger.Shen
 * @date 2024/7/12
 * @description
 * @version 1.0
 */
trait SparkApplication {

  def start(master: String = "local[*]", appName: String = "SparkApplication")(op: => Unit): Unit = {
    // 1. 创建spark config
    // 这里的local表示使用本地线程模拟分布式, 默认为1个线程, 可以使用local[2]表示使用两个线程来模拟, local[*]表示使用当前cpu核数的线程数来模拟
    val sparkConf: SparkConf = new SparkConf()
      .setMaster(master) // 这里别硬编码设置master地址, 应该在提交的时候来指定
      .setAppName(appName)

    // 2. 创建spark上下文
    val context: SparkContext = new SparkContext(sparkConf)

    EnvUtil.setSc(context)

    try {
      op
    } catch {
      case e: Exception => e.printStackTrace()
    }

    // 9. 关闭连接
    context.stop()
  }

}
