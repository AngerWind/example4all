package jdk18;

public class _03_新增snippet代码注释 {

    /**
     * 之前我们如果需要在注释中写代码的话, 需要标记为code, 如果多行代码的话, 会比较麻烦
     *
     * <code>System.out.println("hello world");</code>
     */
    /**
     * 现在我们可以使用snippet注解来写代码了
     * {@snippet :
     *     public static void main(String[] args) {
     *         System.out.println("hello world");
     *     }
     * }
     */
    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
