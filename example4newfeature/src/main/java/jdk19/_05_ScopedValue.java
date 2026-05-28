package jdk19;

public class _05_ScopedValue {

    /**
     * ThreadLocal的限制
     * 1. ThreadLocal变量是可变的，任何运行在当前线程中的代码都可以修改该变量的值，很容易产生一些难以调试的bug。
     *
     * 2. ThreadLocal变量的生命周期会很长。当使用ThreadLocal变量的set方法，为当前线程设置了值之后，
     *    这个值在线程的整个生命周期中都会保留，直到调用remove方法来删除。但是绝大部分开发人员不会主动调用remove来进行删除，这可能造成内存泄漏。
     *
     * 3. ThreadLocal变量可以被继承。如果一个子线程从父线程中继承ThreadLocal变量，
     *    那么该子线程需要独立存储父线程中的全部ThreadLocal变量，这会产生比较大的内存开销。
     */

    /**
     * 虚拟线程的特点是数量巨大，!!!!但是每个虚拟线程的生命周期较短，因此不容易产生内存泄漏问题。!!!!
     * 但是线程继承所带来的内存开销会更大。为了解决这些问题便孵化了ScopedValue，ScopedValue具备ThreadLocal的核心特征，
     * 也就是每个线程只有一个值。
     * !!!!与ThreadLocal不同的是，ScopedValue是不可变的，并且有确定的作用域，!!!!
     * 这也是名字中scoped的含义。
     *
     */
    public static final ScopedValue<String> USER = ScopedValue.newInstance();
    public static final ScopedValue<Integer> AGE = ScopedValue.newInstance();

    public static void main(String[] args) {
        // 调用where来将一个ScopedValue和值进行绑定
        // 然后调用 run / call 方法来执行代码块, 在代码块中可以访问ScopedValue的值
        // where可以链式调用, 表示一次绑定多个值到同一个调用栈中
        ScopedValue.where(USER, "zhangsan").where(AGE, 18).call(() -> {
            return getUser();
        });
    }

    public static String getUser() {
        // 在同一个调用栈中, 重复设置ScopedValue的值, 不会覆盖
        // 而是会创建一个嵌套的作用域，新的值仅在嵌套的作用域中有效
        String user = ScopedValue.where(USER, "lisi").call(() -> {
            return USER.get();
        });
        return USER.get();
    }
}
