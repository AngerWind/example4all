package com.tiger.mapreduce;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.SequenceFile.Metadata;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;
import org.junit.jupiter.api.*;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/7/16
 * @description
 */
public class SequenceFileTest {

    public static void main(String[] args) throws IOException {

        // 实例化一个Configuration，它会自动去加载本地的core-site.xml配置文件的fs.defaultFS属性。(该文件放在项目的resources目录即可。)
        Configuration conf = new Configuration();
        // 将hdfs写入的路径定义在本地，需要修改默认我文件系统，这样就可以覆盖到之前在core-site.xml配置文件读取到的数据。
        conf.set("fs.defaultFS", "file:///");
        // 代码的入口点，初始化HDFS文件系统，此时我们需要把读取到的fs.defaultFS属性传给fs对象。
        FileSystem fs = FileSystem.get(conf);
        // 这个path是指是需要在文件系统中写入的数据,由于我修改了默认的配置，将数据会写在本地的windows操作系统，因此我这里给出了windows的路径。
        Path path = new Path("/seq/seqfile");
        if (new File(String.valueOf(path)).exists()) {
            boolean delete = new File(String.valueOf(path)).delete();
            System.out.println(delete);
        }
        createSeq(fs, conf, path);
        readSeq(fs, path, conf);

        new SequenceFileTest().test();
    }

    // 写入数据
    public static void createSeq(FileSystem fs, Configuration conf, Path path) throws IOException {
        // 通过SequenceFile的createWriter内部类创建一个序列化容器(写入器)，需要传入上面我们定义的三个参数。并指定序列化的key和value类型。

        // 指定元数据
        Metadata metadata = new Metadata();
        metadata.set(new Text("hello"), new Text("world"));
        metadata.set(new Text("aa"), new Text("bb"));

        SequenceFile.Writer writer = SequenceFile.createWriter(conf,
            Writer.file(path),
            Writer.keyClass(Text.class),
            Writer.valueClass(IntWritable.class),
            Writer.compression(CompressionType.NONE),
            Writer.metadata(metadata));

        for (int i = 0; i < 3; i++) {
            Text key = new Text("yinzhengjie" + i);
            IntWritable value = new IntWritable(i);
            writer.append(key, value);
        }
        // 释放资源
        writer.close();
    }

    // 读取数据
    public static void readSeq(FileSystem fs, Path path, Configuration conf) throws IOException {
        // 实例化一个反序列化容器（读取器），需要传递三个参数，
        SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);
        // 创建两个容器
        Text key = new Text();
        IntWritable value = new IntWritable();
        // 通过reader读取器的next方法，将定义好的两个容器传进去
        while (reader.next(key, value)) {
            System.out.printf("姓名:[%s]\n年龄:[%s]\n", key.toString(), value.get());
        }
    }

    @Test
    public void test(){

        try(
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File("J:\\\\seq\\\\seqfile")));
            ) {
            byte[] bytes = new byte[1];
            while (inputStream.read(bytes) != -1) {
                System.out.printf("bytes: 0x%x, 转换后的10进制为: %d 转换为的字符串: %s %n", bytes[0], (int)bytes[0], (char)bytes[0]);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
