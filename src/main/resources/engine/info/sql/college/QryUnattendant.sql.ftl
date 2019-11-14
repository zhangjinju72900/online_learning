select * from(
SELECT
	r.id,
	r.student_count-r.real_student_count as unattendant,
	r.class_id as classId
	
FROM
	t_attend_class_record r
LEFT JOIN t_course tc ON tc.id = r.course_id
LEFT JOIN t_class c ON c.id = r.class_id 
LEFT JOIN t_school tsh ON tsh.id = r.school_id 
LEFT JOIN t_section ts ON ts.id = r.section_id 
LEFT JOIN t_customer_user tu on r.teacher_id = tu.id
where  r.id=#{data.id} 

)a