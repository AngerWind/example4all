package com.tiger.reflect;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.core.ResolvableType;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tiger.shen
 * @version v1.0
 * @date 2021/12/27 11:36
 */
public class TypeTest {

    @Test
    @SneakyThrows
    public void classes(){
        Type type4 = Class4.class.getGenericSuperclass();
        System.out.println(type4);  // Type的具体类型是Class， Class3

        ParameterizedType type3 = (ParameterizedType)Class3.class.getGenericSuperclass();
        Class<String> string = (Class<String>)(type3.getActualTypeArguments()[0]); // 返回泛型参数
        Class<Class2> rawType = (Class<Class2>)type3.getRawType(); // 返回ParameterizedType表示的原始类型
        Class<TypeTest> ownerType = (Class<TypeTest>)type3.getOwnerType(); // 返回ParameterizedType表示的类是在哪个类中定义的，这里是TypeTest，如果不是内部类（顶层类），返回null


        ParameterizedType type2 = (ParameterizedType)Class2.class.getGenericSuperclass();
        TypeVariable typeVariable = (TypeVariable)(type2.getActualTypeArguments()[0]);// 返回泛型参数，这里是E
        Type[] bounds = typeVariable.getBounds(); // 返回E的上界，这里是Object.class, 如果是E extends String，那么返回String.class

        // ResolvableType[] generics = resolvableType.getGenerics();

        System.out.println("1");
    }

    public abstract class Class1<E> { }

    public abstract class Class2<E> extends Class1<E> { }

    public abstract class Class3 extends Class2<String> { }

    public abstract class Class4 extends Class3 { }




}
