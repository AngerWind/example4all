package com.tiger.spark._2_rdd.operator.transform

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ArrayBuffer

class TopN {

  def topN1(): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc: SparkContext = new SparkContext(sparkConf)

    // 1. 获取原始数据：时间戳，省份，城市，用户，广告
    val dataRDD: RDD[String] = sc.textFile("input/agent.log")

    // 2. 将原始数据进行结构的转换。时间戳，省份，城市，用户，广告 => ((省份，广告), 1 )
    val mapRDD: RDD[((String, String), Int)] = dataRDD.map(
      line => {
        val datas = line.split(" ")
        ((datas(1), datas(4)), 1)
      }
    )

    // 3. 将转换结构后的数据，进行分组聚合
    //    ((省份，广告), 1 ) => ((省份，广告), sum)
    val reduceRDD: RDD[((String, String), Int)] = mapRDD.reduceByKey(_ + _)

    // 4. 将聚合的结果进行结构的转换
    //    ((省份，广告), sum ) => (省份,(广告, sum))
    val newMapRDD: RDD[(String, (String, Int))] = reduceRDD.map {
      case ((prv, ad), sum) => (prv, (ad, sum))
    }

    val result: RDD[(String, ArrayBuffer[(String, Int)])] = newMapRDD
      .aggregateByKey(ArrayBuffer[(String, Int)]())((list: ArrayBuffer[(String, Int)], data: (String, Int)) => {
        list.appended(data)
          .sortBy((d: (String, Int)) => d._2)(Ordering.Int.reverse)
          .take(3)
      }, (list1: ArrayBuffer[(String, Int)], list2: ArrayBuffer[(String, Int)]) => {
        list1.appendedAll(list2)
          .sortBy((d: (String, Int)) => d._2)(Ordering.Int.reverse)
          .take(3)
      })

    result.collect().foreach(println)
  }
  def topN2(): Unit = {

    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc: SparkContext = new SparkContext(sparkConf)

    // 1. 获取原始数据：时间戳，省份，城市，用户，广告
    val dataRDD: RDD[String] = sc.textFile("input/agent.log")

    // 2. 将原始数据进行结构的转换。时间戳，省份，城市，用户，广告 => ((省份，广告), 1 )
    val mapRDD: RDD[((String, String), Int)] = dataRDD.map(
      line => {
        val datas = line.split(" ")
        ((datas(1), datas(4)), 1)
      }
    )

    // 3. 将转换结构后的数据，进行分组聚合
    //    ((省份，广告), 1 ) => ((省份，广告), sum)
    val reduceRDD: RDD[((String, String), Int)] = mapRDD.reduceByKey(_ + _)

    // 4. 将聚合的结果进行结构的转换
    //    ((省份，广告), sum ) => (省份,(广告, sum))
    val newMapRDD: RDD[(String, (String, Int))] = reduceRDD.map {
      case ((prv, ad), sum) => (prv, (ad, sum))
    }

    // 5. 将转换结构后的数据根据省份进行分组
    //    ( 省份, 【( 广告A, sumA )，( 广告B, sumB )】 )
    val groupRDD: RDD[(String, Iterable[(String, Int)])] = newMapRDD.groupByKey()

    // 6. 将分组后的数据组内排序（降序），取前3名
    val resultRDD = groupRDD.mapValues(
      iter => {
        // 需要确认toList会不会将所有的数据加载到内存中来???!!!
        iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)
      }
    )
    // 7. 采集数据打印在控制台
    resultRDD.collect().foreach(println)
    sc.stop()
  }

}
