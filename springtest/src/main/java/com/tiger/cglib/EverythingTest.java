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
    public void test1() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Dao.class);
        enhancer.setCallback(new FixedValue() {
            @Override
            public Object loadObject() throws Exception {
                return "hello world";
            }
        });
        Dao dao = (Dao)enhancer.create();
        dao.update();
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
         * 返回固定的值
         * 调用代理类的增强方法，将直接转变为调用loadObject()方法并返回
         * 高度定制
         */
        FixedValue fixedValue = new FixedValue() {
            @Override
            public Object loadObject() throws Exception {
                return "this is fixedValue";
            }
        };
        MethodInterceptor methodInterceptor = new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return new Object();
            }
        };
        /**
         * Dispatches方法将在任何增强方法调用前被调用
         * 该对象将取代代理类作为实际的方法调用类
         * 所以该类必须是superClass和superInterface的子类
         * 否则将类型转换失败
         *
         * 一下代码每次调用都返回一个新的DaoImpl
         * 所以每次调用增强方法都是新的DaoImpl
         */
        Dispatcher dispatcher = new Dispatcher() {
            @Override
            public Object loadObject() throws Exception {
                return new DaoImpl();
            }
        };
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return new Object();
            }
        };
        /**
         * 与Dispatcher相同， 但是LazyLoader返回的对象将被代理类保存起来
         * 该对象将取代代理类作为实际的方法调用类
         * 所以该类必须是superClass和superInterface的子类
         * 否则将类型转换失败
         */
        LazyLoader lazyLoader = new LazyLoader() {
            @Override
            public Object loadObject() throws Exception {
                return new DaoImpl();
            }
        };
        /**
         * 空操作
         */
        NoOp noOp = NoOp.INSTANCE;

        /**
         * 与Dispatcher相同，只是多了一个额外的参数。
         */
        ProxyRefDispatcher proxyRefDispatcher = new ProxyRefDispatcher() {
            @Override
            public Object loadObject(Object proxy) throws Exception {
                return new DaoImpl();
            }
        };
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
        enhancer.setCallbacks(new Callback[]{dispatcher, fixedValue,
                invocationHandler, lazyLoader, methodInterceptor, noOp, proxyRefDispatcher});
        enhancer.setCallbackFilter(callbackFilter);
        enhancer.setSerialVersionUID(34108341341093L);
        enhancer.setInterceptDuringConstruction(false);
        Dao o = (Dao)enhancer.create();
        o.update();
        o.update();

    }


}
