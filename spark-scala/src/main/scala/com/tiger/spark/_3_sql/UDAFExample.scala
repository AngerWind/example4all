package com.tiger.spark._3_sql

import org.apache.spark.sql.expressions.{Aggregator, MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, IntegerType, LongType, StructField, StructType}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, Row, SparkSession}
import org.junit.Test

// 使用udaf求sum
class UDAFExample{


  @Test
  def old(): Unit = {

    // spark 2.x使用这种方式
    class Avg extends UserDefinedAggregateFunction{

      // 定义聚合函数的输入类型
      override def inputSchema: StructType = StructType(Array(StructField("xxx", IntegerType)))

      // 定义聚合函数的缓冲区(中间状态)的数据类型
      override def bufferSchema: StructType = StructType(Array(StructField("sum", LongType),
        StructField("cnt", IntegerType)))

      // 初始化缓冲区,  需要与上面定义的缓冲区的类型一致
      override def initialize(buffer: MutableAggregationBuffer): Unit = {
        // 缓冲区按上面定义的缓冲区数据类型的索引保存和读取自己需要的数据

        // 这里buffer(0) = 0表示更新缓冲区中sum的值
        buffer(0) = 0L
        buffer(1) = 0
      }
      // 定义聚合函数的结果的类型
      override def dataType: DataType = DoubleType

      // 是否稳定: 对于相同的输入是否有相同的输出
      override def deterministic: Boolean = true

      // 进行聚合
      override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
        // 缓冲区按上面定义的缓冲区数据类型的索引保存和读取自己需要的数据
        // 传入的数据Row按照定义的输入数据结构的索引来获取数据

        // 这里的buffer.getLong(0)表示获取sum的值
        // input.getLong(0)表示获取输入数据中xxx的值
        buffer(0) = buffer.getLong(0) + input.getInt(0).toLong
        buffer(1) = buffer.getInt(1) + 1
      }

      // 两个buffer进行聚合, 第二个参数buffer2虽然是row类型但是还是按照缓冲区的方式来取值
      override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
        buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
        buffer1(1) = buffer1.getInt(1) + buffer2.getInt(1)
      }

      // 这里的buffer虽然是Row类型, 但是还是按照buffer的方式来计算最后的结果
      override def evaluate(buffer: Row): Any = buffer.getLong(0).toDouble / buffer.getInt(1)
    }

    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("udf")
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    val frame: DataFrame = session.createDataFrame(List(("zhangsan", 12), ("lisi", 199), ("wanwu", 22), ("zhangsan", 13))).toDF("name", "age")
    val avgUdaf = new Avg

    // 注册udaf函数
    session.udf.register("myAvg", avgUdaf)
    frame.createOrReplaceTempView("user")

    // 使用udaf进行查询
    session.sql("select name, myAvg(age) from user group by name").show()

    session.stop()
  }



  // spark 3.x使用这种方式
  @Test
  def newMethod (): Unit ={
    // 需要三个泛型, 输入类型, 中间状态类型, 输出类型
    class Avg extends Aggregator[Int, (Long, Int), Double]{

      // 初始化, 该方法需要返回一个初始的中间状态
      override def zero: (Long, Int) = (0L, 0)

      // 聚合输入的值
      override def reduce(b: (Long, Int), a: Int): (Long, Int) = {
        // 这种方式每次都要新建一个tuple, 所以可以定义一个case class用以保存中间状态
        (b._1 + a.toLong, b._2 + 1)
      }

      // 合并两个中间状态
      override def merge(b1: (Long, Int), b2: (Long, Int)): (Long, Int) = {
        (b1._1 + b2._1, b1._2 + b2._2)
      }

      // 返回结果值
      override def finish(reduction: (Long, Int)): Double = {
        reduction._1 / reduction._2
      }

      // 中间结果的编码方式, 如果是scala自带的类型, 可以调用Encoders.scalaXXX
      // 如果是自定义的类, 调用Encoders.product
      // 下面两个方法差不
      override def bufferEncoder: Encoder[(Long, Int)] = Encoders.tuple(Encoders.scalaLong, Encoders.scalaInt)

      override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
    }
  }
}
