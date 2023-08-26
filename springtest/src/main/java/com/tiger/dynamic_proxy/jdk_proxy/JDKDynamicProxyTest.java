package com.tiger.dynamic_proxy.jdk_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JDKDynamicProxyTest
 * @date 2021/7/21 10:02
 * @description
 */
public class JDKDynamicProxyTest {

    public interface UserService{
        Number add(String name);
    }

    public interface UserService1{
        Integer add(String name);
    }

    public static class UserServiceImpl implements UserService, UserService1{

        @Override
        public Integer add(String name) {
            System.out.println("do something");
            return null;
        }
    }

    public static class MyInvocationHandler implements InvocationHandler{

        private Object target;

        public MyInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                System.out.println("invoke被调用");
                method.invoke(target, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void main(String[] args) {
        // 该设置用于输出jdk动态代理产生的类
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        UserService instance = (UserService)Proxy.newProxyInstance(UserService.class.getClassLoader(),
                new Class[]{UserService.class, UserService1.class},
                new MyInvocationHandler(new UserServiceImpl()));
        instance.add("zhangssan");

    }
}
