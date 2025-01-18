## 什么是jwt, jws, jwe

最简单的jwt包含两个部分

1. header

   header必须是一个json object, 用来表示消息的元数据

2. payload

   payload可以是一个字节数组, 用来表示任何东西(图像, 文本, 文档)

   但是通常情况下, 他是一个json object, 通过键值对来存储数据, 每个键值对被称为claim

   在jwt规范中, 有7个预定义的claim, 他们分别是

   1. `iss` (Issuer)

      - 描述：表示 JWT 的发行者，通常是一个 URL 或者其他标识符。
      - 示例：`"iss": "https://example.com"`
      
   2. `sub` (Subject)
      - 描述：表示 JWT 的主体，通常是用户的唯一标识符。
      - 示例：`"sub": "1234567890"`  

   3. `aud` (Audience)
      - 描述：表示 JWT 的接收者，可以是一个或多个接收者的标识符。
      - 示例：`"aud": "https://example.com/myapi"`
      
   4. `exp` (Expiration Time)
      - 描述：表示 JWT 的过期时间，通常是一个 Unix 时间戳（自 1970 年 1 月 1 日以来的秒数）。JWT 在此时间之后将不再被视为有效。
      - 示例：`"exp": 1609459200`（表示 2021 年 1 月 1 日）
      
   5. `nbf` (Not Before)

   	- 描述：表示 JWT 的生效时间，即在此时间之前该 JWT 不应被视为有效。
   	- 示例：`"nbf": 1609455600`（表示 2020 年 12 月 31 日）
        

   6. `iat` (Issued At)

      - 描述：表示 JWT 的发行时间，通常是一个 Unix 时间戳。可用于判断 JWT 的年龄。
      - 示例：`"iat": 1609455600`（表示 2020 年 12 月 31 日）

      7. `jti` (JWT ID)

         - 描述：表示 JWT 的唯一标识符，通常用于防止重放攻击。

         - 示例：`"jti": "unique-identifier"`




上面一个最简单的jwt, 任何人都可以**查看**以及**修改**其中的内容, 所以我们可以

1. 对jwt使用非对称算法, 私钥签名, 这样可以保存jwt没有被别人修改, 这就是jws

2. 非对称前面保证了jwt无法被修改, 但是保存在payload中的信息谁都可以查看

   为了保存jwt无法被别人查看, 我们可以**对jwt中的payload进行加密**, 这样谁都无法查看其中的内容了

注意: jwe并不是在jws的基础上构建的, jwe是在jwt之上构建的, 有自己的一套生成方式

所以说, 在生成之前, 应该想好了是生成jwe还是jws



最后, 尽管json具有很好的可读性, 但是占用空间较大, 所以我们可以对header和payload进行压缩, 使得他能够更有效的在网络上传输, 压缩算法通常是base64url



## jwt案例

1. 假设我们有一个header

   ~~~json
   {
     "alg": "none"
   }
   ~~~

   payload

   ~~~json
   The true sign of intelligence is not knowledge but imagination.
   ~~~

2. 移除json中所有不必要的空格

   ~~~java
   String header = '{"alg":"none"}'
   String payload = 'The true sign of intelligence is not knowledge but imagination.'
   ~~~

3. 获取每个字符的utf-8编码, 然后进行base64url编码

   ~~~java
   String encodedHeader = base64URLEncode( header.getBytes("UTF-8") )
   String encodedPayload = base64URLEncode( payload.getBytes("UTF-8") )
   ~~~

4. 使用`.`将header和payload连接

   ~~~java
   String compact = encodedHeader + '.' + encodedPayload + '.'
   ~~~

5. 最后就获得了一个jwt

   ~~~java
   eyJhbGciOiJub25lIn0.VGhlIHRydWUgc2lnbiBvZiBpbnRlbGxpZ2VuY2UgaXMgbm90IGtub3dsZWRnZSBidXQgaW1hZ2luYXRpb24u.
   ~~~

   

## jws案例

1. 假设我们有一个header

   ~~~json
   {
     "alg": "HS256" // 指定了jwt使用的签名算法
   }
   ~~~

   payload

   ~~~json
   {
     "sub": "Joe" 
   }
   ~~~

2. 移除json中所有不必要的空格

   ~~~java
   String header = '{"alg":"HS256"}'
   String claims = '{"sub":"Joe"}'
   ~~~

