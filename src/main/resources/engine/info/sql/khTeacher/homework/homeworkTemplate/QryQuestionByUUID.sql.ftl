select * from (
	select 
	0 as  id,
	q.question_type as questionType,
	0 as   homeworkTemplateId,
	q.id as questionId,
	q.create_time as createTime,
	q.create_by as createBy,
	q.content as questionContent,
	q.base_id as baseId
	from t_question q
	left join  t_homework_template_detail t 
	on t.question_id = q.id 
	where q.uuid =#{data.uuid}
 ) a 