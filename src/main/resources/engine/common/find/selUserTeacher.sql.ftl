select * from (
	select  
	u.id,
	u.name
 	from t_customer_user u
left join t_customer_user_role ur on u.id=ur.customer_user_id
where ur.role_id=11 and u.school_id= (select school_id from t_class where id = #{data.id}) and u.id not in (select customer_user_id from t_customer_user_class where class_id = #{data.id})
) a 
