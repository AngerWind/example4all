package com.tiger.spring_sub_test.bean_factory;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.core.SimpleAliasRegistry;
import java.util.Arrays;

public class SimpleAliasRegistryTest {

    @Test
    @SneakyThrows
    public void test(){
        /**
         * 内部使用 Map来保存数据，key为alia，value为name
         * 一个name可以对应多个alia， alia又可以作为name再对应多个其他的alia
         * 所以可以将其看做一颗树，name作为父节点，alia对应子节点。
         */
        SimpleAliasRegistry registry = new SimpleAliasRegistry();

        registry.registerAlias("k1", "v1");
        registry.registerAlias("k1", "v3");
        registry.registerAlias("v1", "v2");

        // 内部会判断是否循环，即判断是否hasAlias("k1", "v2")
        // registry.registerAlias("v2", "k1");

        // 递归查找，通过v2作为子节点递归查找其父节点，看其所有的父节点中是否有k1
        System.out.println(registry.hasAlias("k1", "v2"));
        System.out.println(registry.hasAlias("v2", "k1"));

        // 递归查找，通过k1作为父节点递归查找他的子节点
        System.out.println(Arrays.toString(registry.getAliases("k1")));

        // 递归查找根节点，也就是他的真实的name
        System.out.println(registry.canonicalName("v2"));
    }
}
