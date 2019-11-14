select * from(
	SELECT
		c.id,
		c.name
	FROM t_course c
	WHERE c.professional_id = #{data.id} and valid_flag = '0'
) tab