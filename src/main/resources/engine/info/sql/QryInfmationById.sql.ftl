select i.content as title,i.id,i.create_by as createBy,
case when u.`name`='' or u.`name` is null then u.nickname else u.`name` end userName  from t_live_review i  
LEFT JOIN t_customer_user u on u.id=i.create_by   where i.id=#{data.id}
union all
select i.content as title,i.id,i.create_by as createBy,
case when u.`name`='' or u.`name` is null then u.nickname else u.`name` end userName 
 from t_information_review i LEFT JOIN t_customer_user u on u.id=i.create_by   where i.id=#{data.id}
union all
select i.title,i.id,i.create_by as createBy,
case when u.`name`='' or u.`name` is null then u.nickname else u.`name` end userName 
from t_information i LEFT JOIN t_customer_user u on u.id=i.create_by   where i.id=#{data.id}
