	insert into t_homework_template_detail(homework_template_id,
	question_id,
	create_time,
	create_by,
	update_time,
	update_by)
	values(
	#{data.templateId},
	#{data.questionId},
	now(),
	#{data.session.userInfo.userId},
	now(),
	#{data.session.userInfo.userId})
