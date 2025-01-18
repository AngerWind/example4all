package com.tiger.datastream._8_window.window_assigners.keyed_window;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.junit.Test;

import com.tiger.datastream._7_watermark.EmitWatermarkInSource;
import com.tiger.pojo.Event;

public class GlobalWindowTest {

    @Test
    public void keyedTimeWindows() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> stream = env.addSource(new EmitWatermarkInSource.SourceWithWatermark());

        WindowedStream<Event, String, GlobalWindow> windowedStream = stream.keyBy(Event::getUser)
                // 需要注意使用全局窗口，必须自行定义触发器才能实现窗口计算，否则起不到任何作用
                .window(GlobalWindows.create());


        /**
         * onElement, onProcessingTime, onEventTime都可以返回一个TriggerResult用来表示是否执行窗口的计算
         * TriggerResult.CONTINUE 表示什么也不做
         * TriggerResult.FIRE_AND_PURGE 表示触发窗口的计算, 并清除窗口中的数据和状态
         * TriggerResult.PURGE 表示清除窗口中的状态
         * TriggerResult.FIRE 表示触发窗口的计算, 但是不清除窗口中的数据与状态
         */
        windowedStream.trigger(new Trigger<Event, GlobalWindow>() {
            /**
             * 每当有元素加入到窗口的时候都会被调用
             * @param element 到来的元素
             * @param timestamp 从元素上提取出来的timestamp
             * @param window 当前窗口
             * @param ctx 一个上下文对象, 可以用来注册定时任务
             */
            @Override
            public TriggerResult onElement(Event element, long timestamp, GlobalWindow window, TriggerContext ctx) throws Exception {
                System.out.println("元素被添加到了当前窗口: " + element);
                System.out.println("元素携带的timestamp为: " + timestamp);

                long currentProcessingTime = ctx.getCurrentProcessingTime(); // 获取当前的process time
                long currentWatermark = ctx.getCurrentWatermark(); // 获取当前的watermark

                // 注册一个基于事件时间的定时任务, 当watermaker到达指定的时间之后, 会调用onEventTime
                // 这里是当前watermark 10s之后触发
                ctx.registerEventTimeTimer(ctx.getCurrentWatermark() + 10 * 1000);

                // 注册一个基于处理时间的定时任务, 当处理时间到达了指定的时间(单位为毫秒)之后, 会调用onProcessingTime
                // 这里指定的是当前时间10s之后触发
                ctx.registerProcessingTimeTimer(System.currentTimeMillis() + 10 * 1000);

                // 创建一个状态, 用来统计有多少个元素在窗口中
                ValueState<Integer> countState = ctx.getPartitionedState(new ValueStateDescriptor<Integer>("count", Integer.class));
                int count = countState.value() == null ? 0 : countState.value();
                count++;
                countState.update(count);

                return TriggerResult.CONTINUE;
            }

            /**
             * 当我们注册的基于事件的定时任务触发时, 会调用这个方法
             * time表示当前处理时间的毫秒数
             * window表示当前的这个窗口
             * ctx可以用来注册定时任务
             */
            @Override
            public TriggerResult onProcessingTime(long time, GlobalWindow window, TriggerContext ctx) throws Exception {
                System.out.println("当前的处理时间是: " + time);

                // 注册一个基于处理时间的定时任务, 当处理时间到达了指定的时间(单位为毫秒)之后, 会调用onProcessingTime
                // 这里指定的是当前时间10s之后触发
                ctx.registerProcessingTimeTimer(System.currentTimeMillis() + 10 * 1000);

                return null;
            }

            @Override
            public TriggerResult onEventTime(long time, GlobalWindow window, TriggerContext ctx) throws Exception {
                // 注册一个基于事件时间的定时任务, 当watermaker到达指定的时间之后, 会调用onEventTime
                // 这里是当前watermark 10s之后触发
                ctx.registerEventTimeTimer(ctx.getCurrentWatermark() + 10 * 1000);

                return null;
            }

            /**
             * 当上面的三个方法返回的TriggerResult为FIRE_AND_PURGE或者PURGE时, 会调用这个方法
             * 在这个方法中, 需要清除状态, 取消还没有执行的定时任务
             */
            @Override
            public void clear(GlobalWindow window, TriggerContext ctx) throws Exception {

                // 清除状态
                ctx.getPartitionedState(new ValueStateDescriptor<>("count", Integer.class)).clear();

                // 清除定时任务
                ctx.deleteEventTimeTimer(1000);
                ctx.deleteProcessingTimeTimer(1000);
            }
        }).max(1);



        env.execute();

    }
}
