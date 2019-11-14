select * from (
	select 
	r.role_id as roleId,
	role.name as roleName
	from t_activity_join_role r
	left join t_role role
	on r.role_id=role.id 
	where r.activity_id=#{data.activityId}
	
) a
