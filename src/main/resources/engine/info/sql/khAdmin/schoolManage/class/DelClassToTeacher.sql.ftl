delete 
	from 
	t_customer_user_class 
	WHERE 
		class_id = #{data.id} 
		AND customer_user_id in 
			(SELECT t_customer_user_role.customer_user_id 
				FROM t_customer_user_role 
				WHERE t_customer_user_role.role_id=11 or t_customer_user_role.role_id=10)
