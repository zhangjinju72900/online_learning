select * from (
	select
	h.id as id,
	h.name as name,
	h.school_id as schoolId,
	h.class_id as classId,
	h.course_id as courseId,
	h.section_id as sectionid,
	h.homework_type as homeworkType,
	h.distribute_time as distributeTime,
	h.end_time as endTime,
	h.distribute_status as distributeStatus,
	h.create_time as createTime,
	d.name as homeworkTypeName,
	d2.name as distributeStatusName,
	cl.name as className,
	co.name as courseName,
	se.name as sectionName
	from t_homework h
	left join t_class cl
	on h.class_id = cl.id
	left join t_course co
	on h.course_id = co.id
	left join t_section se 
	on h.section_id = se.id 
	left join t_dict d
	on h.homework_type = d.code 
	and d.cata_code = 't_homework.homework_type'
	left join t_dict d2
	on h.distribute_status = d2.code 
	and d2.cata_code = 't_homework_distribute_status'
	where h.teacher_id = #{data.session.userInfo.userId} and h.valid_flag = 0 and h.distribute_status in (0,1)
	order by h.distribute_time desc,h.homework_type desc
 ) a 