select * from (
	select
	q.id as id,
	q.id as questionId,
	q.content as content,
	q.question_classify_id as questionClassifyId,
	q.question_type as questionType,
	q.data_flag as dataFlag,
	q.difficulty_level as difficultyLevel,
	IF(q.valid_flag=0,'启用','禁用') as validFlag,
	q.enable_status as enableStatus,
	q.create_time as createTime,
	c.name as questionClassifyName,
	d1.name as questionTypeName,
	d2.name as difficultyLevelName,
	d3.name as enableStatusName,
    d4.name as dataFlagType
	from t_question q
	left join t_question_classify c
	on q.question_classify_id = c.id
	left join t_dict d1
	on q.question_type = d1.code 
	and d1.cata_code = 't_question_question_type'
	left join t_dict d2
	on q.difficulty_level = d2.code 
	and d2.cata_code = 't_homework_difficulty_level'
	left join t_dict d3 
	on q.enable_status = d3.code
	and d3.cata_code = 't_question_enable_status'
    left join t_dict d4
    on q.data_flag = d4.code
    and d4.cata_code = 't_question.data_flag'
	where q.valid_flag = 0 and q.question_type!=3 and (q.data_flag = 0 or teacher_id = #{data.session.userInfo.userId}) order by q.create_time desc 
 ) a 