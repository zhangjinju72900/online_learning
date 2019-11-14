update t_user_focus_on 
set valid_flag=1,
	update_time=now(),
	update_by=#{data.session.userInfo.userId}
where customer_user_id = #{data.session.userInfo.userId} and focus_on_id = #{data.focusOnId}