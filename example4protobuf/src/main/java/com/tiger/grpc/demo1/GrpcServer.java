package com.tiger;

import com.tiger.grpc.service.NewsService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/16
 * @description
 */
public class GrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        // 启动grpc服务
        Server server = ServerBuilder.forPort(9999).addService(new NewsService())
                .build().start();

        // 等待服务关闭
        server.awaitTermination();
    }
}
