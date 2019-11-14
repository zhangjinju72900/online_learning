select * from (
	select  
	u.id,
	u.name
 	from t_customer_user u
left join t_customer_user_role ur on u.id=ur.customer_user_id
where ur.role_id=11 
) a 
