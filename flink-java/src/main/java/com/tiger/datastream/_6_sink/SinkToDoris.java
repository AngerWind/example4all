package com.tiger.datastream._6_sink;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.doris.flink.cfg.DorisExecutionOptions;
import org.apache.doris.flink.cfg.DorisOptions;
import org.apache.doris.flink.cfg.DorisReadOptions;
import org.apache.doris.flink.sink.DorisSink;
import org.apache.doris.flink.sink.writer.serializer.RowDataSerializer;
import org.apache.doris.flink.sink.writer.serializer.SimpleStringSerializer;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.StringData;
import org.apache.flink.table.types.DataType;
import org.junit.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/18
 * @description
 */
public class SinkToDoris {

    @Test
    public void json() throws Exception {
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        Properties props = new Properties();
        props.setProperty("format", "json");
        props.setProperty("read_json_by_line", "true"); // 每行一条 json 数据

        DorisReadOptions readOptions = DorisReadOptions.builder()
                .build();

        DorisExecutionOptions executionOptions = DorisExecutionOptions.builder() // 执行参数
                //.setLabelPrefix("doris-label")  // stream-load 导入的时候的 label 前缀
                .disable2PC() // 开启两阶段提交后,labelPrefix 需要全局唯一,为了测试方便禁用两阶段提交
                .setDeletable(false)
                .setBufferCount(3) // 用于缓存stream load数据的缓冲条数: 默认 3
                .setBufferSize(1024 * 1024) //用于缓存stream load数据的缓冲区大小: 默认 1M
                .setMaxRetries(3)
                .setStreamLoadProp(props) // 设置 stream load 的数据格式 默认是 csv,根据需要改成 json
                .build();
        DorisOptions dorisOptions = DorisOptions.builder()
                .setFenodes("FE_IP:8030")
                .setTableIdentifier("db.table")
                .setUsername("root")
                .setPassword("")
                .build();

        DorisSink<String> sink = DorisSink.<String>builder()
                .setDorisReadOptions(readOptions)
                .setDorisOptions(dorisOptions)
                .setDorisExecutionOptions(executionOptions)
                .setSerializer(new SimpleStringSerializer())
                .build();
    }

    @Test
    public void csv() throws Exception {
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        Properties props = new Properties();

        DorisReadOptions readOptions = DorisReadOptions.builder()
                .build();

        DorisExecutionOptions executionOptions = DorisExecutionOptions.builder() // 执行参数
                //.setLabelPrefix("doris-label")  // stream-load 导入的时候的 label 前缀
                .disable2PC() // 开启两阶段提交后,labelPrefix 需要全局唯一,为了测试方便禁用两阶段提交
                .setDeletable(false)
                .setBufferCount(3) // 用于缓存stream load数据的缓冲条数: 默认 3
                .setBufferSize(1024 * 1024) //用于缓存stream load数据的缓冲区大小: 默认 1M
                .setMaxRetries(3)
                .setStreamLoadProp(props) // 设置 stream load 的数据格式 默认是 csv,根据需要改成 json
                .build();
        DorisOptions dorisOptions = DorisOptions.builder()
                .setFenodes("FE_IP:8030")
                .setTableIdentifier("db.table")
                .setUsername("root")
                .setPassword("")
                .build();

        DorisSink<String> sink = DorisSink.<String>builder()
                .setDorisReadOptions(readOptions)
                .setDorisOptions(dorisOptions)
                .setDorisExecutionOptions(executionOptions)
                .setSerializer(new SimpleStringSerializer())
                .build();

        List<Tuple2<String, Integer>> data = new ArrayList<>();
        data.add(new Tuple2<>("doris",1));
        DataStreamSource<Tuple2<String, Integer>> source = env.fromCollection(data);
        source.map((MapFunction<Tuple2<String, Integer>, String>) t -> t.f0 + "\t" + t.f1)
                .sinkTo(sink);
    }

    @Test
    public void rowData() {
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        //doris sink option
        DorisSink.Builder<RowData> builder = DorisSink.builder();
        DorisOptions.Builder dorisBuilder = DorisOptions.builder();
        dorisBuilder.setFenodes("FE_IP:HTTP_PORT")
                .setTableIdentifier("db.table")
                .setUsername("root")
                .setPassword("password");

        // json format to streamload
        Properties properties = new Properties();
        properties.setProperty("format", "json");
        properties.setProperty("read_json_by_line", "true");
        DorisExecutionOptions  executionOperations = DorisExecutionOptions
                .builder()
                .setLabelPrefix("label-doris") //streamload label prefix
                .setDeletable(false)
                .setStreamLoadProp(properties)
                .build();

        //flink rowdata‘s schema
        String[] fields = {"city", "longitude", "latitude", "destroy_date"};
        DataType[] types = {DataTypes.VARCHAR(256), DataTypes.DOUBLE(), DataTypes.DOUBLE(), DataTypes.DATE()};

        builder.setDorisReadOptions(DorisReadOptions.builder().build())
                .setDorisExecutionOptions(executionOperations)
                .setSerializer(RowDataSerializer.builder()    //serialize according to rowdata
                        .setFieldNames(fields)
                        .setType("json")           //json format
                        .setFieldType(types).build())
                .setDorisOptions(dorisBuilder.build());

        //mock rowdata source
        DataStream<RowData> source = env.fromElements("")
                .map(new MapFunction<String, RowData>() {
                    @Override
                    public RowData map(String value) throws Exception {
                        GenericRowData genericRowData = new GenericRowData(4);
                        genericRowData.setField(0, StringData.fromString("beijing"));
                        genericRowData.setField(1, 116.405419);
                        genericRowData.setField(2, 39.916927);
                        genericRowData.setField(3, LocalDate.now().toEpochDay());
                        return genericRowData;
                    }
                });

        source.sinkTo(builder.build());
    }
}
