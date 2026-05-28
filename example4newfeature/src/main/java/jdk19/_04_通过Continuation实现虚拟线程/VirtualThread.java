package jdk19._04_通过Continuation实现虚拟线程;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

import java.util.concurrent.atomic.AtomicInteger;

public class VirtualThread {

    // 如果将调度程序剥离开的话, 那么虚拟线程实际上就是一个Continuation
    public static final ContinuationScope SCOPE = new ContinuationScope("VirtualThread");
    private final Continuation continuation;

    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    // 为了方便识别虚拟线程, 可以给他添加一个id
    private final int id;

    public VirtualThread(Runnable runnable) {
        continuation = new Continuation(SCOPE, runnable);
        id = COUNTER.getAndIncrement();
    }

    public void run() {
        System.out.println("Virtual Thread " + id  + " is running on " + Thread.currentThread().getName());
        continuation.run();
    }
}