3. 获取每个字符的utf-8编码, 然后进行base64url编码

   ~~~java
   String encodedHeader = base64URLEncode( header.getBytes("UTF-8") )
   String encodedClaims = base64URLEncode( claims.getBytes("UTF-8") )
   ~~~

4. 使用`.`将header和payload连接

   ~~~java
   String concatenated = encodedHeader + '.' + encodedClaims
   ~~~

5. 使用非对称加密中的签名算法, 使用私钥对上面的字符串签名

   ~~~java
    SecretKey key = getMySecretKey() // 获取私钥
    byte[] signature = hmacSha256( concatenated, key ) // 对上面的字符串签名
   ~~~

6. 由于签名始终是字节数组，因此对签名进行 Base64URL 编码，并将其连接到带有句点字符“.”的`concatenated`字符串

   ~~~java
   String compact = concatenated + '.' + base64URLEncode( signature )
   ~~~

7. 最后就获得了一个jwt

   ~~~java
   eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.1KP0SsvENi7Uz1oQc07aXTL7kpQG5jBNIybqr60AlD4
   ~~~



如果想要检验jws, 那么可以直接访问`http://jwt.io`, 他可以直接显示jws的内容





## jwe案例

上面的jwe案例, 虽然我们在结尾添加了签名, 但是payload中的内容, 谁都可以看得见, 这样可能会导致敏感信息的泄露

所以我们可以使用对称加密对jws的每一个部分(header, payload, sign)都进行对称加密, 等到如下的字符串

~~~java
eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0.
6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ.
AxY8DCtDaGlsbGljb3RoZQ.
KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY.
U0m_YmjN04DJvceFICbCVQ
~~~

## 安装

1. maven

   ~~~xml
   <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt-api</artifactId>
       <version>0.12.6</version>
   </dependency>
   <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt-impl</artifactId>
       <version>0.12.6</version>
       <scope>runtime</scope>
   </dependency>
   <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson if Gson is preferred -->
       <version>0.12.6</version>
       <scope>runtime</scope>
   </dependency>
   <!-- 如果
   	- jdk10或之前, 又想要使用RSASSA-PSS (PS256, PS384, PS512)签名算法
   	- jdk10或之前, 又想要使用EdECDH (X25519 or X448) Elliptic Curve Diffie-Hellman加密算法
   	- jdk14或之前, 有想要使用EdDSA (Ed25519 or Ed448) Elliptic Curve 签名算法
   那么需要添加这个依赖
   jdk15或者之后, 不需要添加这个依赖
   -->
   <dependency>
       <groupId>org.bouncycastle</groupId>
       <artifactId>bcprov-jdk18on</artifactId> or bcprov-jdk15to18 on JDK 7
       <version>1.76</version>
       <scope>runtime</scope>
   </dependency>
   -->
   ~~~

2. gradle

   ~~~groovy
   dependencies {
       implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
       runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
       runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6' // or 'io.jsonwebtoken:jjwt-gson:0.12.6' for gson
       
   	// 如果
   	// - jdk10或之前, 又想要使用RSASSA-PSS (PS256, PS384, PS512)签名算法
   	// - jdk10或之前, 又想要使用EdECDH (X25519 or X448) Elliptic Curve Diffie-Hellman加密算法
   	// - jdk14或之前, 有想要使用EdDSA (Ed25519 or Ed448) Elliptic Curve 签名算法
   	// 那么需要添加这个依赖
   	// jdk15或者之后, 不需要添加这个依赖
   	runtimeOnly 'org.bouncycastle:bcprov-jdk18on:1.76' // or bcprov-jdk15to18 on JDK 7
   }
   ~~~

   

## jwt

### 生成jwt

1. 创建一个JwtBuilder

   ~~~java
   //  创建一个builder
   JwtBuilder builder = Jwts.builder();
   ~~~

2. 设置header

   ~~~java
           // 对于一些通用的每次都要设置的header, 我们可以通过Jwts.header()方法来创建一个Header, 然后复用他
           Header commonHeaders = Jwts.header()
                   .add("ddd", "DDD")
                   .add("eee", "EEE")
                   .build();
   
           // jjwt会自动根据配置来设置header中的alg, enc, zip字段
           builder.header()
                   .add(commonHeaders) // 设置通用的header
                   .add("aaa", "bbb") // 设置自定义的header
                   .add("ccc", "ddd");
   
   		// 可以通过添加一个map, 来设置自定义的header
   ~~~

