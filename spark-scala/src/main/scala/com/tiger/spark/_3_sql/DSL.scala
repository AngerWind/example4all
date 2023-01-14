package com.tiger.spark._3_sql

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object DSL {

  def main(args: Array[String]): Unit = {


    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sql")
    val context: SparkContext = SparkContext.getOrCreate(conf)
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import session.implicits._


    val user: DataFrame = session.read.json("sql_data/user.json") // 读取json

    user.printSchema() // 查看user的schema
    val schema: StructType = user.schema // 获取schema

    user.select($"username", $"age" + 1).show // 需要import
    user.select("name", "age").show()

    // 断开连接
    session.stop();

  }

}
