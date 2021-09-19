package com.tiger.cglib;

import com.tiger.cglib.dao.Dao;
import com.tiger.cglib.dao.DaoImpl;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.LazyLoader;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title InvocationHandlerTest
 * @date 2021/9/19 21:17
 * @description
 */
public class InvocationHandlerTest {

    InvocationHandler invocationHandler = new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return new Object();
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
        enhancer.setCallback(invocationHandler);
        Dao dao = (Dao)enhancer.create();
        dao.update();
    }
}
