package com.gitlab.techschool.pcbook.service;

import com.gitlab.techschool.pcbook.pb.*;
import com.gitlab.techschool.pcbook.pb.LaptopServiceGrpc.LaptopServiceBlockingStub;
import com.gitlab.techschool.pcbook.pb.LaptopServiceGrpc.LaptopServiceFutureStub;
import com.gitlab.techschool.pcbook.sample.Generator;
import com.gitlab.techschool.pcbook.service.interceptor.AuthClient;
import com.gitlab.techschool.pcbook.service.interceptor.AuthClientInterceptor;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import io.grpc.*;
import io.grpc.Status.Code;
import io.grpc.stub.StreamObserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/18
 * @description
 */
public class LaptopClient {

    private static final Logger logger = Logger.getLogger(LaptopClient.class.getName());

    // 支持一元通讯, 服务器推流
    private final LaptopServiceBlockingStub blockingStub;
    // 支持一元通讯,
    private final  LaptopServiceFutureStub futureStub;
    // 支持一元通讯, 服务器推流, 客户端推流, 双向流
    private final LaptopServiceGrpc.LaptopServiceStub stub;
    private final ManagedChannel managedChannel;

    public LaptopClient(String host, int port) {

        Map<String, Boolean> accessableRoles = Maps.newHashMap();

        accessableRoles.put("/com.gitlab.techschool.pcbook.service.LaptopService/createLaptop", true);
        accessableRoles.put("/com.gitlab.techschool.pcbook.service.LaptopService/uploadImage", true);
        accessableRoles.put("/com.gitlab.techschool.pcbook.service.LaptopService/searchLaptop", true);


        AuthClient authClient = new AuthClient("user1", "secret");
        AuthClientInterceptor clientInterceptor = new AuthClientInterceptor(authClient, accessableRoles, Duration.ofSeconds(10));

        // todo 没有启动server, 整个main方法都不会报错, 而是打印error日志
        managedChannel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // 不使用安全链接, 而是纯文本
                .intercept(clientInterceptor)
                .build();

        authClient.init(managedChannel);


        blockingStub = LaptopServiceGrpc.newBlockingStub(managedChannel);
        stub = LaptopServiceGrpc.newStub(managedChannel);
        futureStub = LaptopServiceGrpc.newFutureStub(managedChannel);
    }

