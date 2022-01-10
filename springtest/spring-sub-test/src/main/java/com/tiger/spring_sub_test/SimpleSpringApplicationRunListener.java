package com.tiger.spring_sub_test;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;


public class SimpleSpringApplicationRunListener implements SpringApplicationRunListener {

    /**
     * 必须要是这两个参数类型的构造参数
     * @see org.springframework.boot.context.event.EventPublishingRunListener
     */
    public SimpleSpringApplicationRunListener(SpringApplication application, String[] args) {

    }


    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
    }


    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
    }


    @Override
    public void started(ConfigurableApplicationContext context) {
    }


    @Override
    public void running(ConfigurableApplicationContext context) {
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
    }
}
