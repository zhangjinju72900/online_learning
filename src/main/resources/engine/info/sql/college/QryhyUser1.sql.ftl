select 
distinct

(select count(1)as newUser  
from
(select 
DISTINCT tol.customer_user_id
from t_operate_log tol
where not exists(
		select 1 
    from t_operate_log tl
 where tol.customer_user_id=tl.customer_user_id and tl.create_time<tol.create_time )and assembly_type=3 and tol.create_time>=date_sub((DATE_FORMAT(now(),'%Y-%m-%d')),interval 1 day) 
and tol.create_time<=(DATE_FORMAT(now(),'%Y-%m-%d'))
)aa
)newUser,

(SELECT count(uid) as hyUser from(
select 
DISTINCT tol.customer_user_id as uid,
DATE_FORMAT(tol.create_time,'%Y-%m-%d') as createTime
from t_operate_log tol
where  tol.platform_type=1 and tol.create_time>=date_sub((DATE_FORMAT(now(),'%Y-%m-%d')),interval 1 day) 
and tol.create_time<=(DATE_FORMAT(now(),'%Y-%m-%d'))
)bb 
)hyUser,

(SELECT count(id) as totalCount from(
select 
tol.id as id,
DATE_FORMAT(tol.create_time,'%Y-%m-%d') as createTime
from t_operate_log tol
where tol.platform_type=1 and tol.platform_type=1 and tol.create_time>=date_sub((DATE_FORMAT(now(),'%Y-%m-%d')),interval 1 day) 
and tol.create_time<=(DATE_FORMAT(now(),'%Y-%m-%d'))
)cc
)totalCount,

(select count(id) as kjCount from (
select 
tol.id as id,
DATE_FORMAT(tol.create_time,'%Y-%m-%d') as createTime
from t_operate_log tol
where tol.assembly_type=8 and tol.platform_type=1 and tol.create_time>=date_sub((DATE_FORMAT(now(),'%Y-%m-%d')),interval 1 day) 
and tol.create_time<=(DATE_FORMAT(now(),'%Y-%m-%d'))
)dd
)kjCount,

(SELECT count(uid) as zxCount from (
select 
tol.customer_user_id as uid,
DATE_FORMAT(tol.create_time,'%Y-%m-%d') as createTime
from t_operate_log tol
where tol.assembly_type in (4,14) and tol.platform_type=1 and DATE_FORMAT(tol.create_time,'%Y-%m-%d')>=date_sub((DATE_FORMAT(now(),'%Y-%m-%d')),interval 1 day) 
and DATE_FORMAT(tol.create_time,'%Y-%m-%d')<=(DATE_FORMAT(now(),'%Y-%m-%d'))
) ee
)zxCount

from t_operate_log
