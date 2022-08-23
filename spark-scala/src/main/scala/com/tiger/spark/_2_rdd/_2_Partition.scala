package com.tiger.spark._2_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test


class _2_Partition {

  /**
   * 从文件中读取数据的并行度和分区规则
   */
  @Test
  def partitionFromFile(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")

    // 指定默认的并行度
    sparkConf.set("spark.default.parallelism", "3")

    val context: SparkContext = new SparkContext(sparkConf)

    /**
     * 这种方式使用默认的最小并行度, 默认最小并行度的值为 math.min(spark.default.parallelism, 2)
     * 可以通过sparkConf来设置默认的最小并行度
     * 需要注意的是, 这里只是指定了最小的并行度, 实际的并行度还是要通过计算才能得知
     * 因为spark读取文件底层还是使用了mapreduce的 [[org.apache.hadoop.mapred.TextInputFormat]]
     * 所以具体的分区规则还是[[org.apache.hadoop.mapred.TextInputFormat#getSplits(org.apache.hadoop.mapreduce.JobContext]]
     */
    val fileRDD1: RDD[String] = context.textFile("input/")
    fileRDD1.saveAsTextFile("output1/")

    // 指定读取后的记录的最小分区个数
    val fileRDD: RDD[String] = context.textFile("input/", 3)
    fileRDD.saveAsTextFile("output/")

    context.stop()
  }

  /**
   * 从内存中读取数据的并行度和分区规则
   */
  @Test
  def partitionFromMemory(): Unit = {
    val list: List[Int] = List(1, 2, 3, 4, 5, 6)

    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("xxx")

    // sparkConf.set("spark.default.parallelism", "3")

    val context: SparkContext = new SparkContext(sparkConf)

    // 这种方式使用默认的并行度, 默认并行度的值为 spark.default.parallelism, 如果没有那就是cpu线程数
    // 可以通过sparkConf来设置默认的并行度
    // 数据的分区规则是: i为第n个分区数, 从0开始, numSlices为分区数
    //        基本的思想就是按照顺序平均分配
    //        val start = ((i * length) / numSlices).toInt
    //        val end = (((i + 1) * length) / numSlices).toInt
    val rdd: RDD[Int] = context.makeRDD(list)

    // 指定读取后的记录的分区个数
    val rdd1: RDD[Int] = context.makeRDD(list, 3)


    context.stop()
  }

}
