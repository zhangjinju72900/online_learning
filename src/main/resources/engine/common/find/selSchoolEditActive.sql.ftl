select * from (
		select 
	s.id as id,
	s.name as name,
	r.id as region
	 from t_school s 
LEFT JOIN t_region r on r.id=s.region_id
where s.valid_flag = '0' and if(2=#{data.session.userInfo.userId},1=1,  
(select 

	GROUP_CONCAT(rr.description) as text
	
from t_customer_user u 
LEFT JOIN t_customer_user_role r on r.customer_user_id=u.id 
LEFT JOIN t_role rr on rr.id=r.role_id 
JOIN t_region rn on rn.name=rr.description
where u.id=#{data.session.userInfo.userId} and rr.description like '%区%' and r.role_id not in (1,10,11) and u.id!=2 and rn.valid_flag=0) like CONCAT('%',r.`name`,'%')

) and r.id =#{data.id}
 ) a 