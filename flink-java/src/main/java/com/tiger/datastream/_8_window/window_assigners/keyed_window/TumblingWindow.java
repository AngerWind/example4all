package com.tiger.datastream._8_window.window_assigners.keyed_window;

import com.tiger.datastream._7_watermark.EmitWatermarkInSource;
import com.tiger.pojo.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.Test;

public class TumblingWindow {

    /**
     * 滚动时间窗口
     */
    @Test
    public void tumblingTimeWindows() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> stream = env.addSource(new EmitWatermarkInSource.SourceWithWatermark());

        stream.keyBy(Event::getUser)
            // 滚动事件时间窗口, 第一个参数是窗口大小, 第二个参数是偏移量, 这样表示[00:05:00, 01:05:00)一个窗口
            .window(TumblingEventTimeWindows.of(Time.hours(1), Time.minutes(5)));

        // 滚动处理时间窗口
        // .window(TumblingProcessingTimeWindows.of(Time.hours(1), Time.minutes(5)));

        env.execute();

    }

    /**
     * 滚动计数窗口
     */
    @Test
    public void tumblingCountWindows() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> stream = env.addSource(new EmitWatermarkInSource.SourceWithWatermark());

        stream.keyBy(Event::getUser)
            // 滚动计数窗口
            .countWindow(10);

        env.execute();

    }
}
