INSERT INTO `t_homework_detail_answer` (
	`homework_id`,
	`homework_detail_id`,
	`homework_answer_id`,
	`question_id`,
	`objective_score`,
	`objective_real_score`,
	`content`,
	`status`,
	`student_id`,
	`teacher_id`,
	`create_time`,
	`create_by`,
	`update_time`,
	`update_by`
)
VALUES
	(
		#{data.homeworkId},
		#{data.homeworkDetailId},
		#{data.homeworkAnswerId},
		#{data.id},
		#{data.objectiveScore},
		#{data.realScore},
		#{data.content},
		'1',
		#{data.userId},
		#{data.teacherId},
		now(),
		#{data.userId},
		now(),
		#{data.userId}
	);

