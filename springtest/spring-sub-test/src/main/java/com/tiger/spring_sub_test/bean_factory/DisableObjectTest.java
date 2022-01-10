package com.tiger.spring_sub_test.bean_factory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * @see org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
 */

@Component
public class DisableObjectTest implements DisposableBean {
    @Override
    public void destroy() throws Exception {

    }
}