3. 设置payload

   **payload有可能是一个json object, 也可以是任意的字节数组, 所以一下方式二选一**

   - payload是claims(json object)

     ~~~java
             // 通过claims来设置payload, 这种方式将payload设置为json对象
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
     ~~~

   - payload是字节数组

     ~~~java
     // 会添加一个cty: "text/plain"的字段到header, 用来说明字节数组的内容格式是什么
     builder.content("Hello, World!".getBytes(), "text/plain");
     ~~~

4. 生成jwt

   ~~~java
   String jwe = builder.compact();
   ~~~

   

### 读取jwt

~~~java
    private static void parseJWT(String token) {
        
        JwtParserBuilder builder = Jwts.parser();
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


            // 5. 获取claims, header, 字节数组, 然后执行代码
            Claims claims = jwtWithClaims.getPayload();
            Header header = jwtWithClaims.getHeader();

            JwsHeader header1 = jwsWithClaims.getHeader();
        } catch (JwtException exception) {
            // ignore
        }
    }
~~~

### Claim断言

如果你希望在调用parseXXX来解析token的时候, 要求其中的Claims必须含有特定的值, 那么可以使用如下的代码

~~~java
try {
    Jwts.parser()
        .requireSubject("jsmith") // 对预定义的claim进行校验
        .require("myClaim", "myRequiredValue") // 对自定义的claim进行校验
        /* etc... */
        .build()
        .parse(s);
} catch(MissingClaimException mce) {
    // jwt中没有对应的claim
} catch(IncorrectClaimException ice) {
    // jwt中对应的claim不是指定的值
}
~~~



## jws

生成jws的代码和生成jwt的代码没什么差别, 只是需要使用特定的签名算法来对jwt进行签名



JWT 规范确定了3 种密钥算法和 10 种非对称密钥算法：

| Identifier 标识符 | 对称加密签名算法     | 秘钥的要求   |
| ----------------- | -------------------- | ------------ |
| `HS256`           | 使用 SHA-256 的 HMAC | 至少256位bit |
| `HS384`           | 使用 SHA-384 的 HMAC | 至少384位bit |
| `HS512`           | 使用 SHA-512 的 HMAC | 至少512位bit |

| Identifier 标识符 | 非对称加密签名算法                                           | 秘钥的要求                                   |
| ----------------- | ------------------------------------------------------------ | -------------------------------------------- |
| `ES256`           | 使用 P-256 和 SHA-256 的 ECDSA                               | 恰好256bit                                   |
| `ES384`           | 使用 P-384 和 SHA-384 的 ECDSA                               | 恰好384bit                                   |
| `ES512`           | 使用 P-521 和 SHA-512 的 ECDSA                               | 恰好512bit                                   |
| `RS256`           | 使用 SHA-256 的 RSASSA-PKCS-v1_5                             | 至少2048bit                                  |
| `RS384`           | 使用 SHA-384 的 RSASSA-PKCS-v1_5                             | 至少3072bit                                  |
| `RS512`           | 使用 SHA-512 的 RSASSA-PKCS-v1_5                             | 至少4096bit                                  |
| `PS256`           | RSASSA-PSS using SHA-256 and MGF1 with SHA-256<br>运行时类路径中需要 Java 11 或兼容的 JCA 提供程序（如 BouncyCastle） | 至少2048bit                                  |
| `PS384`           | RSASSA-PSS using SHA-384 and MGF1 with SHA-384<br/>运行时类路径中需要 Java 11 或兼容的 JCA 提供程序（如 BouncyCastle） | 至少3072bit                                  |
| `PS512`           | RSASSA-PSS using SHA-512 and MGF1 with SHA-512<br/>运行时类路径中需要 Java 11 或兼容的 JCA 提供程序（如 BouncyCastle） | 至少4096bit                                  |
| `EdDSA`           | Edwards-Curve Digital Signature Algorithm (EdDSA)<br/>运行时类路径中需要 Java 15 或兼容的 JCA 提供程序（如 BouncyCastle） | Ed25519要求必须256bit<br>Ed448要求必须456bit |

这些都在`io.jsonwebtoken.Jwts.SIG`注册表类中表示为常量。

对于上述的算法, 都对秘钥的强度有要求, 比如HS256要求至少32字节长的秘钥, HS384要求至少48字节长的秘钥, RS512要求至少512字节长的秘钥



### 创建秘钥

jjwt提供了方便的工具, 用来生成符合对应算法要求的秘钥

