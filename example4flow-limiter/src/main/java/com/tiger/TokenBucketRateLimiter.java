package com.tiger;

public class TokenBucketRateLimiter {

    private final long capacity; // 令牌桶的容量「限流器允许的最大突发流量」

    private final long generatedPerSeconds; //令牌发放速率, 每秒

    long lastTokenTime = System.currentTimeMillis(); // 上一次令牌发送的时间

    private long currentTokens; // 当前令牌数量

    public TokenBucketRateLimiter(long generatedPerSeconds, int capacity) {
        this.generatedPerSeconds = generatedPerSeconds;
        this.capacity = capacity;
    }


    public synchronized boolean tryAcquire() {
        /*
         * 计算令牌当前数量
         * 请求时间在最后令牌是产生时间相差大于等于额1s（为啥时1s？因为生成令牌的最小时间单位时s），则
         *  1. 重新计算令牌桶中的令牌数
         *  2. 将最后一个令牌发放时间重置为当前时间
         */
        long now = System.currentTimeMillis();
        if (now - lastTokenTime >= 1000) {
            long newPermits = (now - lastTokenTime) / 1000 * generatedPerSeconds; // 计算要发送的令牌
            currentTokens = Math.min(currentTokens + newPermits, capacity); // 令牌数量不能超过桶的容量
            lastTokenTime = now; // 记录本次发送令牌的时间
        }
        if (currentTokens > 0) {
            currentTokens--; // 令牌数减1
            return true;
        }
        return false;
    }
}
