package com.tiger.spark._3_framework.controller

import com.tiger.spark._3_framework.service.WordCountService

/**
 * @author Tiger.Shen
 * @date 2024/7/12
 * @description
 * @version 1.0
 */
class WordCountController {

  private val wordCountService: WordCountService = new WordCountService()

  def execute(): Unit = {
    val array: Array[(String, Int)] = wordCountService.wordCount()
    array.foreach(println)
  }

}
