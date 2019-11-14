select * from (
	select  
	u.id,
	u.name
 	from t_customer_user u
left join (select * from t_customer_user_role where role_id=#{data.id}) ur on  u.id=ur.customer_user_id 
where ur.customer_user_id is null 
) a 