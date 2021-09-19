package com.tiger.shutdown_hook;




import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Test
 * @date 2021/8/19 11:51
 * @description
 */
public class ShutdownTest {

    public static void main(String[] args) throws IOException {
        System.out.println("process start...");
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("shutdown...");
        }));
        new Thread(() -> {
            int i = 0;
            while (true) {
                System.out.println(i++);
            }
        }).start();

        Signal signal = new Signal("TERM");
        SignalHandler oldHandler = Signal.handle(signal, new SignalHandler() {
            @Override
            public void handle(Signal sig) {
                System.out.println("handle sig");
            }
        });
        oldHandler.handle(signal);
        System.out.println("old handle");

        Signal.raise(signal);
    }
}
