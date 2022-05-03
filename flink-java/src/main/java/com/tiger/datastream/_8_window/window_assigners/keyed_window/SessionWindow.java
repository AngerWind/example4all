package com.tiger.datastream._8_window.window_assigners.keyed_window;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.Test;

import com.tiger.datastream._7_watermark.EmitWatermarkInSource;
import com.tiger.pojo.Event;

public class SessionWindow {

    @Test
    public void sessionWindows() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> stream = env.addSource(new EmitWatermarkInSource.SourceWithWatermark());

        stream.keyBy(Event::getUser)
            // 处理时间会话窗口, 会话超时时间为5分钟
            .window(ProcessingTimeSessionWindows.withGap(Time.minutes(5)));

        // 事件时间会话窗口, 会话超时时间为5分钟
        // .window(EventTimeSessionWindows.withGap(Time.minutes(5)));

        env.execute();
    }
}
