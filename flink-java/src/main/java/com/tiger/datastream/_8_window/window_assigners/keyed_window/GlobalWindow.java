package com.tiger.datastream._8_window.window_assigners.keyed_window;

import com.tiger.datastream._7_watermark.EmitWatermarkInSource;
import com.tiger.pojo.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.junit.Test;

public class GlobalWindow {

    @Test
    public void keyedTimeWindows() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> stream = env.addSource(new EmitWatermarkInSource.SourceWithWatermark());

        stream.keyBy(Event::getUser)
                // 全局窗口
                .window(GlobalWindows.create());

        env.execute();

    }
}
