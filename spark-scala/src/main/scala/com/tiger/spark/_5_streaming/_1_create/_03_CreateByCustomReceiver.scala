package com.tiger.spark._4_streaming._1_create


import org.apache.spark.SparkConf
import org.apache.spark.internal.Logging
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}

import java.io.{BufferedReader, InputStreamReader}
import java.net.{ConnectException, Socket}
import java.nio.charset.StandardCharsets
import scala.util.control.NonFatal

/**
 * @author Tiger.Shen
 * @date 2024/7/10
 * @description
 * @version 1.0
 */
object _03_CreateByCustomReceiver {
  def main(args: Array[String]): Unit = {
    //1.初始化 Spark 配置信息
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]")
      .setAppName("StreamWordCount")
    //2.初始化 SparkStreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    //3.创建自定义 receiver 的 Streaming
    val lineStream: ReceiverInputDStream[String] = ssc.receiverStream(new SocketReceiver("hadoop102", 9999))
    //4.将每一行数据做切分，形成一个个单词
    val wordStream: DStream[String] = lineStream.flatMap(_.split("\t"))
    //5.将单词映射成元组（word,1）
    val wordAndOneStream: DStream[(String, Int)] = wordStream.map((_, 1))
    //6.将相同的单词次数做统计
    val wordAndCountStream: DStream[(String, Int)] = wordAndOneStream.reduceByKey(_ + _)
    //7.打印
    wordAndCountStream.print()
    //8.启动 SparkStreamingContext
    ssc.start()
    ssc.awaitTermination()
  }

  /**
   * 自定义Receiver, 必须继承Receiver接口
   * Receiver中有一个泛型, 表示接受到的数据的类型
   * Receiver的构造函数有一个参数, 表示Receiver对于接受到的数据, 应该使用什么持久化策略
   * StorageLevel.MEMORY_ONLY: 对于接受到的数据, 只保存到内存中
   */
  class SocketReceiver(host: String, port: Int ) extends Receiver[String](StorageLevel.MEMORY_ONLY) with Logging {

    private var socket: Socket = _

    def onStart(): Unit = {
      logInfo(s"正在连接 $host:$port")
      try {
        socket = new Socket(host, port)
      } catch {
        case e: ConnectException =>
          restart(s"连接错误 $host:$port", e)
          return
      }
      logInfo(s"成功连接 $host:$port")

      // Start the thread that receives data over a connection
      new Thread("Socket Receiver") {
        setDaemon(true)
        override def run(): Unit = {
          receive()
        }
      }.start()
    }

    def onStop(): Unit = {
      // in case restart thread close it twice
      synchronized {
        if (socket != null) {
          socket.close()
          socket = null
          logInfo(s"Closed socket to $host:$port")
        }
      }
    }

    /** Create a socket connection and receive data until receiver is stopped */
    def receive(): Unit = {
      try {
        val reader = new BufferedReader(new InputStreamReader(socket.getInputStream, StandardCharsets.UTF_8))
        val str = reader.readLine()
        while (!isStopped && str != null) {
          store(str)
        }
        reader.close()

        if (!isStopped()) {
          // 不是停止, 说明readline被中断了, 要重启任务
          restart("Socket data stream had no more data")
        } else {
          // 否则就是Receiver被停止了
          logInfo("Stopped receiving")
        }
      } catch {
        case NonFatal(e) =>
          logWarning("Error receiving data", e)
          restart("Error receiving data", e)
      } finally {
        onStop()
      }
    }
  }
}
