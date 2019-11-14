select * from (
	select
	tcu.id as guideTeacherId,
	tcu.name as tname,
	(select count(1) from t_customer_user_role ur join t_customer_user_class u on ur.customer_user_id = u.customer_user_id
				join t_customer_user cu on ur.customer_user_id = cu.id where ur.role_id = 10 and u.class_id = c.id and cu.school_id = c.school_id) as studentCount
	from t_class c
	left join t_school s on s.id = c.school_id
	left join t_customer_user_class tcuc  on c.id = tcuc.class_id
	join t_customer_user tcu on tcuc.customer_user_id = tcu.id
	left join t_customer_user_role tcur on tcu.id= tcur.customer_user_id
	where c.valid_flag = 0 and c.id=#{model.classId} and tcur.role_id=11
 ) a