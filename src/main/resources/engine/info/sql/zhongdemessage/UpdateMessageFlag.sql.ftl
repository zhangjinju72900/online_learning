	update t_message_record 
	set read_flag = 1
	where id = #{data.id}