select * from (
	select 
	c.id as value,
	c.name as text
	from  t_customer_user_class cuc
	left  join  t_customer_user cu
	on cu.id = cuc.customer_user_id
	left join t_class c
	on c.id = cuc.class_id
	where cu.id = #{data.session.userInfo.userId}
 ) a 