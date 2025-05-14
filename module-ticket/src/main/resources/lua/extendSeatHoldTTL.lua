local keys = KEYS
local ttl = tonumber(ARGV[1])
local memberId = ARGV[2]

for i = 1, #keys do
    if redis.call("EXISTS", keys[i]) == 1 then
        redis.call("PEXPIRE", keys[i], ttl)
    else
        redis.call("SET", keys[i], memberId, "PX", ttl)
    end
end

return 1