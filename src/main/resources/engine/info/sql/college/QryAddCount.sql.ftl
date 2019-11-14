select * from(select count(*)as informationCount ,a.createTime from(select DISTINCT o.customer_user_id,DATE_FORMAT(o.create_time,'%Y-%m-%d') as createTime
 from t_operate_log o
 where not exists(select 1 
                  from t_operate_log b
                  where o.customer_user_id=b.customer_user_id and b.create_time<o.create_time )and assembly_type=3 and  DATE_FORMAT(o.create_time,'%Y-%m-%d ')  >= #{data.createTime} 
AND DATE_FORMAT(o.create_time,'%Y-%m-%d ')  <= #{data.createTime1})a  GROUP BY a.createTime)b