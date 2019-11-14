update t_information set top_flag='1',
	top_by=#{data.topBy},
	top_time=NOW()
	where id=#{data.id}
