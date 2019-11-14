select * from (
select 
 count(tab.userId) as countTimes,
 tab.createTime as createTime,
 tab.`hour` as hours,
 tab.userId AS customerUserId,
 tab.`name` as customerUserName
from
(select 
o.customer_user_id as userId,
tcu.`name`,
DATE_FORMAT(o.create_time,'%Y-%m-%d') as createTime,
DATE_FORMAT(o.create_time,'%H') as hour
 from t_operate_log o
left JOIN t_customer_user tcu on o.customer_user_id = tcu.id
) tab
 GROUP BY tab.`hour`
) a