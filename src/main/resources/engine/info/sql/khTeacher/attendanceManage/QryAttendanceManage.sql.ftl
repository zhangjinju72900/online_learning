select * from(
SELECT
	r.id,
	tcu1.id as ids,
	c.`name` as courseName,
	s.`name` as sectionName,
	cl.`name` as className,
	r.start_time as startTime,
	r.create_time  as  createTime,
	r.class_id as class,
	CONCAT(
		r.real_student_count,
		"/",
		r.student_count
	)as studentCount,
	IFNULL(CONCAT(convert((r.real_student_count)/r.student_count*100, decimal(12,2)),"%"),'0.00%') as signRate
	
FROM
	t_attend_class_record r
LEFT JOIN t_course c ON r.course_id = c.id
LEFT JOIN t_class cl ON r.class_id = cl.id
LEFT JOIN t_section s ON r.section_id = s.id 
LEFT JOIN t_customer_user tcu1 ON r.teacher_id = tcu1.id 
where r.class_id in(
	select 
	c.id 
	from  t_customer_user_class cuc
	left  join  t_customer_user cu
	on cu.id = cuc.customer_user_id
	left join t_class c
	on c.id = cuc.class_id
  where cu.id = #{data.session.userInfo.userId} 
)
 AND r.real_student_count<=r.student_count
 AND tcu1.id=#{data.session.userInfo.userId}
 order  by r.create_time  desc
)a  