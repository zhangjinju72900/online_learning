update t_course 
set valid_flag=1,
	update_time=now(),
	update_by=#{data.session.userInfo.userId}
 where id=#{data.id};
delete from t_course_label where course_id=#{data.id};
delete from t_section where course_id=#{data.id};