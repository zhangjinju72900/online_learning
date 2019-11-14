select * from (
	select
	h.id as id,
	h.name as name,
	h.school_id as schoolId,
	h.homework_type as homeworkType,
	h.create_time as createTime,
	h.difficulty_level as difficultyLevel,
	d1.name as homeworkTypeName,
	d2.name as difficultyLevelName,
	t.questionCount as questionCount
	from t_homework_template h
	left join t_dict d1
	on h.homework_type = d1.code 
	and d1.cata_code = 't_homework.homework_type'
	left join t_dict d2
	on h.homework_type = d2.code 
	and d2.cata_code = 't_homework_difficulty_level'
	left join (select DISTINCT h1.id as id,count(ht.id) as questionCount
					from t_homework_template h1
					left join t_homework_template_detail ht
					on h1.id = ht.homework_template_id
					group by  id
				) t
	on t.id = h.id
	where h.teacher_id = #{data.session.userInfo.userId} and h.valid_flag = 0 ORDER BY h.create_time desc
 ) a 