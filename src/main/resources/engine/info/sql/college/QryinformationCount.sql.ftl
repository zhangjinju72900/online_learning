select *from (select 
 count(*)as informationCount,
 
 a.createTime
from
(
select 
o.customer_user_id,
s.school_type,
DATE_FORMAT(o.create_time,'%Y-%m-%d ') as createTime
 from t_operate_log o
left JOIN t_customer_user u on u.id=o.customer_user_id
left JOIN t_school s on u.school_id = s.id 
where o.assembly_type=14 and DATE_FORMAT(o.create_time,'%Y-%m-%d ')  >= #{data.createTime} 
AND DATE_FORMAT(o.create_time,'%Y-%m-%d ')  <= #{data.createTime1}
)a
 GROUP BY a.createTime
)b
