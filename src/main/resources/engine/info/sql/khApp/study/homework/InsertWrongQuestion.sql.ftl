delete from t_wrong_question_set where homework_id= #{data.homeworkId} and homework_detail_id=#{data.homeworkDetailId}  and student_id = #{data.userId};
INSERT INTO `t_wrong_question_set` (
	`homework_id`,
	`homework_detail_id`,
	`school_id`,
	`class_id`,
	`course_id`,
	`section_id`,
	`question_id`,
	`teacher_id`,
	`student_id`,
	`valid_flag`,
	`create_time`,
	`create_by`,
	`update_time`,
	`update_by`
)
	select 
		#{data.homeworkId},
		#{data.homeworkDetailId},
		school_id,
		class_id,
		course_id,
		section_id,
		#{data.id},
		teacher_id,
		#{data.userId},
		'0',
		now(),
		#{data.userId},
		now(),
		#{data.userId}
		from t_homework where id = #{data.homeworkId} and not EXISTS(SELECT id FROM t_wrong_question_set o WHERE o.question_id = #{data.id} and o.student_id = #{data.userId});

