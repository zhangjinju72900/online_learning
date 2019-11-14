insert into t_homework_answer(
	homework_id,
	homework_type,
	school_id,
	class_id,
	course_id,
	section_id,
	student_id,
	teacher_id,
	create_time,
	create_by,
	update_time,
	update_by
)
values(
	#{data.homeworkId},
	#{data.homeworkType},
	#{data.schoolId},
	#{data.classId},
	#{data.courseId},
	#{data.sectionId},
	#{data.studentId},
	#{data.session.userInfo.userId},
	now(),
	#{data.session.userInfo.userId},
	now(),
	#{data.session.userInfo.userId}
)
