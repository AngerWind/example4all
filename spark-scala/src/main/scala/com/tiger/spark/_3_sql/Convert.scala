package com.tiger.spark._3_sql

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object Convert {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sql")
    val context: SparkContext = SparkContext.getOrCreate(conf)
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import session.implicits._

    val value: RDD[(String, Int)] = context.makeRDD(List("zhangsan" -> 18, "lisi" -> 17, "wanwu" -> 10))

    // rdd <==> dataframe
    val frame: DataFrame = value.toDF("name", "age") // rdd转换为DataFrame, 需要传入对应的列名
    val value1: RDD[Row] = frame.rdd // DataFrame 转换为 rdd

    // dataframe <==> dataset
    val dataSet: Dataset[Person] = frame.as[Person] // DataFrame转换为DataSet
    val frame1: DataFrame = dataSet.toDF() // DataSet转换为DataFrame
    val frame2: DataFrame = dataSet.toDF("name1", "age1") // DataSet转换为DataFrame, 重新命名列

    // rdd <==> dataset
    val personRDD: RDD[Person] = context.makeRDD(List(Person("zhangsna", 15), Person("wanwu", 10), Person("lisi", 199)))
    val dataSet2: Dataset[Person] = personRDD.toDS()  // 需要通过样例类获取结构信息才能够转换为DataSet
    val rdd: RDD[Person] = dataSet2.rdd // DataSet转换为rdd


    // 断开连接
    session.stop();

  }

  case class Person(name: String, age: Int)
}


