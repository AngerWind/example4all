package com.tiger.hadoop.mapreduce._3_writable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 实现Writable接口, 使得类可以序列化, write方法和readFields方法对于字段的序列化顺序需要一致
 * 实现Comparable接口, 类作为Reduce的输入key时需要排序
 * 实现空参构造方法, 用于反序列化时通过反射生成对象
 * 重写toString方法, 用于写入到文件
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlowBean implements Writable, Comparable<FlowBean> {
    private Long upFlow;
    private Long downFlow;
    private Long totalFlow;

    @Override
    public int compareTo(FlowBean o) {
        return 0;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(upFlow);
        out.writeLong(downFlow);
        out.writeLong(totalFlow);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        // 顺序与write方法一致
        this.upFlow = in.readLong();
        this.downFlow = in.readLong();
        this.totalFlow = in.readLong();
    }

    @Override
    public String toString() {
        return upFlow + "\t" + downFlow + "\t" + totalFlow;
    }
}
