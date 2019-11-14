select * from (
	select  
	u.id,
	u.name
 	from t_customer_user u
left join (select * from t_customer_user_class ) uc on  u.id=uc.customer_user_id 
left join t_customer_user_role ur on u.id=ur.customer_user_id
where ur.role_id=10 and u.school_id=#{data.id} and uc.customer_user_id is null 
) a 
