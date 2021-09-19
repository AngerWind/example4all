package com.tiger.cglib;

import com.tiger.cglib.dao.Dao;
import com.tiger.cglib.dao.DaoImpl;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;
import org.junit.Test;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title LazyLoaderTest
 * @date 2021/9/19 21:12
 * @description
 */
public class LazyLoaderTest {

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
        enhancer.setCallback(lazyLoader);
        Dao dao = (Dao)enhancer.create();
        dao.update();
    }
}
