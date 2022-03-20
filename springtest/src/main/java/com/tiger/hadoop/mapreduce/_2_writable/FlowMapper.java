package com.tiger.hadoop.mapreduce._2_writable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 求用户总上行流量, 下行流量, 总流量
 *
 * 输入数据格式:
 * 手机号码		 网络ip			 上行流量    下行流量     网络状态码
 * 13560436666	 120.196.100.99  1116       954         200
 * 13560436666	 8.8.8.8         8452       456         200
 *
 * 输出数据格式:
 * 手机号码		    上行流量        下行流量		总流量
 * 13560436666      1116           954          2070
 */
public class FlowMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] words = value.toString().split("\\s+");
        long up = Long.parseLong(words[2]);
        long down = Long.parseLong(words[3]);
        context.write(new Text(words[0]), new FlowBean(up, down, down + up));
    }
}
