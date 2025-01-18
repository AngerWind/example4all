package com.tiger.spark._2_rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test


class _2_Spill {

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
     * 1. 先获取最小分区个数
     *      1. 通过参数指定最小分区个数
     *      2. 如果没有通过参数指定分区个数, 那么默认使用math.min(defaultParallelism, 2)
     * 2. 获取了最小分区个数后, 计算真实的分区个数
     *      spark读取文件底层还是使用了mapreduce的 [[org.apache.hadoop.mapred.TextInputFormat]]
     *      所以具体的分区规则还是[[org.apache.hadoop.mapred.TextInputFormat#getSplits(org.apache.hadoop.mapreduce.JobContext]]
     *      具体规则如下:
     *        1. 计算所有文件的大小totalSize
     *        2. 计算每个分区的读取的数据大小 goalSize = totalSize / minPartitions
     *        3. 计算剩余文件大小 totalSize % goalSize, 如果该剩余文件大小超过0.1 * goalSize, 那么就成立新分区, 否则和最后一个分区合并
     *      比如总文件大小totalSize=7bytes, minPartitions=2, 那么目标分区大小goalSize=7/2=3bytes
     *      那么分两个区就还剩下1字节, 这1字节超过了目标分区大小的0.1, 那么就新成立一个分区, 所以总共需要3个分区
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

    /**
     * 1. 先获取分区个数
     *    1. 先获取参数中指定的分区个数
     *    2. 如果参数中没有指定分区个数, 那么就获取配置中spark.default.parallelism设置的分区个数
     *    3. 如果spark.default.parallelism也没有设置, 那么就获取cpu核心数作为分区个数
     * 2. 获取了分区个数之后, 就是对数据进行分区了, 具体的数据分区规则如下:
     *       数据的分区规则是: i为第n个分区数, 从0开始, numSlices为分区数
     *       基本的思想就是按照顺序平均分配
     *       val start = ((i * length) / numSlices).toInt
     *       val end = (((i + 1) * length) / numSlices).toInt
     *       如果7个数据分三个区, 那么三个分区的数据下标分别的[0, 1] [2, 3] [4, 5, 6]
     *       分区不均匀的话就是前面的少一点数据
     */
    val rdd: RDD[Int] = context.makeRDD(list)

    // 指定读取后的记录的分区个数
    val rdd1: RDD[Int] = context.makeRDD(list, 3)


    context.stop()
  }

}
