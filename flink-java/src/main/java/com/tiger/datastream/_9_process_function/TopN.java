package com.tiger.datastream._9_process_function;

import com.google.common.collect.Maps;
import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class TopN {

    /**
     * 统计10秒内的最热门的n个url, 每5秒统计一次 类似微博的热搜榜
     */
    @Test
    public void topN() {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> source = env.addSource(new MultiParallelSource())
            // 处理乱序数据, 因为MultiParallelSource中的timestamp本来就是递增的, 所以最大乱序数据可以是0
            .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                    }
                }));

        // 按照url分组, 统计每个url的个数
        SingleOutputStreamOperator<Tuple4<String, Long, Long, Long>> eachUrlCount =
            source.keyBy(Event::getUrl).window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5))).aggregate(
                // 统计每个url的个数
                new AggregateFunction<Event, Long, Long>() {
                    @Override
                    public Long createAccumulator() {
                        return 0L;
                    }

                    @Override
                    public Long add(Event value, Long accumulator) {
                        return ++accumulator;
                    }

                    @Override
                    public Long getResult(Long accumulator) {
                        return accumulator;
                    }

                    @Override
                    public Long merge(Long a, Long b) {
                        return null;
                    }
                },
                // 最后输出url和个数, 以及窗口信息
                new WindowFunction<Long, Tuple4<String, Long, Long, Long>, String, TimeWindow>() {

                    @Override
                    public void apply(String key, TimeWindow window, Iterable<Long> input,
                        Collector<Tuple4<String, Long, Long, Long>> out) throws Exception {
                        out.collect(Tuple4.of(key, input.iterator().next(), window.getStart(), window.getEnd()));
                    }
                });

        // 打印输出
        eachUrlCount.print();

        // 对于同一个窗口的数据, 统计topN
        // eachUrlCount.key

    }

    /**
     * 使用全窗口函数
     */
    public void windowAllWithProcess() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> eventStream =
            env.addSource(new MultiParallelSource()).assignTimestampsAndWatermarks(WatermarkStrategy
                .<Event>forMonotonousTimestamps().withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                    }
                }));
        // 只需要 url 就可以统计数量，所以转换成 String 直接开窗统计
        SingleOutputStreamOperator<String> result = eventStream.map(new MapFunction<Event, String>() {
            @Override
            public String map(Event value) throws Exception {
                return value.getUrl();
            }
        })
            // 非按键开窗, 开滑动窗口
            .windowAll(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
            .process(new ProcessAllWindowFunction<String, String, TimeWindow>() {
                @Override
                public void process(Context context, Iterable<String> elements, Collector<String> out)
                    throws Exception {
                    HashMap<String, Long> urlCountMap = new HashMap<>();
                    // 遍历窗口中数据，将浏览量保存到一个 HashMap 中
                    for (String url : elements) {
                        if (urlCountMap.containsKey(url)) {
                            long count = urlCountMap.get(url);
                            urlCountMap.put(url, count + 1L);
                        } else {
                            urlCountMap.put(url, 1L);
                        }
                    }
                    ArrayList<Tuple2<String, Long>> mapList = new ArrayList<Tuple2<String, Long>>();
                    // 将浏览量数据放入 ArrayList，进行排序
                    for (String key : urlCountMap.keySet()) {
                        mapList.add(Tuple2.of(key, urlCountMap.get(key)));
                    }
                    mapList.sort(new Comparator<Tuple2<String, Long>>() {
                        @Override
                        public int compare(Tuple2<String, Long> o1, Tuple2<String, Long> o2) {
                            return o2.f1.intValue() - o1.f1.intValue();
                        }
                    });
                    // 取排序后的前两名，构建输出结果
                    StringBuilder result = new StringBuilder();
                    result.append("========================================\n");
                    for (int i = 0; i < 2; i++) {
                        Tuple2<String, Long> temp = mapList.get(i);
                        String info = "浏览量 No." + (i + 1) + " url： " + temp.f0 + " 浏览量： " + temp.f1 + " 窗 口 开 始 时 间 ： "
                            + new Timestamp(context.window().getStart()) + " 窗 口 结 束 时 间 ： "
                            + new Timestamp(context.window().getEnd()) + "\n";
                        result.append(info);
                    }
                    result.append("========================================\n");
                    out.collect(result.toString());
                }
            });
        result.print();
        env.execute();
    }

    /**
     * 使用增量聚合函数和全窗口函数
     */
    public void windowAllWithAggregate() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> eventStream =
            env.addSource(new MultiParallelSource()).assignTimestampsAndWatermarks(WatermarkStrategy
                .<Event>forMonotonousTimestamps().withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                    }
                }));

        AggregateFunction<String, HashMap<String, Long>, HashMap<String, Long>> aggregateFunction =
            new AggregateFunction<String, HashMap<String, Long>, HashMap<String, Long>>() {
                @Override
                public HashMap<String, Long> createAccumulator() {
                    return Maps.newHashMap();
                }

                @Override
                public HashMap<String, Long> add(String url, HashMap<String, Long> urlCountMap) {
                    // 遍历窗口中数据，将浏览量保存到一个 HashMap 中
                    if (urlCountMap.containsKey(url)) {
                        long count = urlCountMap.get(url);
                        urlCountMap.put(url, count + 1L);
                    } else {
                        urlCountMap.put(url, 1L);
                    }
                    return urlCountMap;
                }

                @Override
                public HashMap<String, Long> getResult(HashMap<String, Long> accumulator) {
                    return accumulator;
                }

                @Override
                public HashMap<String, Long> merge(HashMap<String, Long> a, HashMap<String, Long> b) {
                    return null;
                }
            };

        ProcessAllWindowFunction<HashMap<String, Long>, String, TimeWindow> processAllWindowFunction =
            new ProcessAllWindowFunction<HashMap<String, Long>, String, TimeWindow>() {
                @Override
                public void process(Context context, Iterable<HashMap<String, Long>> elements, Collector<String> out)
                    throws Exception {
                    HashMap<String, Long> urlCountMap = elements.iterator().next();

                    ArrayList<Tuple2<String, Long>> mapList = new ArrayList<Tuple2<String, Long>>();
                    // 将浏览量数据放入 ArrayList，进行排序
                    for (String key : urlCountMap.keySet()) {
                        mapList.add(Tuple2.of(key, urlCountMap.get(key)));
                    }
                    mapList.sort(new Comparator<Tuple2<String, Long>>() {
                        @Override
                        public int compare(Tuple2<String, Long> o1, Tuple2<String, Long> o2) {
                            return (int)(o2.f1 - o1.f1);
                        }
                    });
                    // 取排序后的前两名，构建输出结果
                    StringBuilder result = new StringBuilder();
                    result.append("========================================\n");
                    for (int i = 0; i < 2; i++) {
                        Tuple2<String, Long> temp = mapList.get(i);
                        String info = "浏览量 No." + (i + 1) + " url： " + temp.f0 + " 浏览量： " + temp.f1 + " 窗 口 开 始 时 间 ： "
                            + new Timestamp(context.window().getStart()) + " 窗 口 结 束 时 间 ： "
                            + new Timestamp(context.window().getEnd()) + "\n";
                        result.append(info);
                    }
                    result.append("========================================\n");
                    out.collect(result.toString());
                }
            };

        // 只需要 url 就可以统计数量，所以转换成 String 直接开窗统计
        SingleOutputStreamOperator<String> result = eventStream.map(new MapFunction<Event, String>() {
            @Override
            public String map(Event value) throws Exception {
                return value.getUrl();
            }
        })
            // 非按键开窗, 开滑动窗口
            .windowAll(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
            .aggregate(aggregateFunction, processAllWindowFunction);

        result.print();
        env.execute();
    }
}