    public void shutdown() throws InterruptedException {
        managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void createLaptop(Laptop laptop) {
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder()
                .setLaptop(laptop)
                .build();

        try {
            // 该方法会堵塞, 直到服务端调用了onCompleted()方法, 才会返回
            // 如果服务端调用了onError(), 那么这个方法会抛出StatusRuntimeException
            // CreateLaptopResponse response = blockingStub.createLaptop(request);

            // 这个调用和上面的调用一样, 区别在于30秒之后会自动抛出一个StatusRuntimeException, 并且Status为DEADLINE_EXCEEDED
            // 表示服务器处理请求超时
            CreateLaptopResponse response = blockingStub.withDeadlineAfter(30, TimeUnit.SECONDS).createLaptop(request);
            logger.info("laptop created with id: " + response.getId());
        } catch (StatusRuntimeException e) {

            // 不管在服务器的responseObserver.onError()中设置什么异常
            // 客户端抛出的始终是StatusRuntimeException
            // 如果onError()中的异常是StatusRuntimeException或者StatusException, 那么客户端接收到的StatusRuntimeException的code与抛出的相同
            // 如果是其他异常, 那么客户端接收到的StatusRuntimeException的code为UNKNOWN

            if (e.getStatus().getCode() == Code.ALREADY_EXISTS) {
                logger.info("laptop already exists");
            } else if (e.getStatus().getCode() == Code.DEADLINE_EXCEEDED) {
                logger.info("服务器处理请求超时");
            } else if (e.getStatus().getCode() == Code.UNKNOWN) {
                logger.log(Level.SEVERE, String.format("request failed, status: %s, message: %s ", e.getStatus().getCode(), e.getMessage()));
            }
            else {
                logger.log(Level.SEVERE, String.format("request failed, status: %s, message: %s ", e.getStatus().getCode(), e.getMessage()));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("request failed, message: %s ", e.getMessage()));
        }

    }

    public void searchLaptop(Filter filter) {
        logger.info("search started");

        SearchLaptopRequest request = SearchLaptopRequest.newBuilder().setFilter(filter).build();

        try {
            // 该方法会立即返回
            Iterator<SearchLaptopResponse> iterator = blockingStub.searchLaptop(request);

            // 设置等待服务器处理的超时时间
            // Iterator<SearchLaptopResponse> iterator = blockingStub
            //         .withDeadlineAfter(5, TimeUnit.SECONDS)
            //         .searchLaptop(request);

            // hasNext()会堵塞, 直到服务器
            //      1. 超过了服务器处理请求的超时时间, 抛出StatusRuntimeException并且code为DEADLINE_EXCEEDED
            //      2. 调用responseObserver.onNext()发送响应, 并返回true
            //      3. 调用responseObserver.onComplete(), 并返回false
            //      4. 调用responseObserver.onError(), 抛出StatusRuntimeException
            while (iterator.hasNext()) {
                SearchLaptopResponse response = iterator.next();
                Laptop laptop = response.getLaptop();
                logger.info("- found: " + laptop.getId());
            }
        } catch (StatusRuntimeException e) {

            // 不管在服务器的responseObserver.onError()中设置什么异常
            // 客户端抛出的始终是StatusRuntimeException
            // 如果onError()中的异常是StatusRuntimeException或者StatusException, 那么客户端接收到的StatusRuntimeException的code与抛出的相同
            // 如果是其他异常, 那么客户端接收到的StatusRuntimeException的code为UNKNOWN

            if (e.getStatus().getCode() == Code.DEADLINE_EXCEEDED) {
                logger.log(Level.SEVERE, "服务器处理请求超时");
            } else {
                logger.log(Level.SEVERE, String.format("request failed, status: %s, message: %s ", e.getStatus().getCode(), e.getMessage()));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "request failed: " + e.getMessage());
            return;
        }

        logger.info("search completed");
    }

    /**
     * 使用客户端推流来上传文件
     */
    public void uploadImage(String laptopID, String imagePath) throws InterruptedException {
        // 因为客户端推流和服务器响应是异步的
        // 为了在客户端中等待服务器的响应, 需要使用一个CountDownLatch
        final CountDownLatch finishLatch = new CountDownLatch(1);

        // 返回一个StreamObserver, 可以使用这个StreamObserver来源源不断的发送request到服务器
        StreamObserver<UploadImageRequest> requestObserver = stub.withDeadlineAfter(10000, TimeUnit.SECONDS)
                // 这里创建一个StreamObserver, 来处理服务器的响应
                .uploadImage(new StreamObserver<UploadImageResponse>() {

                    /**
                     * 一旦服务器调用responseObserver.onNext()来发送响应, 就会调用这个方法
                     */
                    @Override
                    public void onNext(UploadImageResponse response) {
                        logger.info("server got a image with size: " + response.getSize());
                    }

                    /**
                     * 一旦服务器调用responseObserver.onError(), 那么就会调用这个方法, 表示服务器在处理推流的时候, 出现了异常
                     * 或者当客户端调用了requestObserver.onError(), 那么也会调用这个方法, 并接受到一个StatusRuntimeException, code为CANCELLED
                     */
                    @Override
                    public void onError(Throwable t) {
                        logger.log(Level.SEVERE, "upload failed: " + t);
                        finishLatch.countDown();
                    }

                    /**
                     * 一旦服务器调用responseObserver.onCompleted(), 那么就会调用这个方法
                     */
                    @Override
                    public void onCompleted() {
                        logger.info("image uploaded");

                        // 服务器响应完毕
                        finishLatch.countDown();
                    }
                });

        // 读取文件
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(imagePath);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "cannot read image file: " + e.getMessage());
            return;
        }

        // 获取图片类型
        String imageType = imagePath.substring(imagePath.lastIndexOf("."));

        try {

            ImageInfo info = ImageInfo.newBuilder().setLaptopId(laptopID).setImageType(imageType).build();
            UploadImageRequest request = UploadImageRequest.newBuilder().setInfo(info).build();
            // 发送图片的元数据信息
            requestObserver.onNext(request);
            logger.info("sent image info:\n" + info);

            byte[] buffer = new byte[1024];
            while (true) {
                int n = fileInputStream.read(buffer);
                if (n <= 0) {
                    break;
                }

                // 如果服务端调用了onComplete或者onError, 那么说明服务端结束请求了, 应该停止发送消息
                if (finishLatch.getCount() == 0) {
                    return;
                }

                // 发送数据块
                request = UploadImageRequest.newBuilder()
                        .setChunkData(ByteString.copyFrom(buffer, 0, n))
                        .build();

                // 即使服务端调用了onComplete或者onError, 这里的onNext也不会报错, 而是继续执行
                // 所以我们要通过一个标志位来判断是否应该继续发送消息
                requestObserver.onNext(request);

                logger.info("sent image chunk with size: " + n);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "unexpected error: " + e.getMessage());
            requestObserver.onError(e);
            return;
        }

        // 发送图片完成
        requestObserver.onCompleted();

        // 一旦在客户端调用了onError, 那么服务端和客户端的onError()方法都会接受到一个StatusRuntimeException,code为CANCELLED的异常
        // requestObserver.onError(Status.UNAUTHENTICATED.asRuntimeException());

