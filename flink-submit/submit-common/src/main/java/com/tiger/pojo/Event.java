package com.tiger.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    /**
     * flink对pojo类型的要求:
     * 1. 所有公开的属性都可以序列化
     * 2. 需要有一个空参的构造函数
     */
    private String user;
    private String url;
    private Long timestamp;

    @Override
    public String toString() { return "Event{" +
            "user='" + user + '\'' +
            ", url='" + url + '\'' +
            ", timestamp=" + new Timestamp(timestamp) + '}';
    }
}

