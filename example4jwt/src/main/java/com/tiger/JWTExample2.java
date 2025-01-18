package com.tiger;

import javax.crypto.SecretKey;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts.ENC;
import io.jsonwebtoken.Jwts.KEY;


import java.security.*;
import java.security.spec.InvalidKeySpecException;

import java.util.Date;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/25
 * @description
 */
public class JWTExample2 {

    // 创建一个HMAC-SHA-256签名的的秘钥, 用于签名, 通常这个秘钥是从配置文件中读取的
    static SecretKey key = Jwts.SIG.HS256.key().build();

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {


        String jwe = createJWE();
        parseJWE(jwe);


    }

    private static void parseJWE(String token) {
        // 1. 创建一个ParserBuilder
        JwtParserBuilder builder = Jwts.parser();

        // 2. 如果要解析加密的, 带签名的jwt, 那么可以选择调用keyLocator 、 verifyWith或decryptWith方法。
        // builder.keyLocator(keyLocator) //
        // builder.verifyWith(key) // 校验jwe
        // builder.decryptWith(key1); // 解密jwe

        // 3. 对claims进行断言(可选)
        //    如果你希望解析jwt的时候, jwt中必须包含指定的属性, 那么可以调用requireXXX方法
        //    如果jwt中不包含这些属性, 那么在调用parseXXX的时候, 会抛出MissingClaimException, IncorrectClaimException异常
        builder.require("xxx", "xxx");
        builder.require("yyy", "yyy");

        // 4. 指定解压算法(可选)
        //  默认情况下, jjwt会根据header中的zip字段自动选择解压算法
        //  然而如果你使用了自定义的解压算法, 那么你必须将对应的压缩算法添加到builder中, 这样jjwt才可以根据zip字段找到对应的解压算法
        // builder.zip().add(new MyCompressionAlgorithm).and();

        // 3. 创建parser
        JwtParser jwtParser = builder.build();

        try {
            // 4. 调用各种parse*方法, 来解析jwt

            // 通用的解析方法, 因为无法判断字符串到底是jwt, jwe, jes, 所以需要类型判断
            // 同时也无法判断payload中到底是claims还是字节数组, 所以也需要类型判断
            Jwt<?, ?> jwt = jwtParser.parse(token);
            if (jwt instanceof Jwe<?> jwe) {
                Object payload = jwe.getPayload();
                if (payload instanceof Claims) {
                    Claims claims = (Claims)payload;
                }
                if (payload instanceof byte[]) {
                    byte[] content = (byte[])payload;

                }
            } else if (jwt instanceof Jws<?> jws) {
                Object payload = jws.getPayload();
                if (payload instanceof Claims) {
                    Claims claims = (Claims)payload;
                }
                if (payload instanceof byte[]) {
                    byte[] content = (byte[])payload;
                }
            }


            Jwt<Header, Claims> jwtWithClaims = jwtParser.parseUnsecuredClaims(token);// 解析payload是claims的jwt
            Jwt<Header, byte[]> jwtWithContent = jwtParser.parseUnsecuredContent(token);// 解析payload是字节数组的jwt
            Jws<Claims> jwsWithClaims = jwtParser.parseSignedClaims(token);// 解析payload是claims的jws
            Jws<byte[]> jwsWithContent = jwtParser.parseSignedContent(token);// 解析payload是字节数组的jws
            Jwe<Claims> jweWithClaims = jwtParser.parseEncryptedClaims(token);// 解析payload是claims的jwe
            Jwe<byte[]> jweWithContent = jwtParser.parseEncryptedContent(token);// 解析payload是字节数组的jwe
            // jwtParser.isSigned()


            // 5. 可以调用各种require*来对jwt进行校验
            Claims claims = jwtWithClaims.getPayload();
            Header header = jwtWithClaims.getHeader();

            JwsHeader header1 = jwsWithClaims.getHeader();


        } catch (JwtException exception) {
            // ignore
        }

    }

    public static String createJWE() {
        // 1. 创建一个builder
        JwtBuilder builder = Jwts.builder();

        // 2. 设置header

        // 对于一些通用的每次都要设置的header, 我们可以通过Jwts.header()方法来创建一个Header
        // 然后复用他
        Header commonHeaders = Jwts.header()
                .add("ddd", "DDD")
                .add("eee", "EEE")
                .build();

        // jjwt会自动根据配置来设置header中的alg, enc, zip字段
        builder.header()
                .add(commonHeaders) // 设置通用的header
                .add("aaa", "bbb")
                .add("ccc", "ddd");

        // 3.1. 通过claims来设置payload, 这种方式将payload设置为json对象
        builder.claim("xxx", "xxx") // 自定义的claim
                .claim("yyy", "YYY")
                .id("238923829") // jwt的id, 用于防止重放攻击
                .issuer("https://example.com") // jwt的发行者, 通常是一个域名
                .subject("32872934872") // 用户的唯一id
                .audience() // 设置jwt的接受者, 可以有多个
                .add("https://example.com/myapi") // 通过add 方法来添加audience
                .add("https://example2.com/")
                .and() // 通过and()方法继续返回jwtBuilder
                .expiration(new Date()) // jwt的过期时间
                .issuedAt(new Date()) // jwt的生成时间
                .notBefore(new Date()); // jwt的生效时间

        // 3.2. 通过content来设置payload, 这种方式可以将payload设置为任何字节数组
        //      两种方式只能使用其中一种

        // 会添加一个cty: "text/plain"的字段到header, 用来说明字节数组的内容格式是什么
        // builder.content("Hello, World!".getBytes(), "text/plain");


        SecretKey secretKey = KEY.A192KW.key().build();
        String jwe = Jwts.builder()
                .subject("Bob")
                .encryptWith(secretKey, KEY.A192KW, ENC.A128CBC_HS256)
                .compact();
        // 4. 签名或者加密, 二者只能选其一
        // todo
        // builder.signWith(key);
        // builder.encryptWith(key)

        // 5. 压缩(可选)
        // jwt标准只支持对jwe进行压缩, 但是jjwt也能够对jws进行压缩, 只要你同样使用jjwt来解压jws
        // builder.compressWith(ZIP.DEF); // 使用deflate压缩算法, 会自动设置zip header
        // builder.compressWith(ZIP.GZIP); // 使用gzip压缩算法, 会自动设置zip header

        // 自定义压缩算法: https://github.com/jwtk/jjwt?tab=readme-ov-file#custom-compression-algorithm

        // 5. 生成jwt/jwe/jws
        String jwe1 = builder.compact();

        return jwe1;
    }
}
