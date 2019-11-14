select * from (
	select r.role_id as roleId
	from t_customer_user_role r
	where r.customer_user_id=#{data.userId}
	
) a
