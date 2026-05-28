package jdk19._04_通过Continuation实现虚拟线程;


import java.util.Timer;
import java.util.TimerTask;

/**
 * 模拟一个等待操作
 */
public class WaitingOperation {

    /**
     * @param name 等待对象的名称
     * @param duration 等待的时间, 秒
     */
    public static void perform(String name, int duration) {
        System.out.println("Waiting for " + name + " for " + duration + " seconds...");

        var virtualThread = VirtualThreadScheduler.CURRENT_VIRTUAL_THREAD.get();
        var timer = new Timer();

        // 在等待特定的时间之后, 重新将虚拟线程放入调度器中
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Demo.SCHEDULER.schedule(virtualThread);
                timer.cancel();
            }
        }, duration * 1000L
        );
    }
}
