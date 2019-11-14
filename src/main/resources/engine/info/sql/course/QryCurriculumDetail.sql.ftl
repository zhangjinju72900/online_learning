select * from(
	SELECT
		curr.id,
		curr.name as curriculumName,
		curr.school_id as schoolId,
		curr.create_time as createTime,
		curr.execute_time as executeTime,
		tab1.courseId as courseId,
		tab1.courseName as courseName,
		tab1.schoolName as schoolName,
		tab1.professionalId as professionalId
		FROM t_curriculum curr
		LEFT JOIN (
			SELECT curriculum_id,
			GROUP_CONCAT(cla.course_id) as courseId,
			GROUP_CONCAT(cl.`name`) as courseName,
			p.id as professionalId,
			p.name as professionalName,
			s.name as schoolName
		FROM
	 		t_curriculum_course cla
			LEFT JOIN t_course	cl
			ON cla.course_id = cl.id
			LEFT JOIN t_professional p
			ON cl.professional_id = p.id
			LEFT JOIN t_school s
			ON cla.school_id = s.id
			GROUP BY cla.curriculum_id
		) tab1
		ON curr.id = tab1.curriculum_id
  	WHERE curr.id = #{data.id}	
) tab

 