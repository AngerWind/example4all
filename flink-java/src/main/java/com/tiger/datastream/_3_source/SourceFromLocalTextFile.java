package com.tiger.datastream._3_source;

import com.google.common.collect.Lists;
import com.tiger.pojo.Event;
import lombok.SneakyThrows;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.io.PojoCsvInputFormat;
import org.apache.flink.api.java.typeutils.PojoField;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import java.util.List;

public class SourceFromLocalTextFile {

    @SneakyThrows
    @Test
    public void sourceFromLocalTextFile() {

        // 从文件中读取source
        // 参数可以是文件也可以是目录
        // 文件路径可以是相对路径也可以是绝对路径
        // 相对路径是从系统属性 user.dir 获取路径: idea 下是 project 的根目录, standalone 模式下是集群节点根目录
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> textFileSource = env.readTextFile("input/words.txt");

        // 从文件中读取source, 通过FileFormat转换成为pojo
        String file = "path/to/file.txt";
        Path path = new Path(file);
        // 创建PojoCsvInputFormat..... 太复杂了, 还不如按行读取, 自己转换成pojo
        PojoField user = new PojoField(Event.class.getDeclaredField("user"), BasicTypeInfo.STRING_TYPE_INFO);
        PojoField timestamp = new PojoField(Event.class.getDeclaredField("timestamp"), BasicTypeInfo.LONG_TYPE_INFO);
        PojoField url = new PojoField(Event.class.getDeclaredField("url"), BasicTypeInfo.STRING_TYPE_INFO);
        List<PojoField> pojoFields = Lists.newArrayList(user, timestamp, url);
        PojoTypeInfo<Event> eventPojoTypeInfo = new PojoTypeInfo<>(Event.class, pojoFields);
        PojoCsvInputFormat<Event> csvInputFormat = new PojoCsvInputFormat<>(path, eventPojoTypeInfo);

        DataStreamSource<Event> pojoSource = env.readFile(csvInputFormat, file);

    }
}
