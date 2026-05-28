package jdk11;

public class _02_lambda表达式中的类型推断 {

    public static void main(String[] args) {
        // 传统写法
        MyInterface myInterface = (String a, int b) -> {};

        // 自动类型推断
        MyInterface myInterface1 = (var a, var b) -> {};

        // 没什么屌用, 因为写lambda的时候, 类型会省略
        MyInterface myInterface2 = (a, b) -> {};
    }

    @FunctionalInterface
    public interface MyInterface {
        void m1(String a, int b);
    }
}
