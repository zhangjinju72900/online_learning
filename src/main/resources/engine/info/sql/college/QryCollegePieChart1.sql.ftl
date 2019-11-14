SELECT count(DISTINCT uid) as hyStudent,grade,id from(
SELECT 
ts.id as id,
ts.name as name,
td.name as grade,
tol.customer_user_id as uid
from t_operate_log tol
LEFT JOIN t_customer_user tcu on tol.customer_user_id = tcu.id
LEFT JOIN t_school ts on tcu.school_id = ts.id
LEFT JOIN t_dict td on tcu.grade = td.code
where  td.cata_code='t_customer_user.grade' and tcu.valid_flag=0 and ts.valid_flag=0 and tol.create_time>=date_sub((DATE_FORMAT(now(),'%Y-%m-%d')),interval 1 day) 
and tol.create_time<=(DATE_FORMAT(now(),'%Y-%m-%d'))  and  ts.id = #{data.id}
)aaa GROUP BY grade