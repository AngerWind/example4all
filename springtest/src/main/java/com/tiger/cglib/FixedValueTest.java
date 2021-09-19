package com.tiger.cglib;

import com.tiger.cglib.dao.Dao;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.*;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title FixedValueTest
 * @date 2021/9/19 20:54
 * @description
 */
public class FixedValueTest {

    static {
        // 该设置用于输出cglib动态代理产生的类
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:\\Users\\Tiger.Shen\\Desktop\\example4all\\springtest");
        // 该设置用于输出jdk动态代理产生的类
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
    }

    /**
     * 返回固定的值
     * 调用代理类的增强方法，将直接转变为调用 return callback.loadObject()
     * 高度定制
     */
    FixedValue fixedValue = new FixedValue() {
        @Override
        public Object loadObject() throws Exception {
            // 返回值类型一定要与原始方法兼容， 否则会强转失败
            // 当MethodInterceptor代理多个方法时， 需要根据代理方法返回的类型进行相应的判断
            return "this is fixedValue";
        }
    };

    @Test
    public void test7(){

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Dao.class);
        enhancer.setCallback(fixedValue);
        Dao dao = (Dao)enhancer.create();
        dao.update();
    }
}
