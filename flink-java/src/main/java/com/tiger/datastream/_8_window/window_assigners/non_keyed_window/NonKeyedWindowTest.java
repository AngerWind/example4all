package com.tiger.datastream._8_window.window_assigners.non_keyed_window;

import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.junit.Test;

import com.tiger.datastream._7_watermark.EmitWatermarkInSource;
import com.tiger.pojo.Event;

/**
 * 非按键分区的滚动窗口
 * 非按键分区的窗口, 导致任务的并行度只能是1
 */
public class NonKeyedWindowTest {


    @Test
    public void tumblingTimeWindows() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> stream = env.addSource(new EmitWatermarkInSource.SourceWithWatermark());

        // 和按键分区的滚动窗口没什么区别, 只是这里调用windowAll而不是keyBy().window()
        AllWindowedStream<Event, TimeWindow> windowedStream = stream.windowAll(TumblingEventTimeWindows.of(Time.hours(1), Time.minutes(5)));



        env.execute();

    }
}