1. 创建对称加密的签名秘钥

   ~~~java
   SecretKey key = Jwts.SIG.HS256.key().build(); //or HS384.key() or HS512.key()
   ~~~

   如果您需要保存这个新的`SecretKey` 到文本文件中，您可以对其进行 Base64（或 Base64URL）编码：

   ~~~java
   String secretString = Encoders.BASE64.encode(key.getEncoded());
   ~~~

2. 创建非对称加密的秘钥

   ~~~java
   KeyPair keyPair = Jwts.SIG.RS256.keyPair().build(); //or RS384, RS512, PS256, etc...
   
   PrivateKey privateKey = keyPair.getPrivate(); // 获取私钥, 用于签名
   PublicKey publicKey = keyPair.getPublic(); // 获取公钥, 用于解析和校验
   ~~~



### 保存和回复秘钥

在一般情况下, 你都会创建一个秘钥, 然后将其保存到db,redis, 或者配置文件中

然后再要使用的时候, 将其读取并转换



对于对称加密的秘钥, 你可以使用如下的方式进行保存和恢复

1. 保存秘钥

   ~~~java
   SecretKey key = Jwts.SIG.HS256.key().build();
   byte[] encodedKeyBytes = key.getEncoded();     // 直接获得字节数组, 可以保存到db, redis
   String secretString = Encoders.BASE64.encode(encodedKeyBytes); // 转换为base64字符串, 可以保存到配置文件中
   String secretStringBase64Url = Encoders.BASE64URL.encode(encodedKeyBytes); // 转换为base64url字符串, 可以保存到配置文件中
   ~~~

2. 从db, redis, 配置文件中加载秘钥

   ~~~java
   SecretKey key1 = Keys.hmacShaKeyFor(encodedKeyBytes); // 直接从字节数组中获取key
   SecretKey key2 = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString)); // 直接从base64字符串中获取key
   SecretKey key3 = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretStringBase64Url));// 直接从base64url字符串中获取key
   
   // todo
   Password password = Keys.password(secretString.toCharArray()); // 从密码中获取key
   ~~~



对于非对称加密的秘钥, 你可以使用如下的方式进行保存和恢复



~~~java
        KeyPair keyPair = SIG.RS512.keyPair().build();

        // 保存私钥
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] privateKeyBytes = privateKey.getEncoded();     // 直接获得字节数组, 可以保存到db, redis
        String privateKeyBase64 = Encoders.BASE64.encode(privateKeyBytes); // 转换为base64字符串, 可以保存到配置文件中
        String privateKeyBase64Url = Encoders.BASE64URL.encode(privateKeyBytes); // 转换为base64url字符串, 可以保存到配置文件中

        // 恢复私钥
        byte[] decodePrivateKeyBytes1 = privateKeyBytes; // 直接就是私钥的字节数组
        byte[] decodePrivateKeyBytes2 = Decoders.BASE64.decode(privateKeyBase64); // 通过base64解码为字节数组
        byte[] decodePrivateKeyBytes3 = Decoders.BASE64URL.decode(privateKeyBase64Url); // 通过base64url解码为字节数组

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodePrivateKeyBytes1);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey1 = keyFactory.generatePrivate(keySpec);


        // 保存公钥
        PublicKey publicKey = keyPair.getPublic();
        byte[] publicKeyBytes = publicKey.getEncoded();     // 直接获得字节数组, 可以保存到db, redis
        String publicKeyBase64 = Encoders.BASE64.encode(publicKeyBytes); // 转换为base64字符串, 可以保存到配置文件中
        String publicKeyBase64Url = Encoders.BASE64URL.encode(publicKeyBytes); // 转换为base64url字符串, 可以保存到配置文件中

        // 恢复公钥
        byte[] decodePublicKeyBytes1 = publicKeyBytes; // 直接就是私钥的字节数组
        byte[] decodePublicKeyBytes2 = Decoders.BASE64.decode(publicKeyBase64); // 通过base64解码为字节数组
        byte[] decodePublicKeyBytes3 = Decoders.BASE64URL.decode(publicKeyBase64Url); // 通过base64url解码为字节数组

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodePublicKeyBytes1);
        KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
        PublicKey publicKey1 = keyFactory1.generatePublic(publicKeySpec);


        KeyPair keyPair1 = new KeyPair(publicKey1, privateKey1);

        assert privateKey1.equals(privateKey);
        assert publicKey1.equals(publicKey);
