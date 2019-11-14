select * from (
	SELECT 
		cc.id as id,
		cc.class_id as classId, 
		c.`name` as className,
		c.grade as gradeName 
		FROM t_customer_user_class cc 
		LEFT JOIN t_class c 
		ON cc.class_id = c.id
	WHERE cc.customer_user_id = #{data.id}
) tab
