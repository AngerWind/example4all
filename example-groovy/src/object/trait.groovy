package object

/**
 @Title trait
 @author Shen
 @date 2021/12/12 15:35
 @version v1.0
 @description
 */

// 类似于抽象类，主要用于定义胖接口
trait Father {
    int age
    abstract int plus(int num)
    int minus(int num) { plus(-num) }
}

trait Son extends Father {
    @Override
    int plus (int num) { age += num }
}