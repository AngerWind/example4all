package com.tiger.spark._3_sql

import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object UDFExample {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("udf")
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import session.implicits._

    val frame: DataFrame = session.read.json("sql_data/user.json")

    frame.createOrReplaceTempView("user")
    val prefixFunc = (x:String) => "prefix_" + x

    session.udf.register("addPrefix", prefixFunc)
    session.sql("select addPrefix(username) from user").show()

    session.stop()
  }

}
