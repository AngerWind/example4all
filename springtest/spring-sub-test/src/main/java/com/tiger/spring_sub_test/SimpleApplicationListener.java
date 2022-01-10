package com.tiger.spring_sub_test;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * 也可以实现下面两个类，这两个类功能是一样的，但是推荐使用第一个
 * @see org.springframework.context.event.GenericApplicationListener
 * @see org.springframework.context.event.SmartApplicationListener
 *
 * 通过上面两个的supportXXX方法来指定支持的Event类型
 * 要不然Spring会通过ApplicationListener上面的泛型来判断是否支持要发布的事件
 */
public class SimpleApplicationListener implements ApplicationListener<ApplicationEvent> {

    /**
     * @see org.springframework.boot.context.event.EventPublishingRunListener
     * 这个类会调用applicationListener发布生命周期的各种事件
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }
}
