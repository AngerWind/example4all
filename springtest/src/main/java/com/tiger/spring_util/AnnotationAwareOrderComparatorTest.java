package com.tiger.spring_util;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title AnnotationAwareOrderComparatorTest
 * @date 2021/11/29 10:21
 * @description
 */
public class AnnotationAwareOrderComparatorTest {

    @Test
    @SneakyThrows
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");

        /**
         * 通过Order，PriorityOrdered接口， @Order，@Priority接口排序
         * AnnotationAwareOrderComparator继承了OrderComparator，并重载了findOrder方法
         * 该方法在OrderComparator中的实现只能从Order和Priority接口中获取值
         * 重载后使用MergedAnnotations这个工具类获取了@Order和@Priority接口中的值
         *
         * @see MergedAnnotationsTest
         */
        AnnotationAwareOrderComparator.sort(list);
    }
}
