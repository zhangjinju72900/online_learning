select * from(
SELECT
	r.id,
	r.class_id as classId,
	CONCAT(c.grade,"级",c.name) AS className,
	r.course_id as courseId,
	tc.name as courseName,
	r.section_id as sectionId,
	ts.name as sectionName,
	DATE(r.start_time) as startTime,
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
LEFT JOIN t_section ts ON ts.id = r.section_id 
)a