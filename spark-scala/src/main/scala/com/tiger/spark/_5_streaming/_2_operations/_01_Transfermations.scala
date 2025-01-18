package com.tiger.spark._5_streaming._2_operations

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.junit.Test

import scala.collection.mutable
import scala.util.Random

/**
 * @author Tiger.Shen
 * @date 2024/7/11
 * @description
 * @version 1.0
 */
class _01_Transfermations {

  @Test
  def generalOperations(): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("RDDStream")
    val ssc = new StreamingContext(conf, Seconds(4))

    // 创建 RDD 队列
    val rddQueue = new mutable.Queue[RDD[String]]()
    // 创建 QueueInputDStream, 用于保存RDD
    // oneAtTime表示每个时间段只从queue中读取一个RDD作为数据
    val inputStream: InputDStream[String] = ssc.queueStream(rddQueue, oneAtATime = true)


    // 处理队列中的 RDD 数据
    val result: DStream[(String, Int)] = inputStream.flatMap(str => str.split(","))
      .filter(word => !word.startsWith("g")) // 过滤掉g开头的单词
      .map(word => (word, 1))
      .repartition(3) // 改变分区数
      .reduceByKey((v1, v2) => v1 + v2)
    result.print()

    // 启动任务
    ssc.start()

    // 启动另外一个线程, 来生成数据
    new DataGenetor(rddQueue, ssc).start()
    ssc.awaitTermination()
  }

  @Test
  def transform(): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("RDDStream")
    val ssc = new StreamingContext(conf, Seconds(4))

    // 创建 RDD 队列
    val rddQueue = new mutable.Queue[RDD[String]]()
    // 创建 QueueInputDStream, 用于保存RDD
    // oneAtTime表示每个时间段只从queue中读取一个RDD作为数据
    val inputStream: InputDStream[String] = ssc.queueStream(rddQueue, oneAtATime = true)


    // 处理队列中的 RDD 数据
    val result: DStream[(String, Int)] = inputStream.transform(rdd => {
      val words = rdd.flatMap(str => str.split(","))
      val wordAndOne = words.map(word => (word, 1))
      val reduced = wordAndOne.reduceByKey((v1, v2) => v1 + v2)
      reduced
    })
    result.print()

    // 启动任务
    ssc.start()

    // 启动另外一个线程, 来生成数据
    new DataGenetor(rddQueue, ssc).start()

    ssc.awaitTermination()
  }

  @Test
  def join(): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("RDDStream")
    val ssc = new StreamingContext(conf, Seconds(4))

    val rddQueue1 = new mutable.Queue[RDD[String]]()
    val inputStream1: InputDStream[String] = ssc.queueStream(rddQueue1, oneAtATime = true)

    val rddQueue2 = new mutable.Queue[RDD[String]]()
    val inputStream2: InputDStream[String] = ssc.queueStream(rddQueue2, oneAtATime = true)

    val result1: DStream[(String, String)] = inputStream1.map(word => {
      val strings: Array[String] = word.split(",")
      (strings(0), strings(1))
    })
    val result2: DStream[(String, String)] = inputStream2.map(word => {
      val strings: Array[String] = word.split(",")
      (strings(0), strings(1))
    })
    // 对同一时间段内的两个RDD数据进行join操作, DStream的泛型必须是二元组, 才可以执行join操作
    // 底层实际上执行的是rdd1.join(rdd2), 所以也必须按照join的规范, 那就是两个RDD中的数据要一致
    // join获得的是相同key的笛卡尔积
    val join1: DStream[(String, (String, String))] = result1.join(result2)
    val join: DStream[(String, (String, Option[String]))] = result1.leftOuterJoin(result2)
    val join3: DStream[(String, (Option[String], String))] = result1.rightOuterJoin(result2)

    join1.print()
    ssc.start()

    // 开始生成数据
    new DataGenetor(rddQueue1, ssc, 5).start()
    new DataGenetor(rddQueue2, ssc, 5).start()

    ssc.awaitTermination()
  }

  @Test
  def updateStateByKey(): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("RDDStream")
    val ssc = new StreamingContext(conf, Seconds(4))
    // 状态的保存, 需要设置checkpoint地址, 一般设置为hdfs
    ssc.checkpoint("cp")

    val rddQueue1 = new mutable.Queue[RDD[String]]()
    val inputStream1: InputDStream[String] = ssc.queueStream(rddQueue1, oneAtATime = true)


    // 计算出当前批次数据的wordcount
    val wordcountPerBatch: DStream[(String, Int)] = inputStream1.flatMap(str => str.split(","))
      .map(word => (word, 1))
      .reduceByKey((v1, v2) => v1 + v2)
    // 当前批次的wordcount和历史数据的wordcount进行汇总集合

    // updateStateByKey接收一个(Seq[V], Option[S]) => Option[S]类型的函数作为参数
    // S表示的是状态的类型, 这里我们希望记录每个key对应出现的个数, 所以这里S为Int
    // V表示Key对应的value的类型, 这里也是Int
    // values表示对于当前RDD中, key对应的value组成的集合
    // old表示历史数据, 如果是第一次计算, 则old为None
    // 对于每个key, 都会执行一次updateStateFunc
    def updateStateFunc(values: Seq[Int], old: Option[Int]): Option[Int] = {
      // 每个key的count, 和之前的count进行聚合
      val newValue = values.sum + old.getOrElse(0)
      Some(newValue)
    }
    val result: DStream[(String, Int)] = wordcountPerBatch.updateStateByKey(updateStateFunc)
    result.print() // 打印聚合之后的结果

    ssc.start()
    // 开始生成数据
    new DataGenetor(rddQueue1, ssc).start()

    ssc.awaitTermination()
  }
}



// batchSize表示每一批数据的个数,
class DataGenetor(val queue: mutable.Queue[RDD[String]], val ssc: StreamingContext, val batchSize: Int = -1)
  extends Thread {

  override def run(): Unit = {
    val firstList = List("hello", "fucking", "grateful", "scared", "gorgeous")
    val secondList = List("world", "car", "bird", "view", "queue", "")
    while (true) {
      // 随机生成一批数据的个数
      var count = batchSize
      if (batchSize == -1) {
        // 如果没有指定batchSize, 则随机生成
        count = new Random().nextInt(8)
      }
      val list = mutable.ListBuffer[String]()

      // 开始随机生成这批数据, 并放入队列中
      for (_ <- 0 to count) {
        val first = firstList(new Random().nextInt(firstList.length))
        val second = secondList(new Random().nextInt(secondList.length))
        list.append(s"$first,$second")
      }
      // 构造RDD, 并放入Queue中
      queue += ssc.sparkContext.makeRDD(list.toList, 1)
      Thread.sleep(2000)
    }
  }
}