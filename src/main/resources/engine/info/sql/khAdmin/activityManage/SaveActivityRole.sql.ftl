insert into 
	t_activity_join_role
	(activity_id,role_id,valid_flag,create_time,create_by,update_time,update_by)
	values
	(#{data.id},#{data.roleId},0,now(),#{data.session.userInfo.empId},now(),#{data.session.userInfo.empId});
