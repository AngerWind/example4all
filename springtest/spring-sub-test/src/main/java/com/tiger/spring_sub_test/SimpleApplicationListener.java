package com.tiger.spring_sub_test;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SimpleApplicationListener
 * @date 2021/12/24 13:51
 * @description
 */
public class SimpleApplicationListener implements ApplicationListener {

    /**
     * @see org.springframework.boot.context.event.EventPublishingRunListener
     * 这个类会调用applicationListener发布生命周期的各种事件
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }
}
