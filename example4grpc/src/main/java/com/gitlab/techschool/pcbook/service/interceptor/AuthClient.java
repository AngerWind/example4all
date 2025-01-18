package com.gitlab.techschool.pcbook.service.interceptor;

import com.gitlab.techschool.pcbook.pb.AuthServiceGrpc;
import com.gitlab.techschool.pcbook.pb.LoginRequest;
import io.grpc.ManagedChannel;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/20
 * @description
 */
public class AuthClient {

    private AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    private final String username;
    private final String password;

    public AuthClient(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String login() {
        return authServiceBlockingStub.login(
                LoginRequest.newBuilder()
                        .setUsername(username)
                        .setPassword(password)
                        .build())
                .getToken();
    }

    public void init(ManagedChannel managedChannel) {
        this.authServiceBlockingStub = AuthServiceGrpc.newBlockingStub(managedChannel);
    }
}
