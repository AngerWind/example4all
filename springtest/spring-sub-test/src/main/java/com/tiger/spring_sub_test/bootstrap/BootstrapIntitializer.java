package com.tiger.spring_sub_test.bootstrap;

import org.springframework.boot.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SubTestBootstrap
 * @date 2021/11/26 17:01
 * @description
 */
public class BootstrapIntitializer implements Bootstrapper {
    @Override
    public void intitialize(BootstrapRegistry registry) {
        registry.addCloseListener(new BootstrapCloseListener());
    }

    /**
     * @see SpringApplication#prepareContext(DefaultBootstrapContext, ConfigurableApplicationContext, ConfigurableEnvironment, SpringApplicationRunListeners, ApplicationArguments, Banner)
     * 392
     */
    public static class BootstrapCloseListener implements  ApplicationListener<BootstrapContextClosedEvent>  {

        @Override
        public void onApplicationEvent(BootstrapContextClosedEvent event) {

        }
    }
}
