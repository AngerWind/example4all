package com.tiger.spring_sub_test;

import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.Bootstrapper;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SubTestBootstrap
 * @date 2021/11/26 17:01
 * @description
 */
public class SubTestBootstrap implements Bootstrapper {
    @Override
    public void intitialize(BootstrapRegistry registry) {
        System.out.println("bootstrap");
    }
}
