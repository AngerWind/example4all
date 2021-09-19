package com.tiger.cglib;

import com.tiger.cglib.dao.Dao;
import com.tiger.cglib.dao.DaoImpl;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;
import net.sf.cglib.proxy.ProxyRefDispatcher;
import org.junit.Test;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title ProxyRefDispatcherTest
 * @date 2021/9/19 21:16
 * @description
 */
public class ProxyRefDispatcherTest {

    /**
     * 与Dispatcher相同，只是多了一个额外的参数。
     */
    ProxyRefDispatcher proxyRefDispatcher = new ProxyRefDispatcher() {
        @Override
        public Object loadObject(Object proxy) throws Exception {
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
        enhancer.setCallback(proxyRefDispatcher);
        Dao dao = (Dao)enhancer.create();
        dao.update();
    }
}
