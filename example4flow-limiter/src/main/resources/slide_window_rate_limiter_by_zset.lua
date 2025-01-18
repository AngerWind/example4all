--KEYS[1]: 限流 key
--ARGV[1]: 当前时间窗口的开始时间戳
--ARGV[2]: 当前时间戳
--ARGV[3]: 阈值
--ARGV[4]: 当前时间戳对应的score, 就是当前时间戳
-- 1. 移除时间窗口之前的数据
redis.call('zremrangeByScore', KEYS[1], 0, ARGV[1])
-- 2. 统计当前元素数量
local res = redis.call('zcard', KEYS[1])
-- 3. 是否超过阈值
if (res == nil) or (res < tonumber(ARGV[3])) then
    redis.call('zadd', KEYS[1], ARGV[2], ARGV[2])
    return 1
else
    return 0
end
