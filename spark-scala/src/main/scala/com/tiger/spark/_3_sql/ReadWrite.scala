package com.tiger.spark._3_sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.execution.datasources.jdbc.JDBCOptions
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.junit.{After, Test}

class ReadWrite {

  val conf: SparkConf = new SparkConf().setMaster("local").setAppName("load_save")
  val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()

  @After
  def stop() {
    session.stop()
  }

  // 通用的读写文件
  @Test
  def load_save(): Unit = {
    // 通过load来指定加载路径或者文件, 如果指定路径下有多个文件会一起加载
    // 通过format指定读取的文件类型, 默认为parquet, 可以通过spark.sql.sources.default来修改默认读取的文件类型
    // format可以指定的值为csv、jdbc、 json、 orc、 parquet, textFile, text
    val frame: DataFrame = session.read.format("json").load("sql_data/user.json")

    // 通过format来指定输出文件的格式, 默认为parquet
    // 通过save来指定保存路径, 需要指定路径, 不能指定文件名
    // 指定输出文件的书写方式: append将文件输出在指定路径下, overwrite删除路径下文件再输出, error表示路径已存在就报错, ignore表示路径已存在就不输出文件
    // 通过option来指定文件格式相关的属性
    frame.write.option("header", "true").format("csv").mode("overwrite").save("sql_data/csv")
  }

  // json文件读写
  @Test
  def json(): Unit = {
    // 读取json文件要求文件的每一行必须是一个json串, 不能一个json对象占据多行
    val frame: DataFrame = session.read.json("sql_data/user.json")
    frame.write.mode("overwrite").json("sql_data/json")
  }

  // csv文件读写
  @Test
  def csv(): Unit = {
    // sep表示分隔符为逗号
    // header表示读取的文件带表头
    // inferSchema表示指定推断类型
    val frame: DataFrame = session.read.option("sep", ",").option("header", "true").option("inferSchema", "true").csv("sql_data/csv")
    frame.write.mode("append").json("sql_data/csv")
  }

  // jdbc读写
  @Test
  def jdbc(): Unit = {
    // sep表示分隔符为逗号
    // header表示读取的文件带表头
    // inferSchema表示指定推断类型
    val frame: DataFrame = session.read
      .option(JDBCOptions.JDBC_URL, "jdbc:mysql://192.168.31.8:3306/gmall")
      .option(JDBCOptions.JDBC_DRIVER_CLASS, "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "871403165")
      .option(JDBCOptions.JDBC_TABLE_NAME, "sku_info")
      .option(JDBCOptions.JDBC_BATCH_FETCH_SIZE, "10")
      .format("jdbc")
      .load()

    frame.show
  }

}
