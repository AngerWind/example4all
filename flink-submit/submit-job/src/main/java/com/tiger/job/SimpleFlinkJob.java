package com.tiger.job;



import com.tiger.pojo.Event;
import com.tiger.source.SingleParallelSource;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SimpleFlinkJob {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 这里你可以写你的作业逻辑：
        env.setParallelism(1);
        DataStreamSource<Event> source = env.addSource(new SingleParallelSource());

        SingleOutputStreamOperator<Event> operator = source.filter((FilterFunction<Event>) event -> {
            System.out.println("filter: " + event);
            return true;
        });

        SingleOutputStreamOperator<String> operator1 = operator.map((MapFunction<Event, String>)event -> {
            System.out.println("map: " + event);
            return event.getUser();
        });

        operator1.print();

        JobExecutionResult result = env.execute("simple flink job");

        System.out.println("提交任务");
    }
}
