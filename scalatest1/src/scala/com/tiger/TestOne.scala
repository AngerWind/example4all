package com.tiger

/**
 * @Title TestOne
 * @author tiger.shen
 * @date 2020/12/24 17:21
 * @version v1.0
 * @description
 */
object TestOne {


  def main(args: Array[String]): Unit = {
    println(TestTwo.hello())
    val buf = new StringBuilder
    buf ++= "hello"
    buf += 'a'
    buf ++= "a"

    buf += 'a'

    println(buf)
    println("""hello \n hahah""")
    println(raw"hello \n hhha")

    val a = "hello"
    val b = "world"
    println(s"${hello}()")

  }

  def hello(): String ={
    "aaa"
  }

}

class TestOne {
  private val cache = Map[String, Int]()

}
