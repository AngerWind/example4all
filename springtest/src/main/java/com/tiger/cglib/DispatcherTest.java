package com.tiger.cglib;

import com.tiger.cglib.dao.Dao;
import com.tiger.cglib.dao.DaoImpl;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Dispatcher;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import org.junit.Test;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title DispatcherTest
 * @date 2021/9/19 21:05
 * @description
 */
public class DispatcherTest {

    static {
        // 该设置用于输出cglib动态代理产生的类
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:\\Users\\Tiger.Shen\\Desktop\\example4all\\springtest");
        // 该设置用于输出jdk动态代理产生的类
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
    }

    /**
     * Dispatches方法将在任何增强方法调用前被调用
     * 该对象将取代代理类作为实际的方法调用类
     *
     * return ((Dao)var10000.loadObject()).update();
     *
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

    @Test
    public void test7(){

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Dao.class);
        enhancer.setCallback(dispatcher);
        Dao dao = (Dao)enhancer.create();
        dao.update();
    }
}
