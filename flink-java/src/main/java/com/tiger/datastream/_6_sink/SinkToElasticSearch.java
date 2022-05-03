package com.tiger.datastream._6_sink;

import com.tiger.pojo.Event;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class SinkToElasticSearch {

    @Test
    public void sinkToElasticSearch() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Event> stream = env.fromElements(new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L),
                new Event("Alice", "./prod?id=100", 3000L),
                new Event("Alice", "./prod?id=200", 3500L),
                new Event("Bob", "./prod?id=2", 2500L),
                new Event("Alice", "./prod?id=300", 3600L),
                new Event("Bob", "./home", 3000L),
                new Event("Bob", "./prod?id=1", 2300L),
                new Event("Bob", "./prod?id=3", 3300L));

        // 1. 指定ElasticSearch的地址
        ArrayList<HttpHost> httpHosts = new ArrayList<>();
        httpHosts.add(new HttpHost("hadoop102", 9200, "http"));


        // 2. 创建一个 ElasticsearchSinkFunction
        ElasticsearchSinkFunction<Event> elasticsearchSinkFunction = new ElasticsearchSinkFunction<Event>() {
            @Override
            public void process(Event element, RuntimeContext ctx, RequestIndexer indexer) {
                // 通过Event构建写入到es的数据
                HashMap<String, String> data = new HashMap<>();
                data.put(element.getUser(), element.getUrl());

                // 创建一个IndexRequest
                IndexRequest request = Requests.indexRequest()
                        .index("clicks")
                        .type("type") // Es 6 必须定义 type
                        .source(data);

                // 将数据写入到es
                indexer.add(request);
            }
        };

        // 3. 添加sink
        stream.addSink(new ElasticsearchSink.Builder<Event>(httpHosts, elasticsearchSinkFunction).build());
        env.execute();

    }
}
