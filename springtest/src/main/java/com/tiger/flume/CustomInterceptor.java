package com.tiger.flume;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;
import java.util.Map;

public class CustomInterceptor implements Interceptor {

    @Override
    public void initialize() {

    }

    // 单个事件拦截
    @Override
    public Event intercept(Event event) {
        // 1.获取事件中的头信息
        Map<String, String> headers = event.getHeaders();
        // 2.获取事件中的 body 信息
        String body = new String(event.getBody());
        // 3.根据 body 中是否有"atguigu"来决定添加怎样的头信息
        if (body.contains("atguigu")) {
            // 4.添加头信息
            headers.put("type", "first");
        } else {
            // 4.添加头信息
            headers.put("type", "second");
        }
        return event;
    }

    // 批量事件拦截
    @Override
    public List<Event> intercept(List<Event> events) {
        events.forEach(this::intercept);
        return events;
    }

    @Override
    public void close() {}

    public static class Builder implements Interceptor.Builder {
        @Override
        public Interceptor build() {
            return new CustomInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
