select * from (
	select 
	h.id as id,
	h.name as name,
	CONCAT(h.distribute_time,'') as distributeTime,
	h.end_time endTime,
	c.name as className,
		h.correctStatus,
	count( DISTINCT a.student_id) as commitCount,
	d.name as homeworkTypeName,
	d2.name as difficultyLevelName,
	(select count(1) from t_homework_answer ah where ah.homework_id = h.id and ah.valid_flag = 0 ) as classCount
	from (select id,name,class_id,homework_type,distribute_time,end_time,difficulty_level,valid_flag,case when th.end_time<now() then 0 else 1 end correctStatus from t_homework th) h
	left join t_class c
	on h.class_id = c.id 
	left join t_dict d
	on h.homework_type = d.code 
	and d.cata_code = 't_homework.homework_type'
	left join t_dict d2
	on h.difficulty_level = d2.code 
	and d2.cata_code = 't_homework_difficulty_level'
	left join t_customer_user_class cuc
	on cuc.class_id = c.id 
	left join t_homework_answer a
	on a.homework_id = h.id
	and a.status in (2,3)
	and a.valid_flag = 0
	left join t_customer_user cu on cu.id = c.create_by and cu.valid_flag = 0
	left join t_customer_user cu1 on cu1.id = c.update_by and cu1.valid_flag = 0
	left join t_school s on s.id = c.school_id
	left join t_curriculum tc on c.curriculum_id = tc.id
	where h.id = #{data.homeworkId} and h.valid_flag = 0
 ) a 