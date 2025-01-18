package com.gitlab.techschool.pcbook.service.interceptor;

import io.grpc.*;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;
import io.grpc.Metadata.Key;

import java.time.Duration;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/20
 * @description
 */
public class AuthClientInterceptor implements ClientInterceptor {

    private static final Logger logger = Logger.getLogger(AuthClientInterceptor.class.getName());

    private static final Metadata.Key<String> AUTHORIZATION_HEADER_KEY = Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    private final AuthClient authClient;
    // 需要校验权限的方法
    private final Map<String, Boolean> authMethods;

    private String accessToken;

    private final Duration duration;

    public AuthClientInterceptor(AuthClient authClient, Map<String, Boolean> authMethods, Duration refreshDuration) {
        this.authClient = authClient;
        this.authMethods = authMethods;
        this.duration = refreshDuration;
        refreshToken(); // 首次登录
        scheduleRefreshToken(duration.toMillis());
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel next) {
        if (authMethods.getOrDefault(method.getFullMethodName(), false)) {
            return new SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

                @Override
                public void start(Listener<RespT> responseListener, Metadata headers) {
                    /* put custom header */
                    headers.put(AUTHORIZATION_HEADER_KEY, accessToken);
                    super.start(new SimpleForwardingClientCallListener<RespT>(responseListener) {
                        @Override
                        public void onHeaders(Metadata headers) {
                            /**
                             * if you don't need receive header from server,
                             * you can use {@link io.grpc.stub.MetadataUtils#attachHeaders}
                             * directly to send header
                             */
                            logger.info("header received from server:" + headers);
                            super.onHeaders(headers);
                        }
                    }, headers);
                }
            };
        }
        return next.newCall(method, callOptions);
    }


    // Refreshes the token on a scheduled basis
    private void scheduleRefreshToken(long refreshMillis) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable refreshTask = () -> {
            try {
                refreshToken();
            } catch (Exception e) {
                logger.severe("Token refresh failed: " + e.getMessage());
            }
        };
        scheduler.scheduleAtFixedRate(refreshTask, 0, refreshMillis, TimeUnit.MILLISECONDS);
    }

    // Retrieves a new token from the AuthClient
    private void refreshToken() {
        try {
            this.accessToken = authClient.login();
            logger.info("Token refreshed: " + accessToken);
        } catch (Exception e) {
            logger.severe("Failed to refresh token: " + e.getMessage());
        }
    }
}
