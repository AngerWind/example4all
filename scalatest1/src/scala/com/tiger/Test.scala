package com.tiger

/**
 * @Title Test
 * @author tiger.shen
 * @date 2020/12/24 16:10
 * @version v1.0
 * @description
 */
object Test {

  val more = 7

  def m(x: Int): Int ={
    var a :String = null
    a = "hello";
    return  x + 1
  }

  val f = (x: Int) => {
    x + more
  }

  def f(x: Int): String = x.toString

  val a = List(1, 2, 3)
  val b: Unit = for (i <- a if i > 1) {
    print(i)
  }

  def hello: Float = {
    return 2.2f
  }
  hello

  def main(args: Array[String]): Unit = {

  }


}
