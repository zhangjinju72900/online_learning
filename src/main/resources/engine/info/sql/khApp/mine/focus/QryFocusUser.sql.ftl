select * from (
	SELECT 
	o.customer_user_id as customerUserId
	from t_user_focus_on o
	where o.focus_on_id = #{data.session.userInfo.userId} and valid_flag = '0'
) a 