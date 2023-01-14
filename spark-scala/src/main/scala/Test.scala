

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

object Test {


    def main(args: Array[String]): Unit = {

      class Animal
      val myMap: collection.Map[String, Any] = Map("Number" -> 1, "Greeting" -> "Hello World",
        "Animal" -> new Animal)
      /* 下面注释的代码将会不通过编译
       * Any不能被当时Int使用
       */
      //val number:Int = myMap("Number")
      //println("number is " + number)
      //使用类型转换，可以通过编译
      val number: Int = myMap("Number").asInstanceOf[Int]
      println("number  is " + number)
      //下面的代码将会抛出ClassCastException
      val greeting: String = myMap("Number").asInstanceOf[String]

      val value: Map[Int, Int] = Map(1 -> 2)
      value(1)

    }

}


