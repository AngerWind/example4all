package com.tiger.post_contrustor_test;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Super
 * @date 2021/1/27 10:19
 * @description
 */
public class Super {

    public Super() {
        System.out.println("super contrustor");
    }

    @PostConstruct
    public void init() {
        System.out.println("super post construst");
    }

    public static void main(String[] args) {
        // HashSet<String> strings = new HashSet<>();
        // strings.add("127.0.0.1:123");
        // strings.add("127.0.2.1:123");
        // strings.add("127.0.1.1:123");
        // strings.add("127.0.3.1:123");
        //
        // Set<String> collect = strings.stream().map(h -> {
        //     if (h instanceof String) {
        //         return "'" + h + "'";
        //     }
        //     return null;
        // }).filter(Objects::nonNull).collect(Collectors.toSet());
        // String hostStr = String.join(",", collect);
        // System.out.println(hostStr);

        String host = "127.0.0.1:8888";
        if (host.contains(":")) {
            host = host.split(":")[0];
        }
        System.out.println(host);
    }
}
