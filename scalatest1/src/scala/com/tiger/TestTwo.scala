package com.tiger

/**
 * @Title TestTwo
 * @author tiger.shen
 * @date 2020/12/24 18:03
 * @version v1.0
 * @description
 */
object TestTwo {

  def hello() = "aaa"

  def main(args: Array[String]): Unit = {
    args.foreach(println)
    args.foreach(s => println(s + ""))
    for (s <- args){
      s.concat(hello)
    }

    def b(r: Symbol): Unit = {
      if (r.name == "hello"){

      }
    }

    b(Symbol("hello"))
    val two = new TestTwo("hello")
    println(two.title)
    two.title = hello
    println(two.title)
    two.title(1)

    def makeIncreaser(more: Int): Int => Int = _ + more



  }
  def ha(x: Int) = Math.abs(x)

  def a(x: String) = {
    for (i <- 2 to 4){

    }
    val array = new Array[String](3)
    array(1) = "hello"
    val arr = Nil

    val arrays = Array("hello", "world")
    if (x.equals("hello")){
      "aa"
    } else {
      "a"
    }
    var a = 2


  }

  def bb(a: String) = {
    a.concat("hello")
  }
  var c = "world"
  println(c == "hello")

}

class TestTwo(var title: String) {

}
