package com.gitlab.techschool.pcbook.service;


import static org.junit.jupiter.api.Assertions.*;

import com.gitlab.techschool.pcbook.pb.*;
import com.gitlab.techschool.pcbook.pb.LaptopServiceGrpc.LaptopServiceBlockingStub;
import com.gitlab.techschool.pcbook.sample.Generator;
import com.gitlab.techschool.pcbook.service.image.DiskImageStore;
import com.gitlab.techschool.pcbook.service.image.ImageStore;
import com.gitlab.techschool.pcbook.service.jwt.JWTManager;
import com.gitlab.techschool.pcbook.service.laptop.InMemoryLaptopStore;
import com.gitlab.techschool.pcbook.service.laptop.LaptopStore;
import com.gitlab.techschool.pcbook.service.rate.InMemoryRatingStore;
import com.gitlab.techschool.pcbook.service.rate.RatingStore;
import com.gitlab.techschool.pcbook.service.user.InMemoryUserStore;
import com.gitlab.techschool.pcbook.service.user.User;
import com.gitlab.techschool.pcbook.service.user.UserStore;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.grpc.*;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/18
 * @description
 */
class LaptopClientTest {

    // automatic graceful shutdown channel at the end of the test
    @Rule
    public final GrpcCleanupRule grpcCleanupRule = new GrpcCleanupRule();

    private LaptopStore laptopStore;
    private ImageStore imageStore;
    private RatingStore ratingStore;

    private LaptopServer laptopServer;
    private ManagedChannel managedChannel;
    private LaptopServiceBlockingStub blockingStub;


    private final Generator generator = new Generator();

    @BeforeEach
    public void init() throws IOException {

        // 创建一个内存中的, 用于测试的服务器
        String serverName = InProcessServerBuilder.generateName();

        // 这样创建的服务器, 会执行在当前这个测试线程中
        // InProcessServerBuilder serverBuilder = InProcessServerBuilder.forName(serverName).directExecutor();

        // 这样创建的服务器,
        InProcessServerBuilder serverBuilder = InProcessServerBuilder.forName(serverName).executor(Executors.newFixedThreadPool(3));

        imageStore = new DiskImageStore("img/receive");
        laptopStore = new InMemoryLaptopStore();
        ratingStore = new InMemoryRatingStore();

        JWTManager jwtManager = new JWTManager("secret", Duration.ofMinutes(15));
        UserStore userStore = new InMemoryUserStore();
        // 添加模拟用户
        userStore.save(new User("admin1", "secret", Sets.newHashSet("admin")));
        userStore.save(new User("user1", "secret", Sets.newHashSet("user")));


        Map<String, Set<String>> accessableRoles = Maps.newHashMap();

        accessableRoles.put("/com.gitlab.techschool.pcbook.service.LaptopService/createLaptop", Sets.newHashSet("admin"));
        accessableRoles.put("/com.gitlab.techschool.pcbook.service.LaptopService/uploadImage", Sets.newHashSet("admin"));
        accessableRoles.put("/com.gitlab.techschool.pcbook.service.LaptopService/searchLaptop", Sets.newHashSet("user", "admin"));

        laptopServer = new LaptopServer(serverBuilder, 0, laptopStore, imageStore, ratingStore, jwtManager, userStore, accessableRoles);

        // 启动服务器
        laptopServer.start();

        // 创建一个内存中的channel, 并连接上指定的服务器
        // managedChannel = InProcessChannelBuilder.forName(serverName).directExecutor().build();
        managedChannel = InProcessChannelBuilder.forName(serverName).executor(Executors.newFixedThreadPool(3)).build();

        // 通过grpcCleanupRule来自动关闭channel
        grpcCleanupRule.register(managedChannel);

        // 创建一个阻塞式的stub, 用于发送请求
        blockingStub = LaptopServiceGrpc.newBlockingStub(managedChannel);
    }

    @AfterEach
    public void shutdown() throws InterruptedException {
        laptopServer.stop();
        // 这里无需关闭managedChannel, 因为grpcCleanupRule会自动关闭managedChannel
    }


    @Test
    void test1() {
        // Laptop如果没有id, 那么会自动填充uuid
        Assertions.assertDoesNotThrow(() -> {
            CreateLaptopRequest request = CreateLaptopRequest.newBuilder()
                    .setLaptop(generator.NewLaptopWithoutId())
                    .build();
            CreateLaptopResponse response = blockingStub.createLaptop(request);
            // id不为null, 并且id为uuid
            assertNotNull(response.getId());
            UUID.fromString(response.getId());

            Laptop laptop = laptopStore.findById(response.getId());
            assertNotNull(laptop);

            // 除了id, 其他的都应该相同
            Laptop.getDescriptor().getFields().forEach(filedDescriptor -> {
                if (!filedDescriptor.getName().equals("id")) {
                    assertEquals(request.getLaptop().getField(filedDescriptor), laptop.getField(filedDescriptor));
                }
            });
        });

    }

