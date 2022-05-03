package com.tiger.datastream._3_source.custom;

import com.tiger.pojo.Event;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

import java.util.Calendar;
import java.util.Random;

/**
 * 继承ParallelSourceFunction以实现多并行度
 */
public class MultiParallelSource implements ParallelSourceFunction<Event> {

    // 声明一个布尔变量，作为控制数据生成的标识位
    private Boolean running = true;

    @Override
    public void run(SourceContext<Event> ctx) throws Exception {
        Random random = new Random(); // 在指定的数据集中随机选取数据
        String[] users = {"Mary", "Alice", "Bob", "Cary"};
        String[] urls = {"./home", "./cart", "./fav", "./prod?id=1", "./prod?id=2"};

        while (running) {
            ctx.collect(new Event(users[random.nextInt(users.length)], urls[random.nextInt(urls.length)],
                    Calendar.getInstance().getTimeInMillis()));

            // 隔 1 秒生成一个，方便观测
            Thread.sleep(1000);
        }
    }

    /**
     * 上层调用cancel去停止run方法
     */
    @Override
    public void cancel() {
        this.running = false;
    }

}
