package com.tiger;

import java.lang.instrument.Instrumentation;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/1/3
 * @description
 */
public class PremainAgent {

    /**
     * 1. premain方法，会在main方法之前被调用
     * 2. premain报错会导致jvm退出
     * 3. 通过JVM参数 -javaagent:jarpath[=key1=value1&key2=value2]
     * 4. 多个-javaagent的premain按照顺序调用
     */
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("premain start!");
        System.out.println(args);
        addTransformer(instrumentation);
        System.out.println("premain end!");
    }

    private static void addTransformer(Instrumentation instrumentation) {
        /*
            Instrumentation提供的addTransformer方法，在类加载时会回调ClassFileTransformer接口
            回调的顺序是:
                1. 将Transformer根据canRetransform分为两类
                2. 按照注册顺序调用canRetransform为false的这一类
                3. 按照注册顺序调用canRetransform为true的这一类
         */
        instrumentation.addTransformer(new InsertClassFileTransformer("com.tiger.PremainDemo", "main", "111"), true);
        instrumentation.addTransformer(new InsertClassFileTransformer("com.tiger.PremainDemo", "main", "222"));
    }
}
