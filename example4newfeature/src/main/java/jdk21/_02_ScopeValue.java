package jdk21;

import java.lang.ScopedValue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class _02_ScopeValue {

    /*
        产生的原因
            假设我有两个方法, saveUser(User user) 和 saveLog(Log log)
            为了让这两个使用同一个事务, 那么他们就必须使用同一个Connection, 所以我们就必须给这两个方法添加一个Connection参数
            saveUser(User user, Connection connection) 和 saveLog(Log log, Connection connection)

            在之前的场景下, 我们可以通过ThreadLocal来解决这个问题, 即每个方法都从TheadLocal中获取Connection
            这样就不需要给每个方法都添加一个Connection参数了

            但是ThreadLocal也有一些弊端
                1. 必须调用remove, 否则会内存泄露
                2. 如果子线程要使用父线程的ThreadLocal, 那么他会将其复制到自己的ThreadLocalMpa中, 会增加内存
                3. 只要能够获取ThreadLocal的地方, 都可以随时调用set, 这样就不容易分辨是按照什么顺序来更新共享数据的

            上面提到的几点在之前都不是什么大问题,
                1. 比如内存泄露, 只要调用remove就好了
                2. 比如子线程负责父线程的ThreadLocal, 因为我们的线程数量并不多, 所以增加的内存也不会太多

            但是随着虚拟线程的到来, 内存泄露问题不用担心了, 因为虚拟线程使用完就会很快释放,
            ThreadLocal中的数据也会被删除, 这样就不用调调用remove方法了

            但是虚拟线程的数量非常多, 通常是百万级别的, 如果每个虚拟线程都要拷贝父线程的ThreadLocal, 那么内存增加会变得非常的大
            为了解决这个问题, ScopedValues就出现了
     */

    // 泛型是数据的类型
    private static final ScopedValue<String> GIFT = ScopedValue.newInstance();

    public static void giveGift() {

        // 往ScopeValue中保存数据
        ScopedValue.Carrier carrier = ScopedValue.where(GIFT, Thread.currentThread().getName());

        // 在run中调用方法, 会拷贝一份副本, 与当前线程绑定, run方法结束之后, 副本就失效了
        // 所以在每个线程中调用run方法, 获取的都是本线程中设置的值
        carrier.run(() -> {
            recieveGift();
        });
    }
    public static void recieveGift() {
        GIFT.get(); // 这里获取到的是线程的名字

        ScopedValue.where(GIFT, "200").run(() -> {
            System.out.println(GIFT.get()); // 这里获取到的是200, 因为只有在run的时候, 才会将数据进行拷贝, 并与线程绑定
        });

        GIFT.get(); // 这里获取到的依然是线程的名字, 因为数据是和当前线程绑定的
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                giveGift();
            });
        }
    }

}
