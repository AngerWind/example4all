package com.tiger.spring_sub_test.bean_factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.FactoryBeanRegistrySupport;
import org.springframework.stereotype.Component;

/**
 * @see FactoryBeanRegistrySupport
 */
@Component
public class FactoryBeanTest implements FactoryBean<String> {

    @Override
    public String getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
