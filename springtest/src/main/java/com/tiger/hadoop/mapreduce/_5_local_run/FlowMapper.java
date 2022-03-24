package com.tiger.hadoop.mapreduce._5_local_run;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 求用户总上行流量, 下行流量, 总流量
 *
 * 输入数据格式:
 * id	手机号码		 网络ip			 上行流量    下行流量     网络状态码
 * 7    13560436666	 120.196.100.99  1116       954         200
 * 7    13560436666	 8.8.8.8         8452       456         200
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

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {

    }
}
