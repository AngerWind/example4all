package com.tiger.spark._2_rdd.operator.transform.single_rdd_transform.value_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class _8_SampleOperator {

  @Test
  def sample (): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")
    val context: SparkContext = new SparkContext(sparkConf)


    val list: RDD[Int] = context.makeRDD(List(1, 2, 3, 4), 2)
    /**
     * 这个函数主要的作用是对数据集进行随机抽样
     * withReplacement:
     */
    val value: RDD[Int] = list.sample(true, 0.4, 1)

    context.stop()
  }

}