        // 等待服务器将图片写入磁盘, 并响应
        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            logger.warning("request cannot finish within 1 minute");
        }
    }

    public void rateLaptop(String[] laptopIDs, double[] scores) throws InterruptedException {
        CountDownLatch finishLatch = new CountDownLatch(1);
        // 返回一个StreamObserver, 用于向服务器发送流式请求
        StreamObserver<RateLaptopRequest> requestObserver = stub.withDeadlineAfter(5000000, TimeUnit.SECONDS)
                // 需要传入一个StreamObserver, 用于处理服务器的流式响应
                .rateLaptop(new StreamObserver<RateLaptopResponse>() {

                    /**
                     * 一旦服务器调用responseObserver.onNext()来发送响应, 就会调用这个方法来处理服务器的响应
                     */
                    @Override
                    public void onNext(RateLaptopResponse response) {
                        logger.info("laptop rated: id = " + response.getLaptopId() +
                                ", count = " + response.getRatedCount() +
                                ", average = " + response.getAverageScore());
                    }

                    /**
                     * 服务器调用responseObserver.onError(), 那么就会调用这个方法, 表示服务器在处理推流的时候, 出现了异常
                     * 客户端调用requestObserver.onError(), 也会调用这个方法, 并接受到一个StatusRuntimeException,code为CANCELLED
                     */
                    @Override
                    public void onError(Throwable t) {
                        logger.log(Level.SEVERE, "rate laptop failed: " + t.getMessage());
                        finishLatch.countDown();
                    }

                    /**
                     * 一旦服务器调用responseObserver.onCompleted(), 那么就会调用这个方法
                     */
                    @Override
                    public void onCompleted() {
                        logger.info("rate laptop completed");
                        finishLatch.countDown();
                    }
                });

        int n = laptopIDs.length;
        try {
            for (int i = 0; i < n; i++) {
                RateLaptopRequest request = RateLaptopRequest.newBuilder()
                        .setLaptopId(laptopIDs[i])
                        .setScore(scores[i])
                        .build();
                // 通过onNext()来不停的给服务器推流
                requestObserver.onNext(request);
                logger.info("sent rate-laptop request: id = " + request.getLaptopId() + ", score = " + request.getScore());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "unexpected error: " + e.getMessage());
            // 客户端推流失败
            requestObserver.onError(e);
            return;
        }

        // 客户端推流完成, 调用该方法会使服务端的responseObserver.onCompleted()方法被调用
        requestObserver.onCompleted();

        // 客户端推流失败, 一旦调用这个方法, 那么会立即调用服务端的responseObserver.onError()方法, 并接受到一个StatusRuntimeException,code为CANCELLED
        // requestObserver.onError(Status.INVALID_ARGUMENT.withDescription("invalid argument").asRuntimeException());

        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            logger.warning("request cannot finish within 1 minute");
        }
    }


    public static void main(String[] args) throws InterruptedException {



        LaptopClient client = new LaptopClient("localhost", 8000);
        Generator generator = new Generator();

        // testCreateLaptop();

        // testSearchLaptop();

        testUploadImage(client, generator);

        // testRateLaptop();
    }

    public static void testUploadImage(LaptopClient client, Generator generator) throws InterruptedException {

        try {
            Laptop laptop = generator.NewLaptopWithRandomUUID();
            client.createLaptop(laptop);

            // 发送图片
            client.uploadImage(laptop.getId(), "img/send/laptop.png");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "unexpected exception: " + e.getMessage());
        } finally {
            client.shutdown();
        }
    }



    public static void testSearchLaptop() throws InterruptedException {
        LaptopClient client = new LaptopClient("localhost", 8000);
        Generator generator = new Generator();

        try {
            for (int i = 0; i < 10; i++) {
                Laptop laptop = generator.NewLaptopWithRandomUUID();
                client.createLaptop(laptop);
            }

            Filter filter = Filter.newBuilder()
                 .setMaxPriceUsd(3000)
                 .setMinCpuCores(4)
                 .setMinCpuGhz(2.5)
                 .build();

            client.searchLaptop(filter);
        } finally {
            client.shutdown();
        }
    }

    public static void testCreateLaptop() throws InterruptedException {
        LaptopClient client = new LaptopClient("localhost", 8000);
        Generator generator = new Generator();

        try {
            // client.createLaptop(generator.NewLaptopWithRandomUUID());
            client.createLaptop(generator.NewLaptopWithId("invalid id"));
        } finally {
            client.shutdown();
        }
        /**
         * 具体的代码查看对应的测试类
         * @see LaptopClientTest
         */
    }

    public static void testRateLaptop() throws InterruptedException {
        LaptopClient client = new LaptopClient("0.0.0.0", 8000);
        Generator generator = new Generator();

        try {
            int n = 3;
            String[] laptopIDs = new String[n];

            for (int i = 0; i < n; i++) {
                Laptop laptop = generator.NewLaptopWithRandomUUID();
                laptopIDs[i] = laptop.getId();
                client.createLaptop(laptop);
            }

            Scanner scanner = new Scanner(System.in);
            while (true) {
                logger.info("rate laptop (y/n)? ");
                String answer = scanner.nextLine();
                if (answer.toLowerCase().trim().equals("n")) {
                    break;
                }

                double[] scores = new double[n];
                for (int i = 0; i < n; i++) {
                    scores[i] = generator.NewLaptopScore();
                }

                client.rateLaptop(laptopIDs, scores);
            }
        } finally {
            client.shutdown();
        }

    }
}
