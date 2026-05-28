package jdk19;

import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class _01_虚拟线程 {

    // 虚拟线程的使用场景是io密集型的, 如果是cpu密集型的, 虚拟线程个数和cpu个数一致就可以了

    public static void main(String[] args) {
        // 传统的使用方式
        try (var executor = Executors.newCachedThreadPool()) {
            // 向executor中提交1000008个任务
            IntStream.range(0, 10).forEach(i -> {
                executor.submit(() -> {
                    // 模拟io操作, sleep 1s
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getName());
                });
            });

        }

        // 虚拟线程的使用方式
        // 任务在碰到io的时候, 会自动yield出来
        // 虚拟线程并不绑定到特定的系统线程上, 而是随机的
        // 在虚拟线程中, 调用Thread.currentThread()获得的是虚拟线程, 通过ThreadLocal获取到的也是虚拟线程的ThreadLocal
        // 在虚拟线程中, 不要使用synchronized, 会导致系统线程堵塞, 而是使用ReentrantLock
        // 虚拟线程和python中的协程类似, 都是自带一个栈对象frame, 在java中frame保存在堆中
        // 虚拟线程是守护进程, 不能被设置为非虚拟线程, 并且优先级默认为5, 不能修改(在未来或许可以)
        // 虚拟线程不支持stop, suspend, resume

        // 一个虚拟线程Executor, 他不是池化的, 每次都会创建一个新的虚拟线程
        // 虚拟线程用完直接丢弃, 不会复用
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // 向executor中提交1000008个任务
            IntStream.range(0, 100).forEach(i -> {
                executor.submit(() -> {
                    // 模拟io操作, sleep 1s
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    // todo 这里不知道为什么, 输出的名字是空的字符串
                    System.out.println(Thread.currentThread().getName());
                });
            });

            // 因为使用了try-with-resources, 在调用executor.close()的时候, 会确保所有的虚拟线程任务都已经完成
        }

        // 通过new Thead()创建的仍然是系统线程
        Thread thread2 = new Thread(() -> {});


        // 可以通过Thread.startVirtualThread()来创建虚拟线程
        Thread.startVirtualThread(() -> {
            // 获取到的是虚拟线程的名字
            System.out.println(Thread.currentThread().getName());
        });

        // 创建一个系统线程, 并启动
        Thread.ofPlatform().name("haha").start(() -> {});

        // 创建了一个虚拟线程, 但是不启动
        Thread thread = Thread.ofVirtual().name("zhangsan").unstarted(() -> {});
        thread.start();// 启动虚拟线程

        // 创建一个虚拟线程, 并且启动
        Thread thread1 = Thread.ofVirtual().name("lisi").start(() -> {});

        boolean isVirtual = thread1.isVirtual(); // 判断是否是虚拟线程

        // 虚拟线程的实际类型是 VirtualThread, 他是Thread的子类


    }
}

