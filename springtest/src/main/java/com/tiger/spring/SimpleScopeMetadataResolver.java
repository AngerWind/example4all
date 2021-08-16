package com.tiger.spring;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SimpleScopeMetadataResolver
 * @date 2020/12/11 14:42
 * @description
 */
public class SimpleScopeMetadataResolver implements ScopeMetadataResolver, BeanNameAware {
    @Override
    public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
        String beanClassName = definition.getBeanClassName();
        if (beanClassName.endsWith(BeanDefinition.SCOPE_SINGLETON)) {
            return new ScopeMetadata();
        } else {
            ScopeMetadata prototypeMetadata = new ScopeMetadata();
            prototypeMetadata.setScopeName(BeanDefinition.SCOPE_PROTOTYPE);
            return prototypeMetadata;
        }
    }

    @Override
    public void setBeanName(String name) {
        System.out.println(name);
    }
}
