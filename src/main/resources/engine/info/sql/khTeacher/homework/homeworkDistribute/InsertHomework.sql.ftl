insert into t_homework(
	name,
	school_id,
	class_id,
	course_id,
	section_id,
	homework_type,
	distribute_time,
	end_time,
	distribute_status,
	difficulty_level,
	teacher_id,
	create_time,
	create_by,
	update_time,
	update_by
	)
select 
	#{data.name},
	#{data.schoolId},
	#{data.classId},
	#{data.courseId},
	#{data.sectionId},
	#{data.homeworkType},
	now(),
	#{data.endTime},
	1,
	#{data.difficultyLevel},
	#{data.session.userInfo.userId},
	now(),
	#{data.session.userInfo.userId},
	now(),
	#{data.session.userInfo.userId}
	from DUAL
where not EXISTS (select id from t_homework where id=#{data.homeworkId})