    @Test
    public void test2() {
        // 如果有id, 那么不会自动填充id
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder()
                .setLaptop(generator.NewLaptopWithId(UUID.randomUUID().toString()))
                .build();
        CreateLaptopResponse response = blockingStub.createLaptop(request);
        assertEquals(request.getLaptop().getId(), response.getId());

        Laptop laptop = laptopStore.findById(request.getLaptop().getId());
        assertNotNull(laptop);
        assertEquals(request.getLaptop(), laptop);
    }

    @Test
    public void test3() {
        // 如果有id, 但是id不是uuid, 那么会抛出异常, 并且状态码为INVALID_ARGUMENT
        CreateLaptopRequest request1 = CreateLaptopRequest.newBuilder()
                .setLaptop(generator.NewLaptopWithId("invalid-uuid"))
                .build();
        try {
            blockingStub.createLaptop(request1);
            fail();
        } catch (StatusRuntimeException e) {
            assertEquals(Status.INVALID_ARGUMENT.getCode(), e.getStatus().getCode());
        }
    }

    @Test
    public void test4() {
        // 如果有两个id一样的laptop, 那么会抛出异常, 并且状态码为ALREADY_EXISTS
        CreateLaptopRequest request1 = CreateLaptopRequest.newBuilder()
                .setLaptop(generator.NewLaptopWithRandomUUID())
                .build();
        blockingStub.createLaptop(request1);
        try {
            blockingStub.createLaptop(request1); // 这里应该会报错
            fail(); // 如果没有保存, 那么fail
        } catch (StatusRuntimeException e) {
            assertEquals(Status.ALREADY_EXISTS.getCode(), e.getStatus().getCode());
        }
    }

    @Test
    public void test5() {
        // 测试服务器处理请求超时
        CreateLaptopRequest request1 = CreateLaptopRequest.newBuilder()
                .setLaptop(generator.NewLaptopWithRandomUUID())
                .build();

        try {
            // 5秒后, 向服务器发送请求超时
            // 如果超时, 那么createLaptop方法会抛出一个StatusRuntimeException, 并且code为DEADLINE_EXCEEDED
            blockingStub.withDeadlineAfter(3, TimeUnit.SECONDS).createLaptop(request1);
            fail();
        } catch (StatusRuntimeException e) {
            assertEquals(Status.DEADLINE_EXCEEDED.getCode(), e.getStatus().getCode());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void rateLaptop() throws Exception {
        Generator generator = new Generator();
        Laptop laptop = generator.NewLaptopWithRandomUUID();
        laptopStore.save(laptop);

        LaptopServiceGrpc.LaptopServiceStub stub = LaptopServiceGrpc.newStub(managedChannel);
        RateLaptopResponseStreamObserver responseObserver = new RateLaptopResponseStreamObserver();
        StreamObserver<RateLaptopRequest> requestObserver = stub.rateLaptop(responseObserver);

        double[] scores = {8, 7.5, 10};
        double[] averages = {8, 7.75, 8.5};
        int n = scores.length;

        for (int i = 0; i < n; i++) {
            RateLaptopRequest request = RateLaptopRequest.newBuilder()
                    .setLaptopId(laptop.getId())
                    .setScore(scores[i])
                    .build();
            requestObserver.onNext(request);
        }

        requestObserver.onCompleted();
        assertNull(responseObserver.err);
        assertTrue(responseObserver.completed);
        assertEquals(n, responseObserver.responses.size());

        int idx = 0;
        for (RateLaptopResponse response : responseObserver.responses) {
            assertEquals(laptop.getId(), response.getLaptopId());
            assertEquals(idx + 1, response.getRatedCount());
            assertEquals(averages[idx], response.getAverageScore(), 1e-9);
            idx++;
        }
    }

    private class RateLaptopResponseStreamObserver implements StreamObserver<RateLaptopResponse> {
        public List<RateLaptopResponse> responses;
        public Throwable err;
        public boolean completed;

        public RateLaptopResponseStreamObserver() {
            responses = new LinkedList<>();
        }

        @Override
        public void onNext(RateLaptopResponse response) {
            responses.add(response);
        }

        @Override
        public void onError(Throwable t) {
            err = t;
        }

        @Override
        public void onCompleted() {
            completed = true;
        }
    }
}