INSERT INTO `t_homework_detail_objective_answer` (
	`homework_detail_answer_id`,
	`question_answer_options_id`,
	`create_time`,
	`create_by`,
	`update_time`,
	`update_by`
)
VALUES
	(
		#{data.homeworkDetailAnswerId},
		#{data.optionId},
		now(),
		#{data.userId},
		now(),
		#{data.userId}
	);
