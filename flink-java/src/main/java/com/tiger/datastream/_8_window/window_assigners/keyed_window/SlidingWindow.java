package com.tiger.datastream._8_window.window_assigners.keyed_window;

import com.tiger.datastream._7_watermark.EmitWatermarkInSource;
import com.tiger.pojo.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.Test;

public class SlidingWindow {

    /**
     * 滑动时间窗口
     */
    @Test
    public void slidingTimeWindows() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> stream = env.addSource(new EmitWatermarkInSource.SourceWithWatermark());

        stream.keyBy(Event::getUser)
            // 滑动事件时间窗口, 第一个参数是窗口大小, 第二个参数是滑动步长, 第三个窗口是偏移量, 这样表示[00:05:00, 01:05:00)一个窗口, 并且每五分钟一个步长
            .window(SlidingEventTimeWindows.of(Time.hours(1), Time.minutes(5), Time.minutes(5)));

        // 滑动处理时间窗口
        // .window(SlidingProcessingTimeWindows.of(Time.hours(1), Time.minutes(5), Time.minutes(5)));

        env.execute();

    }

    /**
     * 滑动计数窗口
     */
    @Test
    public void slidingCountWindows() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> stream = env.addSource(new EmitWatermarkInSource.SourceWithWatermark());

        stream.keyBy(Event::getUser)
                // 滑动计数窗口
                .countWindow(10, 3);

        env.execute();

    }
}
