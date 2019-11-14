select * from(
	SELECT 
		id as value,
		name as text
	FROM t_professional where valid_flag = '0'
) tab