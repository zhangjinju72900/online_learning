select * from (
	select
	cl.id as id,
	class.id as classId,
	s.id as schoolId,
	c.name as name,
	case when c.sex = 0 then '男' else '女' end as sex,
	c.card_num as cardNum,
	c.email as email, 
	c.tel as tel
	from t_customer_user c
	left join t_school s on s.id = c.school_id
	left join t_customer_user_role r on r.customer_user_id = c.id
	left join t_customer_user_class cl on cl.customer_user_id =c.id
	left join t_class class on class.id = cl.class_id
	where r.role_id = 11 
 ) a
