package com.tiger.spark._4_sql

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object _02_DataFrame_DSL {

  def main(args: Array[String]): Unit = {


    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sql")
    val context: SparkContext = SparkContext.getOrCreate(conf)
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    // 读取json
    val user: DataFrame = session.read.json("sql_data/user.json")

    // 查看user的schema
    println(user.printSchema())

    // 获取schema
    val schema: StructType = user.schema

    import session.implicits._
    // 查看username列, 和age + 1列
    user.select($"username", $"age" + 1).show

    // 查看age大于30的数据
    user.filter($"age" > 30).show
    user.where("age > 30").show

    // 按照age分组, 进行count
    user.groupBy("age").count.show

    // 断开连接
    session.stop()
    context.stop()
  }

}
