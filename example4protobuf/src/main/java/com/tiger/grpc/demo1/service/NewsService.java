package com.tiger.grpc.service;

import com.tiger.grpc.proto.NewsProto;
import com.tiger.grpc.proto.NewsProto.News;
import com.tiger.grpc.proto.NewsProto.NewsRequest;
import com.tiger.grpc.proto.NewsProto.NewsResponse;
import com.tiger.grpc.proto.NewsProto.NewsResponse.Builder;
import com.tiger.grpc.proto.NewsServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.Date;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/16
 * @description
 */

// 要实现grpc接口, 必须继承该类
public class NewsService extends NewsServiceGrpc.NewsServiceImplBase{


    @Override
    public void getNews(NewsRequest request, StreamObserver<NewsResponse> responseObserver) {
        String date = request.getDate();
        NewsResponse response = null;

        Builder builder = null;
        try {
            builder = NewsResponse.newBuilder();
            for (int i = 0; i < 100; i++) {
                News news = News.newBuilder().setId(i)
                        .setContent("content: " + i)
                        .setTitle("title: " + i)
                        .setCreateTime(new Date().getTime())
                        .build();

                builder.addNews(news);
            }
            response = builder.build();
        } catch (Exception e) {
            // 处理异常
            responseObserver.onError(e);
        } finally {
            // 返回结果
            responseObserver.onNext(response);
        }
        // 本次处理完成
        responseObserver.onCompleted();
    }
}
