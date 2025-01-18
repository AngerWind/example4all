package com.tiger.datastream._10_streams_transform;

import java.util.Arrays;

import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.streaming.api.datastream.BroadcastConnectedStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/6/28
 * @description
 */
public class _03_BroadcastConnectedStreamTest {

    /**
     * 主数据流是传感器的读数 广播流是动态的传感器校准配置
     */
    // 定义传感器读数的类
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SensorReading {
        public String sensorId;
        public double reading;
    }

    // 定义校准参数的类
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CalibrationConfig {
        public String sensorId;
        public double calibrationFactor;
    }

    @Test
    public void broadcastConnectedStream() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 1. 定义主流, 是传感器读数
        DataStreamSource<SensorReading> mainStream = env.fromCollection(
            Arrays.asList(new SensorReading("1", 2.1), new SensorReading("2", 2.3), new SensorReading("3", 3.1)));

        // 2. 定义broadcastStream, 用于校准参数的流
        DataStreamSource<CalibrationConfig> broadcastStream = env.fromCollection(Arrays
            .asList(new CalibrationConfig("1", 2.0), new CalibrationConfig("2", 3.0), new CalibrationConfig("3", 4.0)));

        // 3. 定义需要广播的状态描述符,  需要两个泛型, 第一个是状态的key类型, 第二个是状态本身
        //  一般情况下, 状态的key就是状态的id, 用于从上下文中获取对应的状态
        MapStateDescriptor<String, CalibrationConfig> state =
            new MapStateDescriptor<>("broadcastState", String.class, CalibrationConfig.class);

        // 创建广播流
        BroadcastStream<CalibrationConfig> broadcastedCalibrationStream = broadcastStream.broadcast(state);

        // 连接主流和广播流
        BroadcastConnectedStream<SensorReading, CalibrationConfig> connectedStreams =
            mainStream.connect(broadcastedCalibrationStream);

        DataStream<SensorReading> calibratedReadings = connectedStreams
            .process(new KeyedBroadcastProcessFunction<String, SensorReading, CalibrationConfig, SensorReading>() {

                public static final String key = "calibratedState";

                @Override
                public void processElement(SensorReading reading, ReadOnlyContext ctx, Collector<SensorReading> out)
                    throws Exception {
                    // 从广播状态中获取校准参数
                    CalibrationConfig config = ctx.getBroadcastState(state).get(key);
                    if (config != null) {
                        // 应用校准因子
                        reading.reading = reading.reading * config.calibrationFactor;
                    }
                    out.collect(reading);
                }

                @Override
                public void processBroadcastElement(CalibrationConfig config, Context ctx, Collector<SensorReading> out)
                    throws Exception {
                    // 更新广播状态
                    ctx.getBroadcastState(state).put(key, config);
                }
            });

        env.execute();

    }
}
