
SELECT
	DISTINCT r.user_id

FROM
	t_sign_in_record r
JOIN t_customer_user_class c ON r.user_id = c.customer_user_id
LEFT JOIN t_customer_user_role ur on ur.customer_user_id=r.user_id
WHERE 
	c.class_id =  #{data.classId} and ur.role_id=10
	and date(r.sign_time) = date(SYSDATE()) 