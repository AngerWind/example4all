package com.tiger.java8;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title HelloInterfaceImpl
 * @date 2021/6/3 14:14
 * @description
 */
public class HelloInterfaceImpl extends SimpleAbstractClass implements HelloInterface, HelloInterface2 {

    @Override
    public void sayHello() {

        // 接口中的static方法不会被继承， 还是要通过类名.方法名调用
        HelloInterface.say();

        // 抽象类中的static方法可以被继承
        say1();
    }

    @Override
    public void sayHello(String say) {

    }

    public static void main(String[] args) {
        HelloInterfaceImpl helloInterface = new HelloInterfaceImpl();
        helloInterface.play("s");

    }
}
