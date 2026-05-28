package jdk14;

public class _01_instanceof模式匹配 {

    public static void main(String[] args) {
        Object o = "zhangsan";

        if (o instanceof String) {
            String str = (String)o;
            System.out.println(str.length());
        }

        // instanceof模式匹配
        if (o instanceof String str) {
            System.out.println(str.length());
        }
    }
}
