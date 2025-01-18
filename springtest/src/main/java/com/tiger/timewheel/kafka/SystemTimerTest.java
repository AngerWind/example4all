package com.tiger.timewheel.kafka;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2023/12/21
 * @description
 */
public class SystemTimerTest {

    public static void main(String[] args) throws Exception {
        SystemTimer timer = new SystemTimer("test", 1, 20, System.currentTimeMillis());
        for (int i = 0; i < 25; i++) {
            // 添加任务，每个任务间隔1s
            timer.add(new TimerTask(i+1) {
                @Override
                public void run() {
                    System.out.println("运行testTask的时间: " + System.currentTimeMillis());
                }
            });
        }

        while (true) {
            try {
                // 驱动时间轮线程间隔1s驱动
                timer.advanceClock(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
