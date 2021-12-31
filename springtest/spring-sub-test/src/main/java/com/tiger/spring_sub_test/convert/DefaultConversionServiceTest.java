package com.tiger.spring_sub_test.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title DefaultConverterServiceTest
 * @date 2021/12/24 18:23
 * @description
 */
public class DefaultConversionServiceTest {

    @Test
    @SneakyThrows
    public void test(){
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new SimpleConverter());
        // Car integer = conversionService.convert("{\"name\":\"zhangsna\",\"age\":16}", Car.class);
        // System.out.println(integer);

        List<String> stringList = Lists.newArrayList("1", "2", "3");
        Set<Integer> convert =
            (Set<Integer>)conversionService.convert(stringList, TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)),
                TypeDescriptor.valueOf(Set.class));
        System.out.println(convert);

        ResolvableType resolvableType = ResolvableType.forClass(List.class);

    }

    @Data
    public static class Car {
        String name;
        Integer age;
    }

    @Test
    @SneakyThrows
    public void test1(){
        Car car = new Car();
        car.setAge(16);
        car.setName("zhangsna");
        new ObjectMapper().writeValueAsString(car);
    }

    public class SimpleConverter implements Converter<List<Integer>, Set<String>> {

        @Override
        public Set<String> convert(List<Integer> source) {
            return source.stream().map(Object::toString).collect(Collectors.toSet());
        }
    }

    @Test
    @SneakyThrows
    public void test3(){
        Set<String> set = new HashSet<>();
        Collection stringList = Lists.newArrayList("1", "2", "3");
        set.addAll(stringList);
    }


}
