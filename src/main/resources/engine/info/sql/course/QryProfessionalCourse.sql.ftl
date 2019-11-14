select * from(
	SELECT 
		c.id,
		c.name as courseName,
		p.name as professionalName,
		c.create_time as createTime,
		c.create_by as createBy,
		c.update_time as updateTime,
		c.update_by as updateBy
	FROM t_course c
	LEFT JOIN t_professional p
	ON c.professional_id = p.id
) tab