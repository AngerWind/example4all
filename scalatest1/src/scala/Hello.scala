/**
 * @Title Hello
 * @author tiger.shen
 * @date 2020/12/24 15:48
 * @version v1.0
 * @description
 */
class Hello {

  class Inner{

    protected  def p(): Unit ={

    }

    private def f(): Unit ={
      println("f")
    }

    class InnerMost{
      f()
    }

    def main(args: Array[String]): Unit = {
      println("hello")
    }

  }
}
