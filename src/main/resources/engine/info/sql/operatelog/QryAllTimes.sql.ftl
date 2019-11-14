select * from (
select 
 count(tab.userId) as countTimes,
 tab.`hour` as hours
from
(select 
o.customer_user_id as userId,
tcu.`name`,
DATE_FORMAT(o.create_time,'%Y-%m-%d') as createTime,
DATE_FORMAT(o.create_time,'%H') as hour
 from t_operate_log o
left JOIN t_customer_user tcu on o.customer_user_id = tcu.id
where
o.assembly_type not in (13,6,14,4,5)
and DATE_FORMAT(o.create_time,'%Y-%m-%d')>= #{data.createTimeStart} 
and DATE_FORMAT(o.create_time,'%Y-%m-%d')<= #{data.createTimeEnd}  
and o.customer_user_id=#{data.customUserId}
) tab
 GROUP BY tab.`hour`
) a