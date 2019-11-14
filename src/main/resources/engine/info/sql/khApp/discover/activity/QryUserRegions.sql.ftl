select * from (
	select s.region_id as regionId,
				 s.id as schoolId,
					GROUP_CONCAT(cs.class_id) as classId
	from t_customer_user u
	left join t_school s 	on u.school_id=s.id 
	LEFT JOIN t_customer_user_class  cs on cs.customer_user_id =u.id 
	where u.id=#{data.userId}
) a
