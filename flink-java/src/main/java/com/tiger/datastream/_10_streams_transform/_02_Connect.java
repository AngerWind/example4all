package com.tiger.datastream._10_streams_transform;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;
import org.junit.Test;


/**
 * connect连接两条流, 其实内部是一国两制的形式
 */
public class _02_Connect {

    @Test
    public void connect() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Integer> stream1 = env.fromElements(1, 2, 3);
        DataStream<Long> stream2 = env.fromElements(1L, 2L, 3L);
        ConnectedStreams<Integer, Long> connectedStreams = stream1.connect(stream2);

        // ConnectedStreams调用协同处理操作转换成SingleOutputStreamOperator
        SingleOutputStreamOperator<String> result = connectedStreams.map(new CoMapFunction<Integer, Long, String>() {
            @Override
            public String map1(Integer value) {
                return "Integer: " + value;
            }

            @Override
            public String map2(Long value) {
                return "Long: " + value;
            }
        });
        result.print();
        env.execute();
    }

    /**
     * 我们可以实现一个实时对账的需求，也就是
     * app 的支付操作和第三方的支付操作的一个双流 Join。
     * App 的支付事件和第三方的支付事件将会互相等待 5 秒钟，如果等不来对应的支付事件，那么就输出报警信息
     */
    @Test
    public void billCheck() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 来自 app 的支付日志
        SingleOutputStreamOperator<Tuple3<String, String, Long>> appStream =
            env.fromElements(Tuple3.of("order-1", "app", 1000L), Tuple3.of("order-2", "app", 2000L))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forMonotonousTimestamps()
                    .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                        @Override
                        public long extractTimestamp(Tuple3<String, String, Long> element, long recordTimestamp) {
                            return element.f2;
                        }
                    }));
        // 来自第三方支付平台的支付日志
        SingleOutputStreamOperator<Tuple4<String, String, String, Long>> thirdpartStream = env
            .fromElements(Tuple4.of("order-1", "third-party", "success", 3000L),
                Tuple4.of("order-3", "third-party", "success", 4000L))
            .assignTimestampsAndWatermarks(
                WatermarkStrategy.<Tuple4<String, String, String, Long>>forMonotonousTimestamps()
                    .withTimestampAssigner(new SerializableTimestampAssigner<Tuple4<String, String, String, Long>>() {
                        @Override
                        public long extractTimestamp(Tuple4<String, String, String, Long> element,
                            long recordTimestamp) {
                            return element.f3;
                        }
                    }));
        // 检测同一支付单在两条流中是否匹配，不匹配就报警
        appStream.connect(thirdpartStream).keyBy(data -> data.f0, data -> data.f0).process(new OrderMatchResult())
            .print();

        // 下面这种写法与上面这种写法是一样的
        // appStream.keyBy(data -> data.f0).connect(thirdpartStream.keyBy(data -> data.f0)).process(new OrderMatchResult())
        //         .print();
        env.execute();
    }

    // 自定义实现 CoProcessFunction
    public static class OrderMatchResult
        extends CoProcessFunction<Tuple3<String, String, Long>, Tuple4<String, String, String, Long>, String> {
        // 定义状态变量，用来保存已经到达的事件
        private ValueState<Tuple3<String, String, Long>> appEventState;
        private ValueState<Tuple4<String, String, String, Long>> thirdPartyEventState;

        @Override
        public void open(Configuration parameters) throws Exception {
            appEventState =
                getRuntimeContext().getState(new ValueStateDescriptor<Tuple3<String, String, Long>>("app-event",
                    Types.TUPLE(Types.STRING, Types.STRING, Types.LONG)));
            thirdPartyEventState =
                getRuntimeContext().getState(new ValueStateDescriptor<Tuple4<String, String, String, Long>>(
                    "thirdparty-event", Types.TUPLE(Types.STRING, Types.STRING, Types.STRING, Types.LONG)));
        }

        @Override
        public void processElement1(Tuple3<String, String, Long> value, Context ctx, Collector<String> out)
            throws Exception {
            // 看另一条流中事件是否来过
            if (thirdPartyEventState.value() != null) {
                out.collect(" 对 账 成 功 ： " + value + " " + thirdPartyEventState.value());
                // 清空状态
                thirdPartyEventState.clear();
            } else {
                // 更新状态
                appEventState.update(value);
                // 注册一个 5 秒后的定时器，开始等待另一条流的事件
                ctx.timerService().registerEventTimeTimer(value.f2 + 5000L);
            }
        }

        @Override
        public void processElement2(Tuple4<String, String, String, Long> value, Context ctx, Collector<String> out)
            throws Exception {
            if (appEventState.value() != null) {
                out.collect("对账成功： " + appEventState.value() + " " + value);
                // 清空状态
                appEventState.clear();
            } else {
                // 更新状态
                thirdPartyEventState.update(value);
                // 注册一个 5 秒后的定时器，开始等待另一条流的事件
                ctx.timerService().registerEventTimeTimer(value.f3 + 5000L);
            }
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            // 定时器触发，判断状态，如果某个状态不为空，说明另一条流中事件没来
            if (appEventState.value() != null) {
                out.collect("对账失败： " + appEventState.value() + " " + "第三方支付 平台信息未到");
            }
            if (thirdPartyEventState.value() != null) {
                out.collect("对账失败： " + thirdPartyEventState.value() + " " + "app 信息未到");
            }
            appEventState.clear();
            thirdPartyEventState.clear();
        }
    }

}
