select * from(
	SELECT 
		c.id,
		c.name as name
	FROM t_course c
	LEFT JOIN t_professional p
	ON c.professional_id = p.id
	WHERE c.professional_id = #{data.id}
) tab