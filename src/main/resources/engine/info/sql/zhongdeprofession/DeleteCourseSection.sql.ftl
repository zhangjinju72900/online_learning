update t_section 
set valid_flag=1,
	update_time=now(),
	update_by=#{data.session.userInfo.userId}
	where course_id=#{data.courseId} and id=#{data.sectionId};
delete from t_section_label where section_id=#{data.sectionId};