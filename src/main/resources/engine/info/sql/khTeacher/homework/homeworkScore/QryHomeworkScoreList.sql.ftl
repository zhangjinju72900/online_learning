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
	case distribute_status when 3 then d2.avgScore
	else '待批改' end as avgScore,
	h.create_time as createTime,
	d.name as homeworkTypeName,
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
	left join (select h1.id as hid,
						case h1.homework_type when 0 then h1.avg_score
						when 1 then d1.name end as avgScore
						from t_homework h1
						left join (select h2.id as id,
													d3.name as name from t_dict d3 
													left join t_homework h2
													on h2.avg_score = d3.code
													and d3.cata_code = 't_homework_answer_subjective_score_level')d1
						on h1.id = d1.id)d2
	on h.id = d2.hid
	where h.teacher_id =  #{data.session.userInfo.userId} and h.valid_flag = 0 and h.distribute_status=3
	order by h.distribute_time desc,h.homework_type desc
 ) a 