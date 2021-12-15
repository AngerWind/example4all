package object

// 只能有public的抽象方法
interface Parent {
    hello()
}

class Child implements Parent {

    @Override
    hello() { }
}

tr