select * from(
	SELECT
		c.id,
		c.name as courseName,
		c.create_time as createTime,
		c.create_by as createBy,
		c.update_time as updateTime,
		c.update_by as updateBy,
		p.name as professionalName,
		t.name as curriculumName,
		t.execute_time as executeTime,
		cla.name as className,
		cla.grade as gradeName
	FROM t_course c
	LEFT JOIN t_professional p
	ON c.professional_id = p.id
	LEFT JOIN t_curriculum_course curr
	ON c.id = curr.course_id
	LEFT JOIN t_curriculum t
	ON curr.school_id = t.school_id
	LEFT JOIN t_class cla
	ON t.school_id = cla.school_id
) tab