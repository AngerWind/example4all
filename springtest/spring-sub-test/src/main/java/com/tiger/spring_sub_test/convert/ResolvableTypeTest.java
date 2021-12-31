package com.tiger.spring_sub_test.convert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title ResolvableTypeTest
 * @date 2021/12/28 18:01
 * @description
 */
public class ResolvableTypeTest {

    public class Three<K,V,F> {
        public Three(K k) { }
    }

    public interface Two<K,V> {}

    public class MySimple extends Three<List<String>, Map<String, Number>, Integer[]> implements Two<ArrayList<Double>, AbstractMap<Long, Character>>{
        public MySimple(){
            super(Lists.newArrayList());
        }
    }

    public MySimple mySimple = new MySimple();

    @Test
    @SneakyThrows
    public void test(){
        ResolvableType resolvableType =
            ResolvableType.forField(ReflectionUtils.findField(ResolvableTypeTest.class, "mySimple"));
        ResolvableType three = resolvableType.getSuperType();
        ResolvableType number = three.getGeneric(1, 1); // 等同于three.getGeneric(1).getGeneric(1)，下标从0开始。找不到返回NONE
        System.out.println(number);

        Class<?> resolve = number.resolve(); // 返回对应的class类，无法解析返回null
        System.out.println(resolve);

        Arrays.stream(three.getGeneric(1).getGenerics()).forEach(System.out::println); //获取Map上所有的泛型

        System.out.println(three.getGeneric(2).isArray()); // 判断当前代表的类型是否为数组
        System.out.println(three.getGeneric(2).getComponentType()); // 获得数组的元素类型

        three.getNested(2); //1表示他自己Three，2表示嵌套一层的最后一个泛型Integer[]，3表示嵌套2层的最后一个泛型Integer

        HashMap<Integer, Integer> map = Maps.newHashMap();
        map.put(2, 1); // 获取第二层的第2个泛型，Map<String, Number>
        map.put(3, 0); // 获取第三层的第1个泛型，String
        three.getNested(3, map); // 获取三层嵌套，第一层是他自己Three，第二层和第三层通过map查找，最后查找到String

        ResolvableType two = resolvableType.getInterfaces()[0];
        System.out.println(ResolvableType.forRawClass(two.getGeneric(0).resolve())); // rawClass, 同forClass但是消除了泛型


        System.out.println(two.getGeneric(0).as(AbstractList.class)); // as,类似于强转，将ArrayList<Double>转换为AbstractList<Double>

        System.out.println(two.resolveGeneric(1, 0)); // 同getGeneric(1，0).resolve();



    }

    @Test
    @SneakyThrows
    public void test1(){
        Constructor constructor = Three.class.getConstructors()[0];
        ResolvableType resolvableType = ResolvableType.forConstructorParameter(constructor, 0, MySimple.class);

        ResolvableType resolvableType1 = ResolvableType.forClass(Three.class, MySimple.class);
        ResolvableType[] generics = resolvableType.getGenerics();
    }
}
