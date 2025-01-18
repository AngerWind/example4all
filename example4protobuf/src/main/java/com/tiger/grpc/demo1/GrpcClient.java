package com.tiger;

import com.tiger.grpc.proto.NewsProto;
import com.tiger.grpc.proto.NewsProto.NewsRequest;
import com.tiger.grpc.proto.NewsProto.NewsResponse;
import com.tiger.grpc.proto.NewsServiceGrpc;
import com.tiger.grpc.proto.NewsServiceGrpc.NewsServiceBlockingStub;
import com.tiger.grpc.proto.NewsServiceGrpc.NewsServiceFutureStub;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/16
 * @description
 */
public class GrpcClient {

    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9999)
                .usePlaintext() // 无需加密或者认证
                .build();

        // 对NewsService异步通讯的客户端
        NewsServiceFutureStub futureStub = NewsServiceGrpc.newFutureStub(managedChannel);

        // 对NewsService同步通讯的客户端
        NewsServiceBlockingStub blockingStub = NewsServiceGrpc.newBlockingStub(managedChannel);

        NewsRequest request = NewsRequest.newBuilder()
                .setDate("1001")
                .build();

        NewsResponse response = blockingStub.getNews(request);

        for (NewsProto.News news : response.getNewsList()) {
            System.out.printf("%s, %s, %s\n", news.getContent(), news.getTitle(), news.getCreateTime());
        }

        // 关闭连接
        managedChannel.shutdown();


    }
}
