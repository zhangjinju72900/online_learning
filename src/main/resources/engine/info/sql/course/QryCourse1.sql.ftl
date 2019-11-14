select * from(
	SELECT
		id,
		name
		FROM t_course 
	WHERE valid_flag = 0 and professional_id = #{data.professionalId}
) tab