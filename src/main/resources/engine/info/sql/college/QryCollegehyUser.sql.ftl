select 
distinct

(SELECT id  FROM
t_school ts 
where ts.id = #{data.id}
)id,

(SELECT count(id) as teachCount from(
select 
tol.id as id,
DATE_FORMAT(tol.create_time,'%Y-%m-%d') as createTime
from t_operate_log tol
LEFT JOIN t_customer_user tcu on tol.customer_user_id = tcu.id
LEFT JOIN t_school ts on tcu.school_id = ts.id
where  tol.platform_type=1 and tol.assembly_type=9 and DATE_FORMAT(tol.create_time,'%Y-%m-%d')=#{data.createTime} and ts.id=#{data.id}
)bb 
)teachCount,

(SELECT count(uid) as hyUser from(
select 
DISTINCT tol.customer_user_id as uid,
DATE_FORMAT(tol.create_time,'%Y-%m-%d') as createTime
from t_operate_log tol
LEFT JOIN t_customer_user tcu on tol.customer_user_id = tcu.id
LEFT JOIN t_school ts on tcu.school_id = ts.id
where  tol.platform_type=1 and DATE_FORMAT(tol.create_time,'%Y-%m-%d')=#{data.createTime} and ts.id=#{data.id}
)bb 
)hyUser,

(SELECT count(id) as totalCount from(
select 
tol.id as id,
DATE_FORMAT(tol.create_time,'%Y-%m-%d') as createTime
from t_operate_log tol
LEFT JOIN t_customer_user tcu on tol.customer_user_id = tcu.id
LEFT JOIN t_school ts on tcu.school_id = ts.id
where tol.platform_type=1 and DATE_FORMAT(tol.create_time,'%Y-%m-%d')=#{data.createTime} and ts.id=#{data.id}
)cc
)totalCount,

(select count(id) as kjCount from (
select 
tol.id as id,
DATE_FORMAT(tol.create_time,'%Y-%m-%d') as createTime
from t_operate_log tol
LEFT JOIN t_customer_user tcu on tol.customer_user_id = tcu.id
LEFT JOIN t_school ts on tcu.school_id = ts.id
where tol.assembly_type=8 and tol.platform_type=1 and DATE_FORMAT(tol.create_time,'%Y-%m-%d')=#{data.createTime} and  ts.id=#{data.id}
)dd
)kjCount,

(SELECT count(uid) as zxCount from (
select 
tol.customer_user_id as uid,
DATE_FORMAT(tol.create_time,'%Y-%m-%d') as createTime
from t_operate_log tol
LEFT JOIN t_customer_user tcu on tol.customer_user_id = tcu.id
LEFT JOIN t_school ts on tcu.school_id = ts.id
where tol.assembly_type in (4,14) and tol.platform_type=1 and DATE_FORMAT(tol.create_time,'%Y-%m-%d')=#{data.createTime} and ts.id=#{data.id}
) ee
)zxCount

from t_operate_log
