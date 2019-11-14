UPDATE t_homework
SET avg_score = (
	SELECT
		ifnull(
			CONVERT (
				SUM(objective_real_score) / COUNT(1),
				DECIMAL (12, 2)
			),
			0
		)
	FROM
		t_homework_answer
	WHERE
		homework_id = #{data.homeworkId}
	AND STATUS = 3
) where id = #{data.homeworkId}