package com.tiger.spring_util;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.OrderUtils;

import java.lang.annotation.Retention;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title MergedAnnotationsTest
 * @date 2021/11/29 10:31
 * @description
 */
@Order(2)
public class MergedAnnotationsTest {

    @Test
    @SneakyThrows
    public void test(){

        // MergedAnnotations mergedAnnotations =
        //     MergedAnnotations.from(MergedAnnotationsTest.class, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);

        Integer order = OrderUtils.getOrder(MergedAnnotationsTest.class);
    }

    @Test
    @SneakyThrows
    public void test1(){
        MergedAnnotations mergedAnnotations =
            MergedAnnotations.from(MergedAnnotationsTest.class, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);
        MergedAnnotation<Order> mergedAnnotation = mergedAnnotations.get(Order.class);
        if (mergedAnnotation.isPresent()) {
            int value = mergedAnnotation.getInt("value");
        }
    }
}
