package com.tiger.jvm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.dsig.XMLSignatureException;

public class HeapTest {

    /**
     * 当前项目依赖spring , 必须要使用jdk11, 但是调试堆内存需要在jdk1.8才行
     * 因为jdk11默认采用g1作为垃圾收集器
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args)
            throws InterruptedException, XMLSignatureException, NoSuchAlgorithmException {
        // xms堆最小内存 xmx堆最大内存 xmn新生代占用内存, 其余为老年代
        // SurvivorRatio: eden和Survivor的比例
        // 启动时添加-Xms20m -Xmx20m -Xmn10m -XX:SurvivorRatio=8 -XX:+PrintGCDetails

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    byte[] bytes = new byte[1024];
                    // 复杂计算
                    for (int j = 0; j < 1000000; j++) {
                        String hash = "35454B055CC325EA1AF2126E27707052";
                        String password = "ILoveJava";
                        MessageDigest md = null;
                        try {
                            md = MessageDigest.getInstance("MD5");
                        } catch (NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        }
                        md.update(password.getBytes());
                        byte[] digest = md.digest();
                        String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.setName("thread-" + i);
            thread.start();
            threads.add(thread);
        }

        System.out.println("hello");

    }
}
