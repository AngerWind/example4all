package jdk9;

public class _01_接口私有方法 {

    public interface MyInterface {
        default void method() {
            System.out.println("MyInterface method");
            method2();
        }

        static void method1() {
            System.out.println("MyInterface method1");
        }

        // JDK9中, 接口中允许定义私有方法
        private void method2() {
            method();
        }

    }
}
