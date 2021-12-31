package com.tiger.spring_sub_test.convert;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;

import java.util.*;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title TypeDescriptorTest
 * @date 2021/12/29 14:04
 * @description
 */
public class TypeDescriptorTest {

    public class Three<K,V,F> {}

    public interface Two<K,V> {}

    public class MySimple extends Three<List<String>, Map<String, Number>, Integer[]>
        implements Two<ArrayList<Double>, AbstractMap<Long, Character>> {}

    public MySimple mySimple = new MySimple();

    @Test
    @SneakyThrows
    public void test(){
        TypeDescriptor typeDescriptor = TypeDescriptor.valueOf(MySimple.class);
        TypeDescriptor object = TypeDescriptor.forObject(mySimple);
        ResolvableType resolvableType = typeDescriptor.getResolvableType(); // 获取ResolvableType
        typeDescriptor.getType(); // 获取具体的Type类型
        typeDescriptor.getObjectType(); // 同getType，但是会把基础类型转为包装类型

        TypeDescriptor abstractList = TypeDescriptor.collection(AbstractList.class, TypeDescriptor.valueOf(String.class)); //AbstractList<String>
        TypeDescriptor list = abstractList.upcast(List.class); // 向上强转为List<String>
        TypeDescriptor arrayList = abstractList.narrow(Lists.newArrayList()); // 向下强转为ArrayList<String>
        abstractList.getElementTypeDescriptor(); // 获取集合的元素类型

        TypeDescriptor map =
            TypeDescriptor.map(Map.class, TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(Integer.class));
        map.isMap();
        map.getMapKeyTypeDescriptor();
        map.getMapValueTypeDescriptor();

        TypeDescriptor array = TypeDescriptor.array(TypeDescriptor.valueOf(String.class));
        array.getElementTypeDescriptor(); //获取数组元素的类型
    }

}
