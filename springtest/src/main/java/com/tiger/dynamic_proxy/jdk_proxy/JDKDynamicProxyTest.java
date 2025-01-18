package com.tiger.dynamic_proxy.jdk_proxy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.AccessException;

import javax.annotation.Nullable;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JDKDynamicProxyTest
 * @date 2021/7/21 10:02
 * @description
 */
public class JDKDynamicProxyTest {

    public interface UserService{
        Number publicMethod(String name);

        void exceptionMethod() throws FileNotFoundException, AccessException;

        // default 方法也会被代理
        default Number add2(String name) {
            return 1;
        }
    }

    public interface UserService1{
        // 生成的代理方法上面不会有这两个注解
        @Deprecated
        @Nullable
        Integer publicMethod(String name);
        void exceptionMethod() throws IOException;
    }

    public static class UserServiceImpl implements UserService, UserService1{

        @Override
        public Integer publicMethod(String name) {
            System.out.println("do something");
            return null;
        }

        @Override
        public void exceptionMethod() throws FileNotFoundException, AccessException {

        }
    }

    public static class MyInvocationHandler implements InvocationHandler{

        private Object target;

        public MyInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        // proxy是当前的proxy对象, method是当前proxy被调用的方法, 也就是代理方法
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                System.out.println("invoke被调用");
                // 如果这里调用method.invoke(proxy, args), 那么又会进入到invoke方法中, 无限循环
                method.invoke(target, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * jdk代理的原理就是将Object, 和各个接口的方法都解析出来, 获得所有方法, 并且获取这些方法的method对象
     * 并且在代理类中, 为method生成对应的代理method
     * 调用代理method的时候, 就调用Handler, 并传入原生method, 这样handler就知道哪个原生method被调用了
     */
    public static void main(String[] args) {
        // 该设置用于输出jdk动态代理产生的类
        // jdk11使用 jdk.proxy.ProxyGenerator.saveGeneratedFiles
        // jdk8使用 sun.misc.ProxyGenerator.saveGeneratedFiles
        // 创建的文件会在当前项目的根目录下
        System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
        UserService instance = (UserService)Proxy.newProxyInstance(UserService.class.getClassLoader(),
                new Class[]{UserService.class, UserService1.class},
                new MyInvocationHandler(new UserServiceImpl()));
        instance.publicMethod("zhangssan");

        // 下面的代码仅用于编写注释, 实际上并不能运行, 可以对照源代码来看写的注释
        // UserService instance1 = (UserService)com.tiger.dynamic_proxy.jdk_proxy.Proxy.newProxyInstance(UserService.class.getClassLoader(),
        //         new Class[]{UserService.class, UserService1.class},
        //         new MyInvocationHandler(new UserServiceImpl()));
        // instance.add("zhangssan");

    }
}
