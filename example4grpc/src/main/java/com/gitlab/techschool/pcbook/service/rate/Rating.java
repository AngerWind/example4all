package com.gitlab.techschool.pcbook.service.rate;

public class Rating {
    private final int count;
    private final double sum;

    // 用于保存laptop 的评分次数, 总分
    public Rating(int count, double sum) {
        this.count = count;
        this.sum = sum;
    }

    public int getCount() {
        return count;
    }

    public double getSum() {
        return sum;
    }

    public static Rating add(Rating r1, Rating r2) {
        return new Rating(r1.count + r2.count, r1.sum + r2.sum);
    }
}
