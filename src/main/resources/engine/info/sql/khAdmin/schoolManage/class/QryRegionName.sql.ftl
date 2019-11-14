select * from(select 
	rr.description as name
from t_customer_user u 
LEFT JOIN t_customer_user_role r on r.customer_user_id=u.id 
LEFT JOIN t_role rr on rr.id=r.role_id 
JOIN t_region rn on rn.name=rr.description
where u.id=#{data.session.userInfo.userId} and r.role_id not in (1,10,11) and u.id!=2 and rn.valid_flag=0
union ALL
select 
	name as text 
	from t_region where valid_flag = 0 and 2=#{data.session.userInfo.userId}

)a