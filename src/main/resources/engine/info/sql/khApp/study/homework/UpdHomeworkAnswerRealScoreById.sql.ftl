UPDATE t_homework_answer
SET objective_real_score = (
	SELECT
		sum(objective_real_score)
	FROM
		t_homework_detail_answer
	WHERE
		homework_answer_id = #{data.homeworkAnswerId} and status = '1'
), status = '3', finish_time = now(), finish_speed = #{data.finishSpeed} where id = #{data.homeworkAnswerId};