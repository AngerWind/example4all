import java.util

import World.left

import scala.collection.mutable.Set

/**
 * @Title World
 * @author tiger.shen
 * @date 2020/12/24 16:08
 * @version v1.0
 * @description
 */

object World {
  def main(args: Array[String]): Unit = {
    val world = "hello world"
    val world1 = new World()

    for (c <- world) {

    }
    val one = List(1, 2, 3)
    val two = List(2, 3, 4)
    val map: Map[Int, String] = Map(1 -> "hello")
    val map1 = map + (2->"haha")
    var three = one ::: two
    val pair = ("hello", "world", "scala", "!!!")
    pair._1
    println(three)



    var jetSet = Set("hello", "asdk", 1)
    jetSet += "lear"



  }

  var left = "sndk"

  def les = {
    new World().less
  }
}

class World {
  private var a: Int = _
  private def less = "a"
  def count() = "a"
  def more = {
    val word = "hello world"
  }
  def hello: String = {
    "a" + left
  }
  def stander(x: Int, y: String): Unit = {

  }


}


