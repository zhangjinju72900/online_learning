select * from (
select 
	o.id,
	o.event_desc as eventDesc,
	o.source_ip as sourceIp,
	o.customer_user_id as customerUserId,
	case when u.`name`='' or u.`name` is null then '未登录用户'  else u.`name` end customerUserName,
	o.event_type as eventType,
	case when o.platform_type=1 then 'APP' else 'PC教师端'  end platformType,
	case when o.assembly_type=1 then '班级'
	when o.assembly_type=2 then '错题集'
	when o.assembly_type=3 then '登陆'
	when o.assembly_type=4 then '动态'
	when o.assembly_type=5 then '关注'
	when o.assembly_type=6 then '活动'
	when o.assembly_type=7 then '教案'
	when o.assembly_type=8 then '课件'
	when o.assembly_type=9 then '上课'
	when o.assembly_type=10 then '试题'
	when o.assembly_type=11 then '数据'
	when o.assembly_type=12 then '通知'
	when o.assembly_type=13 then '系统'
	when o.assembly_type=14 then '资讯'
	when o.assembly_type=15 then '资源'
	when o.assembly_type=16 then '作业'
	end assemblyType,
	o.create_time as createTime
from t_operate_log o
LEFT JOIN t_customer_user u on u.id=o.customer_user_id
WHERE o.customer_user_id = #{data.id} 
and o.assembly_type not in(4,5,6,13,14) and date(o.create_time)>=#{data.createTimeStart} and date(o.create_time)<=#{data.createTimeEnd}
ORDER BY o.create_time desc
) a