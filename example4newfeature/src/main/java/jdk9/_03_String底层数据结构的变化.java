package jdk9;

public class _03_String底层数据结构的变化 {
    public static void main(String[] args) {
        // 之前String的底层数据结构是char[]
        // 在jdk9修改为了byte[]
        // 好处是对于纯英文的字符串来说, 可以节省一半的空间
        String s1 = "abc";
        String s2 = "abc";

        char c = 'a'; // 占用2个字节
        byte b = 97; // 占用1个字节
    }
}
