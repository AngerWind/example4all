package com.gitlab.techschool.pcbook.service.interceptor;

import com.gitlab.techschool.pcbook.service.jwt.JWTManager;
import com.gitlab.techschool.pcbook.service.jwt.JWTManager.UserClaims;
import com.gitlab.techschool.pcbook.service.user.User;
import com.gitlab.techschool.pcbook.service.user.UserStore;
import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/19
 * @description
 */
public class AuthServerInterceptor implements io.grpc.ServerInterceptor {

    private static final Logger logger = Logger.getLogger(AuthServerInterceptor.class.getName());

    private final JWTManager jwtManager;
    private final UserStore userStore;

    // key是方法的全类名, value是能够调用这个方法的role
    // 如果方法名不在这里面, 那么谁都可以调用
    private final Map<String, Set<String>> accessibleRoles;

    private static final Metadata.Key<String> authorizationKey =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    public AuthServerInterceptor(JWTManager jwtManager, UserStore userStore, Map<String, Set<String>> accessibleRoles) {
        this.jwtManager = jwtManager;
        this.userStore = userStore;
        this.accessibleRoles = accessibleRoles;
    }

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        if (authorize(Context.current(), call.getMethodDescriptor().getFullMethodName(), headers)) {
            return next.startCall(call, headers);
        } else {
            // todo 被拒绝之后如何返回具体的信息, 说明权限拒绝
            return null;
        }

    }

    public boolean authorize(Context ctx, String method, Metadata headers){
        boolean containMethod = accessibleRoles.containsKey(method);
        if (!containMethod) {
            // everyone can call
            return true;
        }

        // 获取header中的authorization字段, 他是jwt
        String token = headers.get(authorizationKey);
        if (token == null || token.isEmpty()) {
            // header中没有authorization
            return false;
        }

        UserClaims userClaims = jwtManager.verify(token);
        if (userClaims == null) {
            // token错误, 或者过期
            return false;
        }

        Set<String> ownedRoles = userStore.find(userClaims.name).getRole();
        Set<String> requiredRoles = accessibleRoles.get(method);
        for (String ownedRole : ownedRoles) {
            if (requiredRoles.contains(ownedRole)) {
                return true;
            }
        }
        return false;
    }
}
