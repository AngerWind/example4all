package com.tiger.spark._4_sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
 * @author Tiger.Shen
 * @date 2024/7/8
 * @description
 * @version 1.0
 */
object _03_CreateDataSet {

  case class Person(var name: String, var age: Long)
  def main(args: Array[String]): Unit = {


    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sql")
    val context: SparkContext = SparkContext.getOrCreate(conf)
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import session.implicits._

    /**
     * 使用样例类创建DataSet
     */

    val list: Seq[Person] = Seq(Person("zhangsan", 123), Person("lisi", 23))
    val ds: Dataset[Person] = list.toDS()
    ds.show

    /**
     * 通过RDD来获取DataSet
     */
    val rdd: RDD[Person] = context.makeRDD(Seq(Person("zhangsan", 123), Person("lisi", 23)))
    val ds1: Dataset[Person] = rdd.toDS()
    ds1.show

    // 断开连接
    session.stop()
    context.stop()
  }


}
