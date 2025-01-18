package com.tiger.spark._4_sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
 * @author Tiger.Shen
 * @version 1.0
 */
object _04_DataSet_DSL {

  import org.apache.spark.sql.{SparkSession, DataFrame, Dataset}
  import org.apache.spark.rdd.RDD

  case class Person(name: String, age: Int)


  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("DatasetVsDataFrame")
      .master("local[*]") // 使用本地模式
      .getOrCreate()

    import spark.implicits._

    // 创建一个 Seq[Person] 并转换为 Dataset
    val peopleSeq = Seq(Person("Alice", 30), Person("Bob", 25), Person("Charlie", 35))
    val peopleDS: Dataset[Person] = peopleSeq.toDS()

    // 创建 DataFrame
    val peopleDF: DataFrame = peopleDS.toDF()

    // 类型安全的 API 示例
    demonstrateTypeSafety(spark, peopleDS, peopleDF)

    // 函数式编程接口示例
    demonstrateFunctionalProgramming(spark, peopleDS, peopleDF)

    spark.stop()
  }

  def demonstrateTypeSafety(spark: SparkSession, peopleDS: Dataset[Person], peopleDF: DataFrame): Unit = {
    import spark.implicits._

    println("\nType Safety Comparison:")
    // Dataset 的类型安全操作
    val adultsDS: Dataset[Person] = peopleDS.filter(_.age > 30)
    adultsDS.show()

    // DataFrame 的操作：注意这里需要使用字符串列名，并且返回的是 Row 对象
    val adultsDF: DataFrame = peopleDF.filter($"age" > 30)
    adultsDF.show()

    // 如果在 DataFrame 中拼写错误，编译时不会报错，但会在运行时抛出异常
    // val wrongColumnDF = peopleDF.filter($"ages" > 30) // 会在运行时出错
  }

  def demonstrateFunctionalProgramming(spark: SparkSession, peopleDS: Dataset[Person], peopleDF: DataFrame): Unit = {
    import spark.implicits._
    println("\nFunctional Programming Comparison:")

    // Dataset 使用 map 进行操作，直接操作类型化对象
    val namesDS: Dataset[String] = peopleDS.map(person => person.name)
    namesDS.show()

    // DataFrame 使用 select + expr 进行操作，需要指定列名，返回的是 Row 对象
    val namesDF: DataFrame = peopleDF.select($"name")
    namesDF.show()
  }

}
