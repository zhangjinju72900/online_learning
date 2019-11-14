	update t_message_record 
	set read_flag = 1, update_by = #{data.session.userInfo.userId}, update_time = now()
	where receiver_id = #{data.session.userInfo.userId} and read_flag = 0