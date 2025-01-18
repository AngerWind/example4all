package com.gitlab.techschool.pcbook.service.interceptor;

import com.gitlab.techschool.pcbook.pb.AuthServiceGrpc.AuthServiceImplBase;
import com.gitlab.techschool.pcbook.pb.LoginRequest;
import com.gitlab.techschool.pcbook.pb.LoginResponse;
import com.gitlab.techschool.pcbook.service.jwt.JWTManager;
import com.gitlab.techschool.pcbook.service.user.User;
import com.gitlab.techschool.pcbook.service.user.UserStore;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/20
 * @description
 */
public class AuthService extends AuthServiceImplBase {

    private final JWTManager jwtManager;
    private final UserStore userStore;

    public AuthService(JWTManager jwtManager, UserStore userStore) {
        this.jwtManager = jwtManager;
        this.userStore = userStore;
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        // 查询用户是否存在
        User user = userStore.find(request.getUsername());
        if (user == null) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("user not found")
                            .asRuntimeException());
            return;
        }

        // 校验密码
        if (!user.isCorrectPassword(request.getPassword())) {
            responseObserver.onError(
                    Status.PERMISSION_DENIED
                         .withDescription("invalid password")
                         .asRuntimeException());
            return;
        }

        // 返回token
        try {
            String token = jwtManager.signs(user);
            responseObserver.onNext(LoginResponse.newBuilder()
                   .setToken(token)
                   .build());
            responseObserver.onCompleted();

            return;
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                         .withDescription(e.getMessage())
                         .asRuntimeException());
        }
    }
}
