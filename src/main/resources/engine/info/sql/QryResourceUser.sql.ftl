select
	u.`name`,
	u.id,
	u.easemod_username as easeUserName
	FROM
	t_resource res
	LEFT JOIN t_res_func resf ON res.id = resf.res_id
	LEFT JOIN t_function f ON f.id = resf.func_id
	LEFT JOIN t_role_func rf ON rf.func_id = f.id
	LEFT JOIN t_customer_user_role ur ON rf.role_id = ur.role_id
	LEFT JOIN t_customer_user u ON ur.customer_user_id = u.id
	WHERE
	 res.`id` ='459'
	 
	 union all
	 
	 select
	 u.`name`,
	u.id,
	u.easemod_username as easeUserName
	
	from
	t_customer_user u where u.id=2