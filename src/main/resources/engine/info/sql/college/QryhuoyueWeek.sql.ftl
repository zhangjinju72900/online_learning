select 
 count( if(a.school_type=0,true,null)) as gCount,
  count( if(a.school_type=1,true,null)) as zCount,
 a.createTime,
 a.`hour`
from
(
select DISTINCT
o.customer_user_id,
s.school_type,
DATE_FORMAT(o.create_time,'%Y-%m-%d %H') as createTime,
DATE_FORMAT(o.create_time,'%H') as hour
 from t_operate_log o
left JOIN t_customer_user u on u.id=o.customer_user_id
left JOIN t_school s on u.school_id = s.id
where DATE_FORMAT(o.create_time,'%Y-%m-%d')>=date_sub((DATE_FORMAT(now(),'%Y-%m-%d')),interval 7 day) and DATE_FORMAT(o.create_time,'%Y-%m-%d')<=(DATE_FORMAT(now(),'%Y-%m-%d'))
)a
 GROUP BY a.`hour`