package com.tiger.hadoop.mapreduce._2_writable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author Shen
 * @version v1.0
 * @Title FlowReduce
 * @date 2022/2/26 17:39
 * @description
 */
public class FlowReducer extends Reducer<Text, FlowBean, Text, FlowBean> {
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        long upFlow = 0;
        long downFlow = 0;
        long totalFlow = 0;
        for (FlowBean each : values) {
            upFlow += each.getUpFlow();
            downFlow += each.getDownFlow();
            totalFlow += each.getTotalFlow();
        }
        context.write(key, new FlowBean(upFlow, downFlow, totalFlow));
    }
}
