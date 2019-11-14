select * from (
	SELECT 
		id,
		name
		FROM t_school 
	WHERE valid_flag = 0 and region_id = #{data.regionId} and 2!=#{data.session.userInfo.userId}
union ALL
SELECT 
		id,
		name
		FROM t_school 
	WHERE valid_flag = 0  and 2=#{data.session.userInfo.userId}
) tab
