package com.tiger;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/25
 * @description
 */
public class JWTExample {

    public static void main(String[] args) {

        // 创建一个HMAC-SHA-256签名的的秘钥, 用于签名
        // 通常这个秘钥是从配置文件中读取的
        SecretKey key = Jwts.SIG.HS256.key().build();

        String jws = Jwts.builder()
                .subject("Joe") // 设置预定义的claim, sub
                .signWith(key) // 签名
                .compact(); // 压缩

        // 创建一个jws的解析器
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(key)
                .build();

        try {
            // 解析jws, 如果key无法验证签名, 那么会抛出SignatureException
            Jws<Claims> claims = jwtParser.parseSignedClaims(jws);

            Claims payload = claims.getPayload();
            boolean equals = payload.getSubject().equals("Joe");
        } catch (SignatureException e) {
            System.out.printf("使用key无法解析jws, e: %s", e.getMessage());
        } catch (JwtException e) {
            // ignore
        }
    }
}
