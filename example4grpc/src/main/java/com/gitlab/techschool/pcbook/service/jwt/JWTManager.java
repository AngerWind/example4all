package com.gitlab.techschool.pcbook.service.jwt;

import com.gitlab.techschool.pcbook.service.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/19
 * @description
 */
public class JWTManager {

    // 用于签署和验证token的秘钥
    private String secretKey;

    // token的有效时间
    private Duration duration;

    public JWTManager(String secretKey, Duration duration) {
        this.secretKey = secretKey;
        this.duration = duration;
    }

    public String signs(User user) {
        // todo 使用jjwt
        return String.format("%s.%s.%s", user.getName(), new Date(Instant.now().toEpochMilli() + duration.toMillis()), secretKey);
    }

    public UserClaims verify(String token) {
        // todo 使用jjwt
        String[] userInfo = token.split("\\.");


        return new UserClaims(userInfo[0]);
    }

    // 从jwt中提取朱来的关于user的信息
    public static class UserClaims {
        public String name;

        public UserClaims(String name) {
            this.name = name;
        }
    }
}
