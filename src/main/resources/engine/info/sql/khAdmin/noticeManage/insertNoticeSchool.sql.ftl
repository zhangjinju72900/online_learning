insert into t_notice_school
(   id,
	notice_id,
	school_id,
	create_time,
	create_by,
	update_time,
	update_by)
	values(
	#{data.id}, 
	#{data.noticeId}, 
	#{data.schoolId}, 
	 now(),
	 #{data.session.userInfo.userId}, 
	 now(), 
	 #{data.session.userInfo.userId})