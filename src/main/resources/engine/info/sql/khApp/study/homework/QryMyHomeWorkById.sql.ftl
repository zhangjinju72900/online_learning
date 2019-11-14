SELECT
	ha.id,
	h.`name` as homeworkName,
	c.`name` as courseName,
	s.`name` as sectionName,
	h.distribute_time as distributeTime,
	ha.homework_type as homeType,
	h.end_time as endTime,
	case when (NOW()>end_time and ha.`status` in('0','1')) then 4 else  ha.`status` end status,
	case when (select  count(1) as fileCount from t_question_file qf join t_homework_detail hd on qf.question_id = hd.question_id 
	where hd.homework_id = h.id) > 0 then 1
	else 0 end as fileFlag,
	CONCAT(u.`name`,'老师') as teacherName,
	CASE when ha.status = 3 then 
		CASE when ha.homework_type = 0 then objective_real_score
			else d.`name` end 
	else '无' end as answerResult,
	case when (NOW()>end_time and ha.`status` = 2 ) then 1 else  0 end timeOutStatus 
FROM
	t_homework_answer ha
LEFT JOIN t_homework h ON ha.homework_id = h.id
left join t_course c on ha.course_id = c.id
left join t_section s on ha.section_id = s.id
left join (select hd.homework_id, count(1) as fileCount from t_question_file qf join t_homework_detail hd on qf.question_id = hd.question_id 
	where hd.homework_id = 10)tab1 on ha.homework_id = tab1.homework_id
left join t_customer_user u on ha.teacher_id = u.id
LEFT JOIN t_dict d on ha.subjective_score_level = d.code
where d.cata_code = 't_homework_answer_subjective_score_level'
and ha.valid_flag = 0 and ha.student_id = #{data.studentId} and ha.id = #{data.id}