~~~







### 生成jws

生成jws和生产jwt的代码是一样的, 只不过要调用一个`signWith`方法来对jwt进行签名

1. 调用signWith(key)进行签名,  他接受一个对称签名的SecretKey, 或者非对称加密签名的PrivateKey

2. signWith(key)会根据秘钥来确定最适合的签名算法

   如果您使用长度为 256 位的SecretKey调用signWith ，则它对于HS384或HS512来说不够强大，因此 JJWT 将自动使用HS256对 JWT 进行签名。

3. 可以通过signWith(privateKey, Jwts.SIG.RS512)来强制指定需要使用的签名算法, 不推荐

4. signWith还会自动添加alg字段到header中

~~~java
String jws = Jwts.builder() // 1. 创建builder
    // 2. 指定header
    // 3. 指定paylaod
    
    .signWith(key)  // 4. 签名
    .compact();   
~~~



### 读取jws

读取jws的代码和读取jwt的代码是一样的, 只不过需要调用一个`verifyWith`的方法, 代码如下

~~~java
try {
    
    Jws<Claims> jws = Jwts.parser() 
    .keyLocator(keyLocator)
    .verifyWith(key)    // 如果你使用的是对称加密签名, 那么要使用相同的秘钥, 如果使用的是非对称加密签名, 那么这里应该传入PublicKey
    .build()     
    // 如果payload是json object, 那么调用parseSignedClaims(jwsString), 返回Jws<Claims>
    // 如果payload是byte[], 那么调用parseSignedContent(jwsString), 会返回Jws<byte[]>
    .parseSignedClaims(jwsString); 

    // we can safely trust the JWT

catch (JwtException ex) { 

    // we *cannot* use the JWT as intended by its creator
}
~~~



## jwe



### 概述

上面说到了, jws能够保证发送的token无法被人修改, 但是他并不能防止别人查看jws中paylaod中的具体内容, 所以我们可以选择对jwt中的payload进行加密处理, 这样别人就无法查看jwt的payload中的具体内容了



注意: **jwe并不是在jws的基础上构建的, jwe是在jwt之上构建的, 有自己的一套生成方式, 所以说, 在生成之前, 应该想好了是生成jwe还是jws**



### payload的加密算法

在jwt规范中, 定义了6中对payload的加密算法

| 标识符          | 要求秘钥的长度(bit) | 加密算法                                                     |
| --------------- | ------------------- | ------------------------------------------------------------ |
| `A128CBC‑HS256` | 256                 | [AES_128_CBC_HMAC_SHA_256](https://www.rfc-editor.org/rfc/rfc7518.html#section-5.2.3) authenticated encryption algorithm |
| `A192CBC-HS384` | 384                 | [AES_192_CBC_HMAC_SHA_384](https://www.rfc-editor.org/rfc/rfc7518.html#section-5.2.4) authenticated encryption algorithm |
| `A256CBC-HS512` | 512                 | [AES_256_CBC_HMAC_SHA_512](https://www.rfc-editor.org/rfc/rfc7518.html#section-5.2.5) authenticated encryption algorithm |
| `A128GCM`       | 128                 | AES GCM using 128-bit key (需要jdk8)                         |
| `A192GCM`       | 192                 | AES GCM using 192-bit key (需要jdk8)                         |
| `A256GCM`       | 256                 | AES GCM using 256-bit key (需要jdk8)                         |

我们可以使用`io.jsonwebtoken.Jwts.ENC`来引用这些算法



上面我们可以发现, **这些对payload进行加密的算法, 都是AES算法的变体, 而AES算法是对称加密算法!!!**

那么我们就不能使用RSA和椭圆曲线非对称加密了吗? 也不能使用Diffie-Hellman key exchange?

实际上, **这些这些加密算法都是支持的, 但是他们并不用于加密paylaod, 而是使用这些秘钥通过JWE秘钥管理算法(JWE Key Management Algorithms), 来生成对应AES加密算法的秘钥(JWE Content Encryption Key / CEK), 然后使用这个秘钥来对paylaod进行加密**

所以实际上, payload的加密过程是分为两个步骤的

~~~java
Key algorithmKey = getKeyManagementAlgorithmKey(); // 先获取我们要使用的PublicKey, SecretKey, or Password, 这个key并不直接用于加密paylaod
SecretKey contentEncryptionKey = keyManagementAlgorithm.produceEncryptionKey(algorithmKey); // 通过上面的key, 然后经过jwe秘钥管理算法, 生成一个用于AES对称加密的秘钥SecretKey

byte[] ciphertext = encryptionAlgorithm.encrypt(payload, contentEncryptionKey); // 使用生成后的SecretKey对payload进行加密
~~~



为什么不能直接使用RSA和椭圆曲线算法或者password来直接对payload进行加密呢?

1. 非对称密钥加密（如 RSA 和椭圆曲线）往往很慢。就像*真的很*慢一样。相比之下，对称密钥密码算法*确实非常快*
2. RSA加密（例如）只能加密相对少量的数据。 2048 位 RSA 密钥最多只能加密 245 个字节。 4096 位 RSA 密钥最多只能加密 501 字节。有大量 JWT 可能超过 245 字节，这将使 RSA 无法使用。
3. 密码通常会产生非常差的加密密钥, 或者它们本身通常太短而无法达到最小密钥长度

所以选择通过一种加密算法的秘钥, 来生成另一个对称加密算法的秘钥, 可以用来提高安全性, 又可以非常快速的加解密



### Key Management Algorithms

在jwt标准中, 定义了17种Key Management Algorithms, 用来生成直接对paylaod进行加密的秘钥

| Identifier           | Key Management Algorithm                                     |
| -------------------- | ------------------------------------------------------------ |
| `RSA1_5`             | RSAES-PKCS1-v1_5                                             |
| `RSA-OAEP`           | RSAES OAEP using default parameters                          |
| `RSA-OAEP-256`       | RSAES OAEP using SHA-256 and MGF1 with SHA-256               |
| `A128KW`             | AES Key Wrap with default initial value using 128-bit key    |
| `A192KW`             | AES Key Wrap with default initial value using 192-bit key    |
| `A256KW`             | AES Key Wrap with default initial value using 256-bit key    |
| `dir`                | Direct use of a shared symmetric key as the Content Encryption Key |
| `ECDH-ES`            | Elliptic Curve Diffie-Hellman Ephemeral Static key agreement using Concat KDF |
| `ECDH-ES+A128KW`     | ECDH-ES using Concat KDF and CEK wrapped with "A128KW"       |
| `ECDH-ES+A192KW`     | ECDH-ES using Concat KDF and CEK wrapped with "A192KW"       |
| `ECDH-ES+A256KW`     | ECDH-ES using Concat KDF and CEK wrapped with "A256KW"       |
| `A128GCMKW`          | Key wrapping with AES GCM using 128-bit key (需要jdk8)       |
| `A192GCMKW`          | Key wrapping with AES GCM using 192-bit key(需要jdk8)        |
| `A256GCMKW`          | Key wrapping with AES GCM using 256-bit key(需要jdk8)        |
| `PBES2-HS256+A128KW` | PBES2 with HMAC SHA-256 and "A128KW" wrapping(需要jdk8)      |
| `PBES2-HS384+A192KW` | PBES2 with HMAC SHA-384 and "A192KW" wrapping(需要jdk8)      |
| `PBES2‑HS512+A256KW` | PBES2 with HMAC SHA-512 and "A256KW" wrapping(需要jdk8)      |

这些Key Management Algorithms都在`io.jsonwebtoken.Jwts.KEY`中定义为常量

上面的17种算法, 我们应该怎么去选择呢?

1. 当你想要**使用接受者的RSA公钥来进行加密的时候**, 可以选择`RSA1_5`, `RSA-OAEP`, and `RSA-OAEP-256`算法

   他的加解密过程如下:

   - 选择你想要的payload的加密算法, 然后生成一个随机的对应SecretKey (CEK)
   - 使用这个CEK来加密payload, 生成payload密文
   - 使用接受者的RSA公钥, 来加密这个CEK
   - 将payload密文和CEK密文放到jwt中, 生成jwe

   解密过程如下

   - 接受者接受到jwe后, 使用RSA私钥解密CEK密文, 得到CEK
   - 使用CEK解密payload密文

2. 当你想要**使用接受者的椭圆曲线公钥来进行加密时**, 可以选择`ECDH-ES` 、 `ECDH-ES+A128KW` 、 `ECDH-ES+A192KW`和`ECDH-ES+A256KW`

   他的加密过程如下:

   - https://github.com/jwtk/jjwt?tab=readme-ov-file#elliptic-curve-diffie-hellman-ephemeral-static-key-agreement-ecdh-es, 看不懂

3. 当你**有一个AES对称加密的秘钥, 但是又不想要直接使用该秘钥来加解密payload时**, 可以使用`A128KW` 、 `A192KW` 、 `A256KW` 、 `A128GCMKW` 、 `A192GCMKW`和`A256GCMKW` 

   **（！！！！推荐！！！！）**

   他的加解密过程如下:

   - 选择你想要的payload加密算法, 然后生成一个随机的对应SecretKey (CEK)
   - 使用这个CEK来加密payload, 生成payload密文
   - 使用你的AES秘钥, 对CEK进行加密, 生成CEK密文
   - 将payload密文和CEK密文放到jwt中, 生成jwe

   解密过程如下

   - 接受者接受到jwe后, 使用相同的AES秘钥 解密CEK密文, 得到CEK
   - 使用CEK解密payload密文

4. 当你**有一个AES对称加密的秘钥, 并希望直接使用他来对payload加解密时**, 可以使用`dir`

   他的加解密过程如下:

   - 选择你想要的payload加密算法, 然后直接使用AES秘钥对payload进行加密
   - 将加密后的payload放到jwt中, 生成jwe

   解密过程如下:

   - 使用AES秘钥对payload解密即可

5. 当**你想要使用密码(字母和数字)进行加解密的时候**, 可以选择`PBES2-HS256+A128KW` 、 `PBES2-HS384+A192KW`和`PBES2-HS512+A256KW`

   加密过程如下:

   - 选择你想要的payload加密算法, 然后生成一个随机的对应SecretKey (CEK)
   - 使用这个CEK来加密payload, 生成payload密文
   - 从`PBES2-HS256+A128KW` 、 `PBES2-HS384+A192KW`和`PBES2-HS512+A256KW`中选择一个算法, 对密码进行计算, 生成一个派生的key
   - 使用派生的key, 对CEK进行AES加密
   - 将加密后的payload和加密后的CEK嵌入到jwt中, 生成jwe

   解密过程如下:

   - 从jwe中检索加密后的CEK
   - 从`PBES2-HS256+A128KW` 、 `PBES2-HS384+A192KW`和`PBES2-HS512+A256KW`中选择一个算法, 对密码进行计算, 生成一个派生的key
   - 使用派生的key, 对加密后的CEK进行AEK解密, 得到CEK
   - 使用CEK对加密后的payload进行解密, 得到paylaod



### 生成jwe

经过上面的讲解之后， 我们在生成jwe的时候， 需要选择的东西有三种：

- 根据你的情况的不同， 使用不同类型的秘钥
  1. 使用接受者的RSA公钥
  2. 使用接受者的椭圆曲线公钥
  3. 使用密码
  4. AES对称加密的秘钥
  5. 使用AES对称加密的秘钥直接加密

- 根据秘钥的情况， 选择具体的key management algorithms

- 选择加密payload的加密算法

~~~java
SecretKey secretKey = KEY.A192KW.key().build();
String jwe = Jwts.builder()
   .subject("Bob")
   // secretKey根据你的情况, 可以是
   // RSA的PublicKey, 
   // 椭圆曲线的PublicKey, 
   // Password, Password根据Password password = Keys.password(passwordChars);生成
   // AES对称加密的SecretKey
   .encryptWith(secretKey, KEY.A192KW, ENC.A128CBC_HS256)
   .compact(); 
~~~





### 读取jwe

~~~java
try {
    Jwe<Claims> jwe = Jwts.parser()  
    .build()   
    // 解密的key根据你的情况, 可以是
    // RSA的PrivateKey, 
    // 椭圆曲线的PrivateKey, 
    // Password, Password根据Password password = Keys.password(passwordChars);生成
    // AES对称加密的SecretKey
    .decryptWith(key)
        
    // 如果paylaod是byte[], 那么调用parseEncryptedContent(jweString), 返回一个Jwe<byte[]>
    .parseEncryptedClaims(jweString); 

    // we can safely trust the JWT
    // do something
catch (JwtException ex) {  
    // we *cannot* use the JWT as intended by its creator
}
~~~



  

## 动态的key

在某些情况下, 你可能有不止一个key, 而是有多个key来生成jwe/jes

甚至每个用户都有一个对应的生成jws/jwe的key

那么在解析jws/jwe的时候, 如果不事先检查token, 我们根本就不知道使用哪个秘钥来解析jws/jwe, 所以也就不能调用`verifyWith/decryptWith`方法来指定秘钥了

在这种情况下, jjwt提供了一个`keyLocator()`的方法,  这个方法需要接受一个`KeyLocator`接口, 这个接口中有一个`locate`方法, 用于根据jws或者jwe的Header信息, 来查找对应的key

~~~java
Locator<Key> keyLocator = new MyKeyLocator();

Jwts.parser()
    .keyLocator(keyLocator) // 指定keyLocator, 用于根据Header来查找对应的key
    .build();

public class MyKeyLocator extends LocatorAdapter<Key> {

    // jjwt会先从token中解析header, 然后将header传入这个方法中, 用来查找key
    // 查找到对应的key之后, 然后用于验证jws签名, 解密jwe的密文
    
    // 接受到的header, 根据解析的是jws还是jwe, 具体的类型为JwsHeader/JweHeader
    @Override
    public Key locate(ProtectedHeader<?> header) {
        // 在这个方法中, 依据header中包含的信息, 查找对应的key
        // 通用的做法是在创建token的时候, 在header中设置一个kid的字段, 用来关联用户
        String keyId = header.getKeyId(); 
        
        // 对于使用对称加密签名的jws, 应该返回签名时使用的SecretKey
        // 对于使用非对称加密签名的jws, 应该返回签名时使用的PrivateKey对应的PublicKey
        
        // 对于使用接受者RSA PublicKey加密的jwe, 应该返回RSA PublicKey对应的PrivateKey
        // 对于使用接受者椭圆曲线PublicKey加密的jwe, 应该返回加密时使用的PublicKey对应的PrivateKey
        // 对于使用基于密码的密钥派生算法，返回的解密密钥应该是Password 。
        // 您可以通过调用创建一个Password实例 Keys.password(char[] passwordCharacters) 。
        // 对于AES对称加密的jwe, 应该返回加密是的SecretKey
        Key key = lookupKey(keyId);
        return key;
    }
}
~~~

上面说到locate方法中传入的header的具体类型依据解析的token是jwe还是jws, 具体类型是JweHeader/JwsHeader

如果在解析jws和jwe的时候, 具有不同的key的查找策略, 那么你也可以分别对应的方法

~~~java
public class MyKeyLocator extends LocatorAdapter<Key> {

    // 不再实现public Key locate(ProtectedHeader<?> header)方法
    
    @Override
    public Key locate(JwsHeader header) {
        String keyId = header.getKeyId(); //or any other parameter that you need to inspect
        // 对于使用对称加密签名的jws, 应该返回签名时使用的SecretKey
        // 对于使用非对称加密签名的jws, 应该返回签名时使用的PrivateKey对应的PublicKey
        return lookupSignatureVerificationKey(keyId); 
    }

    @Override
    public Key locate(JweHeader header) {
        String keyId = header.getKeyId(); //or any other parameter// that you need to inspect
        // 对于使用接受者RSA PublicKey加密的jwe, 应该返回RSA PublicKey对应的PrivateKey
        // 对于使用接受者椭圆曲线PublicKey加密的jwe, 应该返回加密时使用的PublicKey对应的PrivateKey
        // 对于使用基于密码的密钥派生算法，返回的解密密钥应该是Password 。
        // 您可以通过调用创建一个Password实例 Keys.password(char[] passwordCharacters) 。
        // 对于AES对称加密的jwe, 应该返回加密是的SecretKey
        return lookupDecryptionKey(keyId); 
    }
}
~~~





## 案例

https://github.com/jwtk/jjwt?tab=readme-ov-file#examples



## 压缩和解压缩

- 压缩

  ~~~java
  // 在创建的时候指定压缩算法
  // 会自动在header中添加一个zip字段, 用来表示使用的压缩算法
  Jwts.builder()
     .compressWith(Jwts.ZIP.DEF) // DEFLATE compression algorithm
     // .. etc ...
      .build()
      
  // 自定义压缩算法
  // https://github.com/jwtk/jjwt?tab=readme-ov-file#custom-compression-algorithm
  ~~~

- 解压

  ~~~java
          //  默认情况下, jjwt会根据header中的zip字段自动选择解压算法
          //  然而如果你使用了自定义的解压算法, 那么你必须将对应的压缩算法添加到builder中, 这样jjwt才可以根据zip字段找到对应的解压算法
          builder.zip().add(new MyCompressionAlgorithm).and();
  ~~~

  

