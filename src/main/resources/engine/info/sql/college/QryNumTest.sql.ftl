
SELECT school_type as schoolType,count(uid)as numCount,createTime from(
select DISTINCT
tol.customer_user_id as uid,
c.name,
dt.name as school_type,
DATE_FORMAT(tol.create_time,'%Y-%m-%d') as createTime
from t_operate_log tol
LEFT JOIN t_customer_user c on c.id= tol.customer_user_id
LEFT JOIN t_school s on c.school_id = s.id
LEFT JOIN t_dict dt on s.school_type=dt.code 
where dt.cata_code='t_school.school_type'  and c.valid_flag=0 and s.valid_flag=0 and DATE_FORMAT(tol.create_time,'%Y-%m-%d')=#{data.createTime}
)as bbb GROUP BY school_type 