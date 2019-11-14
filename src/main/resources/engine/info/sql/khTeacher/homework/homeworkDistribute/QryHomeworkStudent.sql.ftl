select * from (
	select 
	c.id as studentId,
		c.easemod_username as easName
	 from 
	t_customer_user_class cu 
	left join t_customer_user c
	on cu.customer_user_id = c.id
	join t_customer_user_role ur on cu.customer_user_id = ur.customer_user_id
	where cu.class_id = #{data.classId} and ur.role_id = 10
 ) a 