select * from(
	SELECT
		c.id as id,
		c.name as schoolName
	FROM t_school c
	where c.valid_flag = '0' and FIND_IN_SET(c.region_id, #{data.id})
) tab
