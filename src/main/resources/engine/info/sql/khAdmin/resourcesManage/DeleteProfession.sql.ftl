update t_customer_resources 
set valid_flag=1,
	update_time=now(),
	update_by=#{data.session.userInfo.userId}
 where id=#{data.id} or parent_id=#{data.id}
