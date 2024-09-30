package com.tiger.spark._3_sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object _01_CreateDataFrame {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sql")
    val context: SparkContext = SparkContext.getOrCreate(conf)
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    // 读取json创建dataframe
    val user: DataFrame = session.read.json("sql_data/user.json")
    // 从csv文件中创建dataFrame
//    session.read.csv("sql/data/user.csv")


    // 从集合中创建dataframe
    val user1: DataFrame = session.createDataFrame(List(("zhqangsan", 12), ("lisi", 199), ("wanwu", 22))).toDF("name", "age")
    // 从rdd中创建dataframe
    val rdd: RDD[(String, Int)] = context.makeRDD(List(("zhqangsan", 12), ("lisi", 199), ("wanwu", 22)))
    val frame: DataFrame = session.createDataFrame(rdd)

    user.createTempView("user_temp") // 如果有重名的view将报错, 只能在当前session中读取到该view, session断开后view消失
    user.createOrReplaceTempView("user_temp")
    val userResult: DataFrame = session.sql("select * from user_temp")
    userResult.show()

    user.createGlobalTempView("user_global_temp") // 创建global view,  可以跨session读取该view, session断开后view消失
    user.createOrReplaceGlobalTempView("user_global_temp")
    session.newSession().sql("select * from global_temp.user_global_temp").show()

    // 断开连接
    session.stop();
    context.stop();

  }

}
