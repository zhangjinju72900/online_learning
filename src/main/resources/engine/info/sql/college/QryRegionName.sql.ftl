select * from(select 
	GROUP_CONCAT(rr.description) as regionName
from t_customer_user u 
LEFT JOIN t_customer_user_role r on r.customer_user_id=u.id 
LEFT JOIN t_role rr on rr.id=r.role_id 
where u.id=#{data.session.userInfo.userId} and rr.description like '%区%' and r.role_id not in (1,10,11) and u.id!=2 GROUP BY u.id
	union all 
	
	select '' as regionName,
		(select DATE_ADD(curdate(),interval -day(curdate())+1 day)) AS createTime 
		from dual where 2=#{data.session.userInfo.userId}
)a