package jdk14;


/**
 * 没有流行起来, 还不如lombok, 只有在简单的情况下使用
 */
public class _02_record关键字 {


    // 会自动加上 getter, toString, equals, hashCode, 全部参数的构造函数
    public static record User (String name, int age) {

        // name和age是final的, 无法修改, 也就没有setter了


        // 也可以自定义方法
        public void study() {
            System.out.println("study");
        }
    }

    public static void main(String[] args) {
        User zhangsan = new User("zhangsan", 18);

        int age1 = zhangsan.age(); // 只有getter, 没有setter
    }
}
