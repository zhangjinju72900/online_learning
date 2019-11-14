update 
t_question 
set 
	valid_flag = 1,
	update_time = now(),
	update_by = #{data.session.userInfo.userId}
where id = #{data.id} and data_flag = 1 and teacher_id = #{data.session.userInfo.userId}

