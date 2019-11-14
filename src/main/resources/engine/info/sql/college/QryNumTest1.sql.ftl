SELECT school_type as schoolType,count(school_type) as numCount from(
select DISTINCT
s.id as id,
tol.customer_user_id as uid,
c.name,
dt.name as school_type
from t_operate_log tol
LEFT JOIN t_customer_user c on c.id= tol.customer_user_id
LEFT JOIN t_school s on c.school_id = s.id
LEFT JOIN t_dict dt on s.school_type=dt.code 
where dt.cata_code='t_school.school_type' and tol.create_time>=date_sub((DATE_FORMAT(now(),'%Y-%m-%d')),interval 1 day) 
and tol.create_time<=(DATE_FORMAT(now(),'%Y-%m-%d')) and c.valid_flag=0 and s.valid_flag=0
) as bbb GROUP BY school_type 