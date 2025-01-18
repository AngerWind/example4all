package com.gitlab.techschool.pcbook.service.rate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryRatingStore implements RatingStore {
    // key是laptop id
    // value是laptop的打分次数和总分
    private ConcurrentMap<String, Rating> data;

    public InMemoryRatingStore() {
        data = new ConcurrentHashMap<>();
    }

    @Override
    public Rating add(String laptopID, double score) {
        // 必须是线程安全的, 因为有可能多个客户端同时调用
        return data.merge(laptopID, new Rating(1, score), Rating::add);
    }
}
