local keys = KEYS
local value = ARGV[1]

for i=1, #keys do
	local actualValue = redis.call('get', keys[i])

	if not actualValue then
		return -1;
	end

	if actualValue ~= value then
		return 0;
	end

end

return 1;