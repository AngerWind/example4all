package com.tiger.datastream._8_window.window_assigners.keyed_window;

import com.tiger.datastream._7_watermark.EmitWatermarkInSource;
import com.tiger.pojo.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.junit.Test;

public class GlobalWindowTest {

    @Test
    public void keyedTimeWindows() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> stream = env.addSource(new EmitWatermarkInSource.SourceWithWatermark());

        WindowedStream<Event, String, org.apache.flink.streaming.api.windowing.windows.GlobalWindow> windowedStream = stream.keyBy(Event::getUser)
                // 需要注意使用全局窗口，必须自行定义触发器才能实现窗口计算，否则起不到任何作用
                .window(GlobalWindows.create());



        env.execute();

    }
}
