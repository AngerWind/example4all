package com.tiger

/**
 * @Title Rational
 * @author tiger.shen
 * @date 2020/12/25 18:04
 * @version v1.0
 * @description
 */
class Rational (n: Int, d: Int) {
  print(s"n is $n, d is $d, rational is $this")
  require(d != 0)

  private val g = gcd(n.abs, d.abs)
  var number: Int = n / g
  var denom: Int = d / g

  override def toString: String = s"$number / $denom"

  // 辅助构造函数的第一行必须是调用同类的别的早于调用构造函数定义的构造函数
  // 这样保证构造函数的最终调用将结束与对主构造函数的调用
  def this(n: Int) = {
    this(n, 1)
  }

  def unary_- = new Rational(-this.number, this.denom)

  private def gcd(a: Int, b: Int): Int = {
    if (b == 0)
      a
    else
      gcd(b, a % b)
  }

  def add(that: Rational): Rational = {
    new Rational(that.denom * number + that.number * denom, that.denom * this.denom)
  }

  // 当调用 x + y时也需要遵从操作符优先级规则
  def +(that: Rational) = this.add(that)

  def *(that: Rational) = new Rational(this.number * that.number, this.denom * that.denom)
  def *(i: Int) = new Rational(this.number * i, this.denom)



  def lessThan(that: Rational) = {
    this.number * that.denom < that.number * this.denom
  }

  def maxThan(that: Rational) = !this.lessThan(that)

  def max(that: Rational) = {
    if (lessThan(that))
      that
    else
      this
  }

}

object Rational {
  def main(args: Array[String]): Unit = {
    val rational = new Rational(1, 2)
    val rational1 = new Rational(2, 3)
    val rational2 = rational * 2
    val result = rational add rational1
    println(result)
    println(-result)
    println(null == null)

    val add = (_: Int) + (_: Int)
    val addd = print _
    println(addd)
    List("")
  }
}
