update t_subject 
set 
	top_flag='0',
	top_time=null,
	update_time=NOW(),
	update_by=#{data.session.userInfo.userId} 
where id=#{data.id}
