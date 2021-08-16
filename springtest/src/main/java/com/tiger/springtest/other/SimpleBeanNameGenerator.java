package com.tiger.springtest.other;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SimpleBeanNameGenerator
 * @date 2020/12/11 10:54
 * @description
 */
public class SimpleBeanNameGenerator implements BeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanClassName = definition.getBeanClassName();
        return "hello-" + beanClassName;
    }

    public static void main(String[] args) {

    }
}
