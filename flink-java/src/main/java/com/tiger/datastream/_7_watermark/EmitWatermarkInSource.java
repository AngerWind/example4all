package com.tiger.datastream._7_watermark;

import com.tiger.pojo.Event;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import java.util.Calendar;
import java.util.Random;

/**
 * 在source中直接生成watermark
 */
public class EmitWatermarkInSource {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.addSource(new SourceWithWatermark()).print();
        env.execute();
    }


    public static class SourceWithWatermark implements SourceFunction<Event> {
        private boolean running = true;

        @Override
        public void run(SourceContext<Event> sourceContext) throws Exception {
            Random random = new Random();
            String[] userArr = {"Mary", "Bob", "Alice"};
            String[] urlArr = {"./home", "./cart", "./prod?id=1"};

            while (running) {
                // 获取当前毫秒时间戳
                long currTs = Calendar.getInstance().getTimeInMillis();

                // 设置Event
                String username = userArr[random.nextInt(userArr.length)];
                String url = urlArr[random.nextInt(urlArr.length)];
                Event event = new Event(username, url, currTs);

                // 使用 collectWithTimestamp 方法将数据发送出去，并指明数据中的时间戳的字段
                sourceContext.collectWithTimestamp(event, event.getTimestamp());
                // 发送水位线
                // watermark是一个毫秒数!!!!!!!!!!!!!
                sourceContext.emitWatermark(new Watermark(event.getTimestamp() - 1L));
                Thread.sleep(1000L);
            }
        }

        @Override
        public void cancel() {
            running = false;
        }

    }
}
