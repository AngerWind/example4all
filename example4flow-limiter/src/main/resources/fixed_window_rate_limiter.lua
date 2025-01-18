
-- KEYS[1]: 限流 key
-- ARGV[1]: 阈值
-- ARGV[2]: 过期时间


local count = redis.call("incr",KEYS[1])
if count == 1 then
  -- count为1, 说明key是新插入的, 所以设置过期时间
  redis.call('expire',KEYS[1],ARGV[2])
end
if count > tonumber(ARGV[1]) then
  return 0
end
return 1
