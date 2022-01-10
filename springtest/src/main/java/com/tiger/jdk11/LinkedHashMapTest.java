package com.tiger.jdk11;

import org.junit.Test;

import java.util.LinkedHashMap;

/**
 * @author Shen
 * @version v1.0
 * @Title LinkedHashMapTest
 * @date 2022/1/3 15:23
 * @description
 */
public class LinkedHashMapTest {

    @Test
    public void test1() {
        LinkedHashMap<Object, Object> map = new LinkedHashMap<Object, Object>(2);
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.put("k5", "v4");
    }
}
