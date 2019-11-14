select * from (
	select  distinct 
	tc.grade as value,
	CONCAT(tc.grade,"级") as text 
	from t_customer_user_class tcuc 
	LEFT JOIN t_class tc ON tc.id = tcuc.class_id 
	WHERE tcuc.customer_user_id = #{data.session.userInfo.userId} 
	and tc.valid_flag = 0  ORDER BY  grade  DESC
 ) a 