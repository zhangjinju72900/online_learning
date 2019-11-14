update t_homework set
	name=#{data.name},
	school_id=#{data.schoolId},
	class_id=	#{data.classId},
	course_id=#{data.courseId},
	section_id=#{data.sectionId},
	homework_type=#{data.homeworkType},
	distribute_time=now(),
	end_time=#{data.endTime},
	distribute_status=#{data.status},
	difficulty_level=#{data.difficultyLevel},
	teacher_id=#{data.session.userInfo.userId},
	update_time=now(),
	update_by=#{data.session.userInfo.userId}
	
where id=#{data.homeworkId}

