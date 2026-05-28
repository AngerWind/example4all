package jdk19._04_通过Continuation实现虚拟线程;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadScheduler {

    // 调度器以fifo的方式来执行虚拟线程
    private final Queue<VirtualThread> queue = new ConcurrentLinkedQueue<>();
    // 调度器拥有少量的系统线程, 来执行虚拟线程
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    // 类似ThreadLocal, 可以让每个系统线程访问当前正在执行的虚拟线程
    public static final ScopedValue<VirtualThread> CURRENT_VIRTUAL_THREAD = ScopedValue.newInstance();

    public void start() {
        while (true) {
            // 调度器不断地从队列中取出虚拟线程, 并执行
            if (!queue.isEmpty()) {
                var vt = queue.remove();
                executor.submit(() -> {
                    // 将当前执行的虚拟线程vt, 设置到scopedValue中
                    ScopedValue.where(CURRENT_VIRTUAL_THREAD, vt).run(vt::run);
                });
            }
        }
    }

    public void schedule(VirtualThread vt) {
        queue.add(vt);
    }
}
