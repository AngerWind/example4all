package jdk21;

public class _04_switch增强 {

    public static void main(String[] args) {
        Object o = "hello world";


        // switch添加了when的支持
        String str = switch (o) {
            case null -> "null";
            case String s
                    when s.equalsIgnoreCase("yes") -> "yes";
            case String s
                    when s.equalsIgnoreCase("no") -> "no";
            default ->
                    "other";
        };

        System.out.println(str);
    }

}
