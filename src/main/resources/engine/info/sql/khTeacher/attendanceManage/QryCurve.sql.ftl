SELECT
	r.id,
	c.`name` as courseName,
	s.`name` as sectionName,
	cl.`name` as className,
	DATE_FORMAT(r.start_time,'%Y-%m-%d %T') as startTime,
	r.class_id as class,
	CONCAT(
		r.real_student_count,
		"/",
		r.student_count
	)as studentCount,
	IFNULL(convert((r.student_count-r.real_student_count)/r.student_count*100, decimal(12,2)),'0.00') as signRate
FROM
	t_attend_class_record r
LEFT JOIN t_course c ON r.course_id = c.id
LEFT JOIN t_class cl ON r.class_id = cl.id
LEFT JOIN t_section s ON r.section_id = s.id where r.teacher_id = #{data.session.userInfo.userId}
and case when #{data.classId} = '' then 1 = 1 
else r.class_id in (#{data.classId}) end
and case when #{data.startDate} = '' then 1 = 1 
else r.start_time >= #{data.startDate} end
and case when #{data.endDate} = '' then 1 = 1
else r.start_time <= #{data.endDate} end