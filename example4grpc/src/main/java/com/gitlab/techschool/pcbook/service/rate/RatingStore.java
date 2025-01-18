package com.gitlab.techschool.pcbook.service.rate;

public interface RatingStore {
    Rating add(String laptopID, double score);
}
