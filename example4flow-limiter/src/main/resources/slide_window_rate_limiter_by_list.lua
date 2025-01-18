--[[
基本原理：使用一个list来保存同一个key的调用时间，每次成功调用就将调用时间lpush到list里面，list[times-1]就是最老的一次调用的时间
假如限制同一个key在5000毫秒内调用5次
则在每次同一个key调用时，获取list[4]
如果list[4]为nil，说明调用未满5次，可以成功调用。同时将当前的时间lpush到list里面。
如果list[4]不为nil，那就用当前时间减去list[4]，如果该值大于5000毫秒，说明调用已经超过了限制时间，可以调用成功。同时将当前的时间lpush到list里面。
如果该值小于5000毫秒，说明在5000毫秒内已经调用5次了，不能调用成功。

最后每次调用还需要清理list的长度。防止无限增长
]]

--返回的变量
local result = {}

local key = KEYS[1]
local expired = ARGV[1]
local times = ARGV[2]
local current = ARGV[3]

--获取阈值次数的时间
local earliestTime = redis.call('lindex', key, tonumber(times) - 1)
--比较时间
if (earliestTime == false) or (tonumber(current) - tonumber(earliestTime) > tonumber(expired)) then
    redis.call('lpush', key, current)
    redis.call('expire', key, expired)
    result[1] = 1
else
    result[1] = 0
    -- 返回需要等待的时间
    result[2] = tonumber(expired) - (tonumber(current) - tonumber(earliestTime))
end
--清理队列
if (earliestTime ~= false) then
    redis.call('ltrim', KEYS[1], 0, tonumber(ARGV[2]) * 3)
end
return result
