select * from (
	SELECT
	th.id AS id,
	th.name AS name,
	th.class_id AS classId,
	CONCAT(tc.grade,"级",tc.name) AS className,
	th.course_id AS courseId,
	tco.name AS courseName,
	th.section_id AS sectionId,
	tsn.name AS sectionName,
	th.homework_type AS homeworkType,
	d.name AS homeworkTypeName,
	th.distribute_time AS distributeTime,
	th.end_time as endTime,
	tu.name as teacherName,
	case when th.distribute_status = 3 and  th.homework_type = 0 then th.avg_score
	when th.distribute_status = 3 and  th.homework_type = 1 then  d1.name
	else '未批改' end as avgScore
	FROM t_homework th 
	LEFT JOIN t_school ts ON ts.id = th.school_id 
	LEFT JOIN t_class tc ON tc.id = th.class_id 
	LEFT JOIN t_course tco ON tco.id = th.course_id 
	LEFT JOIN t_section tsn ON tsn.id = th.section_id 
	LEFT JOIN t_customer_user tu on th.teacher_id = tu.id
	LEFT JOIN t_dict d ON d.code = th.homework_type and d.cata_code = 't_homework.homework_type' 
	LEFT JOIN t_dict d1 on d1.code = th.avg_score and d1.cata_code = 't_homework_answer_subjective_score_level'
	where th.valid_flag = 0 and tc.valid_flag=0 and tco.valid_flag=0 and ts.valid_flag=0 and tsn.valid_flag=0 and tu.valid_flag=0 
	and th.distribute_time >=date_sub((DATE_FORMAT(now(),'%Y-%m-%d')),interval 7 day) 
	and  th.distribute_time<=(DATE_FORMAT(now(),'%Y-%m-%d')) and ts.id=#{data.id}
 	order by th.distribute_time desc
 ) a
