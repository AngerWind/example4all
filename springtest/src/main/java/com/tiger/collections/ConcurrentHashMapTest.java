package com.tiger.collections;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class ConcurrentHashMapTest {

    @Test
    public void test() {
        ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>(32);
        for (int i = 0; i < 100; i++) {
            map.put(String.valueOf(i), 100);
        }
        System.out.println("hello");
    }
}
