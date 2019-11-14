select * from (
select * from(
	select
		ts.name as schoolName,
		ts.id as schoolId,
		ts.school_type AS schoolType, 
		tol.platform_type as platformType,	
		tol.update_time AS updateTime,	
		count(tol.id) as activityTimes
	from t_operate_log tol
	left join t_customer_user tcu on tol.customer_user_id = tcu.id
	left join t_school ts on tcu.school_id = ts.id
	group by ts.id
) sch_actTimes
where 
updateTime>=date_sub((DATE_FORMAT(now(),'%Y-%m-%d')),interval 30 day) 
and updateTime<=(DATE_FORMAT(now(),'%Y-%m-%d')) 
AND schoolId is not null
AND schoolType = 1
and platformType = 1
order by activityTimes DESC
LIMIT 10
) a
