package com.tiger.spark._3_sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.execution.datasources.jdbc.JDBCOptions
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.junit.{After, Test}

import java.util.Properties

class _08_ReadWrite {

  val conf: SparkConf = new SparkConf().setMaster("local").setAppName("load_save")
  val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()

  @After
  def stop(): Unit = {
    session.stop()
  }

  // 通用的读写文件
  @Test
  def load_save(): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("load_save")
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    // 指定默认的读取和输出的文件类型
    conf.set("spark.sql.sources.default", "json")


    // 通过load来指定加载路径或者文件, 如果指定路径下有多个文件会一起加载
    // 通过format指定读取的文件类型, 默认为parquet, 可以通过spark.sql.sources.default来修改默认读取的文件类型

    // format可以指定的值为csv, jdbc, json, orc, parquet, textFile, text, avro
    val frame: DataFrame = session.read.format("json").load("sql_data/user.json")

    // 通过format来指定输出文件的格式, 默认为parquet, 可以通过spark.sql.sources.default来修改默认输出的文件类型
    // 通过save来指定保存路径, 需要指定路径, 不能指定文件名
    // 指定输出文件的书写方式: append将文件输出在指定路径下, overwrite删除路径下文件再输出, error表示路径已存在就报错, ignore表示路径已存在就不输出文件
    // 通过option来指定文件格式相关的属性, 不同的文件格式有不同的属性
    frame.write.option("header", "true").format("csv").mode("overwrite").save("sql_data/csv")
  }

  @Test
  def parquet(): Unit = {
    // 读取parquet文件
    val df = session.read.load("sql_data/users.parquet")
    df.show

    //保存为 parquet 格式
    df.write.mode("append").save("output")
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
    // header表示读取的文件是否带表头
    // inferSchema表示指定推断类型
    val frame: DataFrame = session.read.option("sep", ",").option("header", "true").option("inferSchema", "true").csv("sql_data/csv")
    frame.write.mode("append").json("sql_data/csv")
  }

  // jdbc读写
  @Test
  def jdbc(): Unit = {

    /**
     * 方式1
     */
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

    /**
     * 方式2
     */
    val frame1: DataFrame = session
      .read.format("jdbc")
      .options(Map("url" -> "jdbc:mysql://linux1:3306/spark-sql?user=root&password=123123",
        "dbtable" -> "user",
        "driver" -> "com.mysql.jdbc.Driver"
      ))
      .load()
    frame1.show

    /**
     * 方式3
     */
    val props: Properties = new Properties()
    props.setProperty("user", "root")
    props.setProperty("password", "123123")
    val frame2: DataFrame = session.read.jdbc("jdbc:mysql://linux1:3306/spark-sql", "user", props)
    frame2.show

    /**
     * 写jdbc方式1
     */
    frame.write
      .format("jdbc")
      .option("url", "jdbc:mysql://linux1:3306/spark-sql")
      .option("user", "root")
      .option("password", "123123")
      .option("dbtable", "user")
      .mode(SaveMode.Append)
      .save()

    /**
     * 写jdbc方式2
     */
    val props1: Properties = new Properties()
    props1.setProperty("user", "root")
    props1.setProperty("password", "123123")
    frame.write.mode(SaveMode.Append).jdbc("jdbc:mysql://linux1:3306/spark-sql","user", props)
  }

}
