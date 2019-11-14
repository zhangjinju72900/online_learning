SELECT
	DISTINCT c.customer_user_id
FROM
	t_customer_user_class c LEFT JOIN t_customer_user_role r on r.customer_user_id=c.customer_user_id 
WHERE 
	c.class_id = #{data.classId} and r.role_id=10