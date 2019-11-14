select * from (
	select 
	t.id as id,
		q.question_type as questionType,
	t.homework_template_id as homeworkTemplateId,
	t.question_id as questionId,
	t.create_time as createTime,
	t.create_by as createBy,
	q.content as questionContent
	from t_homework_template_detail t
	left join t_question q
	on t.question_id = q.id 
	where t.homework_template_id = #{data.homeworkTemplateId}
 ) a 