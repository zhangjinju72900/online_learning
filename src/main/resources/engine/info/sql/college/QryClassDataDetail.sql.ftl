select * from(
SELECT
	r.id,
	r.class_id as classId,
	CONCAT(c.grade,"级",c.name) AS className,
	tc.name as courseName,
	ts.name as sectionName,
	DATE(r.start_time) as startTime,
	tu.name as teacherName,
	CONCAT(
		r.real_student_count,
		"/",
		r.student_count
	)as attendance,
	IFNULL(CONCAT(convert(r.real_student_count/r.student_count*100, decimal(12,2)),"%"),'0.00%') as rate
FROM
	t_attend_class_record r
LEFT JOIN t_course tc ON tc.id = r.course_id
LEFT JOIN t_class c ON c.id = r.class_id 
LEFT JOIN t_school tsh ON tsh.id = r.school_id 
LEFT JOIN t_section ts ON ts.id = r.section_id 
LEFT JOIN t_customer_user tu on r.teacher_id = tu.id
where tc.valid_flag=0 and c.valid_flag=0 and tsh.valid_flag=0 and ts.valid_flag=0 and tu.valid_flag=0
and (DATE_FORMAT(r.start_time,'%Y-%m-%d')) >=#{data.startTime} 
and (DATE_FORMAT(r.start_time,'%Y-%m-%d'))<=#{data.endTime} 
and tsh.id=#{data.id}

)a