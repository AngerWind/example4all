package com.tiger.spark._4_sql

import org.apache.spark.sql.SparkSession
import org.junit.Test

/**
 * @author Tiger.Shen
 * @date 2024/7/9
 * @description
 * @version 1.0
 */

object _09_ConnectToHive {
  def main(args: Array[String]): Unit = {
    new _09_ConnectToHive().connectToEmbededHive()
  }
}

class _09_ConnectToHive {

  @Test
  def connectToEmbededHive(): Unit = {
    /**
     * 连接内置的Hive
     *     1. 导入Hive依赖
     *     2. spark启动Hive的支持
     *     3. 设置Hive真实数据的位置, 默认在项目根目录下的spark-warehouse下
     */

    val spark: SparkSession = SparkSession
      .builder()
      // 设置Hive真实数据的位置
      .config("spark.sql.warehouse.dir", "warehouse")
      .enableHiveSupport()
      .master("local[*]")
      .appName("sql")
      .getOrCreate()



    spark.sql(
      """
        |create table user(username string, age int);
        |""".stripMargin)

    spark.sql(
      """
        |insert into user(username, age) values("zhangsan", 12), ("lisi", 14)
        |""".stripMargin)

    spark.sql(
      """
        |select * from user""".stripMargin).show

    spark.stop()
  }

  @Test
  def connectToExternalHive(): Unit = {
    //  创建 SparkSession
    val spark: SparkSession = SparkSession
      .builder()
      .enableHiveSupport()
      .master("local[*]")
      .appName("sql")
      .getOrCreate()

    spark.sql(
      """
        |create table user(varchar username, int age;)
        |""".stripMargin)

    spark.sql(
      """
        |insert into user(username, age) values("zhangsan", 12), ("lisi", 14)
        |""".stripMargin)

    spark.stop()
  }

}
