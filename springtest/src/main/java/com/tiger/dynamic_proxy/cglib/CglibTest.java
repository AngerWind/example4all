package com.tiger.dynamic_proxy.cglib;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.tiger.dynamic_proxy.cglib.dao.BaseDao;
import com.tiger.dynamic_proxy.cglib.dao.BaseDaoImpl;
import com.tiger.dynamic_proxy.cglib.dao.StudentMapper;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.*;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title DispatcherTest
 * @date 2021/9/19 21:05
 * @description
 */
public class CglibTest {

    @Before
    public void before() {
        // 该设置用于输出cglib动态代理产生的类
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, ".\\cglib");
        // 该设置用于输出jdk动态代理产生的类
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
    }

    @Test
    public void invocationHandler() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BaseDao.class);
        enhancer.setInterfaces(new Class[]{StudentMapper.class});
        // 设置生成的代理类是否实现Factory接口
        enhancer.setUseFactory(true);
        // 设置生成的代理类中添加uid
        enhancer.setSerialVersionUID(1L);
        // 设置在构造函数中调用的当前类的方法是否进行拦截
        enhancer.setInterceptDuringConstruction(false);
        enhancer.setCallback(new InvocationHandler() {
            /*
             * update方法伪代码如下: 
             * public String update() { 
             *     return (String) invocationHandler.invoke(this, method, args); 
             * }
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("hello world");
                return method.invoke(new BaseDaoImpl(), args);
            }
        });
        BaseDao baseDao = (BaseDao)enhancer.create();
        baseDao.update();

        Factory baseDaoFactory = (Factory) baseDao;
        BaseDao baseDao1 = (BaseDao)baseDaoFactory.newInstance(new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                System.out.println(111);
                return method.invoke(new BaseDaoImpl());
            }
        });
        baseDao1.update();

    }

    @Test
    public void methodInvocation() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BaseDao.class);
        enhancer.setInterfaces(new Class[]{StudentMapper.class});
        enhancer.setCallback(new MethodInterceptor() {
            /**
             * 与InvocationHandler类似, 只是多接收了一个MethodProxy参数
             * methodProxy: 可用于调用代理后的方法和原始方法
             */
            @Override
            public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy)
                throws Throwable {
                System.out.println("Before Method Invoke");
                // 调用原始方法
                Object result = method.invoke(new BaseDaoImpl(), args);

                System.out.println("After Method Invoke");

                // 以下方法将导致递归调用， 抛出异常
                // method.invoke(object, args);

                // 返回值类型一定要与原始方法兼容， 否则会强转失败
                // 当MethodInterceptor代理多个方法时， 需要根据代理方法返回的类型进行相应的判断
                return result;
            }
        });
        BaseDao baseDao = (BaseDao)enhancer.create();
        baseDao.update();
    }

    @Test
    public void dispatcher() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BaseDao.class);
        enhancer.setInterfaces(new Class[]{StudentMapper.class});
        enhancer.setCallback(new Dispatcher() {
            /*
             * 代理类的update()伪代码如下:
             * public String update() {
             *    return ((Dao)dispatcher.loadObject()).update();
             * }
             * 
             * 1. 调用代理类的任何方法, 都会转而调用Dispatcher.loadObject()的对应方法
             * 2. Dispatcher.loadObject()返回的对象必须是superClass和superInterface的子类 否则将类型转换失败
             * 3. 代码每次调用都返回一个新的DaoImpl, 所以每次调用增强方法都是新的DaoImpl
             */
            @Override
            public Object loadObject() throws Exception {
                return new BaseDaoImpl();
            }
        });
        BaseDao baseDao = (BaseDao)enhancer.create();
        baseDao.update();
    }

    @Test
    public void lazyLoader() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BaseDao.class);
        enhancer.setCallback(new LazyLoader() {
            /*
             * update方法伪代码如下:
             * 
             * Object var10000;
             * public String update () {
             *      if (var10000 == null) {
             *          var10000 = lazyLoader.loadObject();
             *      }
             *      return (Dao)var10000.update();
             * }
             * 
             * 与Dispatcher类似, 只是LazyLoader会把loadObject()返回的对象缓存起来
             */
            @Override
            public Object loadObject() throws Exception {
                return new BaseDaoImpl();
            }
        });
        BaseDao baseDao = (BaseDao)enhancer.create();
        baseDao.update();
    }

    @Test
    public void proxyRefDispatcher() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BaseDao.class);
        enhancer.setCallback(new ProxyRefDispatcher() {
            /*
             * 代理类的update()伪代码如下:
             * public String update() {
             *      return ((Dao)proxyRefDispatcher.loadObject(this)).update();
             * }
             * 
             * 与Dispatcher类似, 只是loadObject方法会收到代理类的this引用
             */
            @Override
            public Object loadObject(Object proxy) throws Exception {
                return new BaseDaoImpl();
            }
        });
        BaseDao baseDao = (BaseDao)enhancer.create();
        baseDao.update();
    }

    @Test
    public void noOp() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BaseDao.class);
        // 不对Dao进行代理, 生成的dao对象就和new Dao() 一样
        enhancer.setCallback(NoOp.INSTANCE);
        BaseDao baseDao = (BaseDao)enhancer.create();
        baseDao.update();
    }

    @Test
    public void fixedValue() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BaseDao.class);
        enhancer.setCallback(new FixedValue() {
            /*
             * update方法伪代码如下:
             * public String update() {
             *     return fixedValue.loadObject();
             * }
             */
            @Override
            public Object loadObject() throws Exception {
                // 返回值类型一定要与原始方法兼容， 否则会强转失败
                // 当MethodInterceptor代理多个方法时， 需要根据代理方法返回的类型进行相应的判断
                return "this is fixedValue";
            }
        });
        BaseDao baseDao = (BaseDao)enhancer.create();
        System.out.println(baseDao.update());
    }

    @Test
    public void setCallbacks() {
        /*
         * setCallbacks()方法必须和setCallbackFilter()方法同时使用!!!!!!!!!!!
         * setCallbacks()为代理类设置多个callbacks对象
         * CallbackFilter的accept()方法, 根据给定的Method, 返回method使用的callbacks的索引
         *
         * 这两个方法会影响字节码的生成, 并不是在运行时调用accept获取获取到索引, 然后调用对应的callback
         * 而是在生产字节码的时候, 就调用accept()获取对应的callback, 然后生成对应的字节码
         */
        Dispatcher dispatcher = new Dispatcher() {
            @Override
            public Object loadObject() throws Exception {
                return  new BaseDaoImpl();
            }
        };
        LazyLoader lazyLoader = BaseDaoImpl::new;
        NoOp noOp = NoOp.INSTANCE;
        Callback[] callbacks = {dispatcher, lazyLoader, noOp};
        CallbackFilter callbackFilter = new CallbackFilter() {
            @Override
            public int accept(Method method) {
                if ("update".equals(method.getName())) {
                    // update使用dispatcher来代理
                    return 0;
                } else if ("equals".equals(method.getName())) {
                    // equals使用lazyLoader来代理
                    return 1;
                }
                // 其他方法不进行代理
                return 2;
            }
        };

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BaseDao.class);
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackFilter(callbackFilter);
        BaseDao baseDao = (BaseDao)enhancer.create();
        baseDao.update();

        /*
         * 上面的方法使用起来比较麻烦, 要先生成callback数组,
         * 然后在accept()中设置方法和索引的映射,
         * 可以使用CallbackFilter的子类CallbackHelper
         * 直接根据method返回对应的callback
         */
        CallbackHelper callbackHelper = new CallbackHelper(BaseDao.class, new Class[] {}) {
            @Override
            protected Object getCallback(Method method) {
                if ("update".equals(method.getName())) {
                    return dispatcher;
                } else if ("equals".equals(method.getName())) {
                    return lazyLoader;
                }
                return noOp;
            }
        };
        Enhancer enhancer1 = new Enhancer();
        enhancer1.setSuperclass(BaseDao.class);
        enhancer1.setCallbackFilter(callbackHelper);
        enhancer1.setCallbacks(callbackHelper.getCallbacks());
        BaseDao baseDao1 = (BaseDao)enhancer.create();
        baseDao1.update();

    }

}
