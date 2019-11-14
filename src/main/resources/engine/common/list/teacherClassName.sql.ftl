select * from (
	select 
	tcuc.class_id as value,
	CONCAT(tc.grade,"级",tc.name) as text 
	from t_customer_user_class tcuc 
	JOIN t_class tc ON tc.id = tcuc.class_id 
	WHERE tcuc.customer_user_id = #{data.session.userInfo.userId}
	order by tc.grade desc
 ) a 