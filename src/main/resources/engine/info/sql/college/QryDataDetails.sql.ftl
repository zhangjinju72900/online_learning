select DISTINCT

(select count(*)as count from (
select 
o.customer_user_id
 from t_operate_log o
left JOIN t_customer_user u on u.id=o.customer_user_id
left JOIN t_school s on u.school_id = s.id  where  DATE_FORMAT(o.create_time,'%Y-%m-%d ')  >= #{data.createTime} 
AND DATE_FORMAT(o.create_time,'%Y-%m-%d ')  <= #{data.createTime1}
)a
)LivelyCount,
(select count(*)as count from(
select DISTINCT
o.customer_user_id
 from t_operate_log o
left JOIN t_customer_user u on u.id=o.customer_user_id
left JOIN t_school s on u.school_id = s.id where  DATE_FORMAT(o.create_time,'%Y-%m-%d ')  >= #{data.createTime} 
AND DATE_FORMAT(o.create_time,'%Y-%m-%d ')  <= #{data.createTime1}
)bb
)LivelyUser,

(select count(*) as count from (
select 
o.customer_user_id
 from t_operate_log o
left JOIN t_customer_user u on u.id=o.customer_user_id
left JOIN t_school s on u.school_id = s.id 
where o.assembly_type=14 and  DATE_FORMAT(o.create_time,'%Y-%m-%d ')  >= #{data.createTime} 
AND DATE_FORMAT(o.create_time,'%Y-%m-%d ')  <= #{data.createTime1}
)cc
)informationCount,
(select count(*)as count  from(select DISTINCT o.customer_user_id
 from t_operate_log o
 where not exists(select 1 
                  from t_operate_log b
                  where o.customer_user_id=b.customer_user_id and b.create_time<o.create_time )and assembly_type=3  and  DATE_FORMAT(o.create_time,'%Y-%m-%d ')  >= #{data.createTime} 
AND DATE_FORMAT(o.create_time,'%Y-%m-%d ')  <= #{data.createTime1})dd)addUser,
(select count(*) as count from(
select 
o.customer_user_id
 from t_operate_log o
left JOIN t_customer_user u on u.id=o.customer_user_id
left JOIN t_school s on u.school_id = s.id 
where o.assembly_type=8  and  DATE_FORMAT(o.create_time,'%Y-%m-%d ')  >= #{data.createTime} 
AND DATE_FORMAT(o.create_time,'%Y-%m-%d ')  <= #{data.createTime1}
)ee)CoursewareCount
from t_operate_log 