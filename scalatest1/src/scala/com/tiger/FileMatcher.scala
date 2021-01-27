package com.tiger

import java.io.File

/**
 * @Title FileMatcher
 * @author tiger.shen
 * @date 2020/12/30 10:26
 * @version v1.0
 * @description
 */
object FileMatcher {

  private def filesHere = new File(".").listFiles

  def filesEnding(query: String): Array[File]
  = filesMatching((fileName: String) => {
    fileName.endsWith(query)
  })

  def filesContaining(query: String): Array[File]
  = filesMatching(_.contains(query))

  def filesRegex(query: String): Array[File]
  = filesMatching(_.matches(query))

  def filesMatching(mathcher: (String) => Boolean): Array[File]
  = for (file <- filesHere if mathcher(file.getName)) yield file


}
