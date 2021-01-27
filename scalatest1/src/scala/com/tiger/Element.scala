package com.tiger

/**
 * @Title Element
 * @author tiger.shen
 * @date 2020/12/30 13:53
 * @version v1.0
 * @description
 */
abstract class Element {
  def contents: Array[String]

  def height: Int = contents.length

  def width: Int = if (height == 0) 0 else contents(0).length

  def above(that: Element): Element = new Element.ArrayElement(this.contents ++ that.contents)

  def biside(that: Element): Element =
    new Element.ArrayElement(
      for {
        (line1, line2) <- this.contents zip that.contents
      } yield line1 + line2
    )

  override def toString: String = contents.mkString("\n")

}


object Element {

  def elem(contents: Array[String]): Element = new ArrayElement(contents)

  def elem(chr: Char, width: Int, height: Int): Element = new UniformElement(chr, width, height)

  def elem(line: String): Element = new LineElement(line)

  private class ArrayElement(override val contents: Array[String]) extends Element {

  }

  private class LineElement(s: String) extends Element {
    override def width: Int = s.length

    override def height: Int = 1

    override val contents: Array[String] = Array(s)
  }

  private class UniformElement(
                        ch: Char, override val width: Int, override val height: Int
                      ) extends Element {
    private val line = ch.toString * width

    override def contents: Array[String] = Array.fill(height)(line)

  }

  def main(args: Array[String]): Unit = {
    new Array[String](3)
    Array("hello")
  }
}

/**
 * Created by gaosong on 2017-10-13
 */
object InnerClassDriver extends App{
  //调用外部类中的内部类对象
  println(new OuterClass().InnerObject.y)

  //调用外部类中的内部类
  val oc = new OuterClass
  var ic = new oc.InnerClass
  println(ic.x)

  //调用外部对象的内部类
  println((new OuterObject.InnerClass).m)

  //调用外部对象的内部对象
  println(OuterObject.InnerObject.n)

}

class OuterClass{
  class InnerClass{
    var x = 1
  }
  object InnerObject{
    val y = 2
  }
}
object OuterObject{
  class InnerClass{
    var m = 3
  }

  object InnerObject{
    var n = 4
  }
}
