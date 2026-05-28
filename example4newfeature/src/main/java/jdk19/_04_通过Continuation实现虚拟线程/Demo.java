package jdk19._04_通过Continuation实现虚拟线程;


// https://www.bilibili.com/video/BV18hJczSEJU/?spm_id_from=333.1007.tianma.3-1-7.click&vd_source=f79519d2285c777c4e2b2513f5ef101a
public class Demo {

    public static final VirtualThreadScheduler SCHEDULER = new VirtualThreadScheduler();
    public static void main(String[] args) {
        new Thread(SCHEDULER::start).start();

        var vt1 = new VirtualThread(() -> {
            System.out.println("1.1");
            System.out.println("1.2");
            WaitingOperation.perform("network", 2);
            System.out.println("1.3");
            System.out.println("1.4");
        });

        var vt2 = new VirtualThread(() -> {
            System.out.println("2.1");
            System.out.println("2.2");
            WaitingOperation.perform("disk", 5);
            System.out.println("2.3");
            System.out.println("2.4");
        });

        SCHEDULER.schedule(vt1);
        SCHEDULER.schedule(vt2);
    }
}
