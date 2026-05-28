package jdk11;

public class _01_String新增方法 {


    public static void main1(String[] args) {
        var str = " abc ";
        System.out.println(str.trim()); // 移除字符串左右两边的空白字符

        // 但是trim对于 Unicode空白字符并不好使
        var c = '\u2000'; // Unicode空白字符
        var str1 = c + "abc" + c;
        System.out.println(str1.trim());

        System.out.println(str1.strip()); // 取出左右的空白字符
        System.out.println(str1.stripTrailing()); // 去除后面的空白字符
        System.out.println(str1.stripLeading()); // 去除前面的空白字符串
    }

    public static void main2(String[] args) {
        var str = "  ";
        System.out.println(str.isBlank()); // 新增isBlank方法
    }

    public static void main3(String[] args) {
        var str = "abc";
        System.out.println(str.repeat(3)); // 新增repeat方法, abcabcabc
    }

}
