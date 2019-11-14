insert into t_question (
	content,
	question_type,
	difficulty_level,
	answer_thought,
	data_flag,
	teacher_id,
	create_time,
	create_by,
	update_time,
	update_by)
values (
	#{data.content},
	3,
	#{data.difficultyLevel},
	#{data.answerThought},
	1,
	#{data.session.userInfo.userId},
	now(),
	#{data.session.userInfo.userId},
	now(),
	#{data.session.userInfo.userId})