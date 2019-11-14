	update t_homework_answer
	set subjective_score_level = #{data.subjectiveScoreLevel},
	remark = #{data.remark},
	status = 3,
	update_time = now(),
	update_by = #{data.session.userInfo.userId} 
	where id = #{data.homeworkAnswerId}