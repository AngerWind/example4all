package com.gitlab.techschool.pcbook.service;

import com.gitlab.techschool.pcbook.pb.Laptop;
import com.gitlab.techschool.pcbook.service.image.DiskImageStore;
import com.gitlab.techschool.pcbook.service.image.ImageStore;
import com.gitlab.techschool.pcbook.service.interceptor.AuthServerInterceptor;
import com.gitlab.techschool.pcbook.service.interceptor.AuthService;
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
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/18
 * @description
 */
public class LaptopServer {
    private static final Logger logger = Logger.getLogger(Laptop.class.getName());

    private final int port;
    private final Server server;

    public LaptopServer(int port, LaptopStore laptopStore, ImageStore imageStore, RatingStore ratingStore,
                        JWTManager jwtManager, UserStore userStore, Map<String, Set<String>> accessibleRoles) {
        this(ServerBuilder.forPort(port), port, laptopStore, imageStore, ratingStore, jwtManager, userStore, accessibleRoles);
    }

    /**
     * 根据不同类型的ServerBuilder来创建不同类型的服务器
     */
    public LaptopServer(ServerBuilder<?> serverBuilder, int port, LaptopStore laptopStore, ImageStore imageStore,
                        RatingStore ratingStore, JWTManager jwtManager, UserStore userStore, Map<String, Set<String>> accessibleRoles) {
        this.port = port;
        this.server = serverBuilder
                .addService(new LaptopService(laptopStore, imageStore, ratingStore))
                .addService(new AuthService(jwtManager, userStore))
                .intercept(new AuthServerInterceptor(jwtManager, userStore, accessibleRoles)) // 拦截所有的请求, 验证是否登录
                .build();
    }


    public void start() throws IOException {
        server.start();
        logger.info("server started on port: " + port);


        // 添加一个shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("shut down gRPC server because JVM shuts down");

            try {
                this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server!= null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        InMemoryLaptopStore laptopStore = new InMemoryLaptopStore();
        ImageStore imageStore = new DiskImageStore("img/receive");
        RatingStore ratingStore = new InMemoryRatingStore();

        JWTManager jwtManager = new JWTManager("secret", Duration.ofMinutes(15));
        UserStore userStore = new InMemoryUserStore();

        // 添加模拟用户
        userStore.save(new User("admin1", "secret", Sets.newHashSet("admin")));
        userStore.save(new User("user1", "secret", Sets.newHashSet("user")));

        Map<String, Set<String>> accessableRoles = Maps.newHashMap();

        accessableRoles.put("/com.gitlab.techschool.pcbook.service.LaptopService/createLaptop", Sets.newHashSet("admin"));
        accessableRoles.put("/com.gitlab.techschool.pcbook.service.LaptopService/uploadImage", Sets.newHashSet("admin"));
        accessableRoles.put("/com.gitlab.techschool.pcbook.service.LaptopService/searchLaptop", Sets.newHashSet("user", "admin"));

        LaptopServer server = new LaptopServer(8000, laptopStore, imageStore, ratingStore, jwtManager, userStore, accessableRoles);

        server.start();

        server.blockUntilShutdown();
    }

}
