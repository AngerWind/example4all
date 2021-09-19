package com.tiger.cglib;

import com.tiger.cglib.dao.Dao;
import com.tiger.cglib.dao.DaoImpl;
import lombok.SneakyThrows;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.CallbackHelper;
import net.sf.cglib.proxy.Dispatcher;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.LazyLoader;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import net.sf.cglib.proxy.ProxyRefDispatcher;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title EverythingTest
 * @date 2021/7/23 10:14
 * @description
 */
public class EverythingTest {

    @Before
    public void before(){
        // 该设置用于输出cglib动态代理产生的类
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:\\Users\\Tiger.Shen\\Desktop\\example4all\\springtest");
        // 该设置用于输出jdk动态代理产生的类
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
    }



    @SneakyThrows
    @Test
    public void test2() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Dao.class);
        // enhancer.setCallbackTypes();
        Class enhancerClass = enhancer.createClass();
        Constructor constructor = enhancerClass.getConstructor();
        Object o = constructor.newInstance();
    }

    @SneakyThrows
    @Test
    public void test3() {





        /**
         * 空操作
         */
        NoOp noOp = NoOp.INSTANCE;


        /**
         * 根据Method返回一个索引， 该索引从0开始， 对应setCallbacks()的参数。
         * 根据返回的索引指示该Method应该被哪个Callback增强
         */
        CallbackFilter callbackFilter = new CallbackFilter() {
            @Override
            public int accept(Method method) {
                return 6;
            }
        };
        /**
         * CallbackFilter的子类
         * 根据method返回增强该方法的Callback
         */
        CallbackHelper callbackHelper = new CallbackHelper(Dao.class, new Class[0]) {
            @Override
            protected Object getCallback(Method method) {
                return NoOp.INSTANCE;
            }
        };

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Dao.class);
        enhancer.setCallbacks(new Callback[]{noOp});
        enhancer.setCallbackFilter(callbackFilter);
        enhancer.setSerialVersionUID(34108341341093L);
        enhancer.setInterceptDuringConstruction(false);
        Dao o = (Dao)enhancer.create();
        o.update();
        o.update();

    }


}
