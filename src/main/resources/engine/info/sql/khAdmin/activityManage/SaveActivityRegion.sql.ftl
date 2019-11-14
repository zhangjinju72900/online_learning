insert into 
	t_activity_join_region
	(activity_id,region_id,valid_flag,create_time,create_by,update_time,update_by,schoolId,classId)
	values
	(#{data.id},#{data.regionId},0,now(),#{data.session.userInfo.empId},now(),#{data.session.userInfo.empId},#{data.schoolId},#{data.classId});
