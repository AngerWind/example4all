package com.tiger.cglib;

import com.tiger.cglib.dao.Dao;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title MethodInterceptorTest
 * @date 2021/9/15 14:27
 * @description
 */
public class MethodInterceptorTest {

    /**
     * MethodInterceptor能够代理Object中的toString, hashCode, equals, finalize, clone
     * 以及所有的非final方法。
     */
    MethodInterceptor methodInterceptor = new MethodInterceptor() {
        /*
        object: 代理对象
        method: 代理后的方法
        args: 参数
        methodProxy: 可用于调用代理后的方法和原始方法
         */
        @Override
        public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            System.out.println("Before Method Invoke");
            // 调用原始方法
            Object result = methodProxy.invokeSuper(object, args);
            System.out.println("After Method Invoke");
            // 以下两种方法都将调用被代理后的方法， 导致递归调用， 抛出异常
            // method.invoke(object, method);
            // method.invoke(object, args);
            return result;
        }
    };

    /**
     * 与CallbackHelper使用， 指定拦截的方法名。
     */
    CallbackHelper callbackHelper = new CallbackHelper(Dao.class, new Class[]{}) {
        @Override
        protected Object getCallback(Method method) {
            if ("update".equals(method.getName())) {
                return methodInterceptor;
            }
            return NoOp.INSTANCE;
        }
    };


     static {
        // 该设置用于输出cglib动态代理产生的类
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:\\Users\\Tiger.Shen\\Desktop\\example4all\\springtest");
        // 该设置用于输出jdk动态代理产生的类
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
    }

    @Test
    public void test() {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Dao.class);
        enhancer.setCallback(methodInterceptor);
        Dao dao = (Dao)enhancer.create();
        dao.update();
    }

//    @Test
//    public void test7(){
//
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(Dao.class);
//        enhancer.setCallbackFilter(callbackHelper);
//        enhancer.setCallbacks(callbackHelper.getCallbacks());
//        Dao dao = (Dao)enhancer.create();
//        dao.update();
//    }
}
