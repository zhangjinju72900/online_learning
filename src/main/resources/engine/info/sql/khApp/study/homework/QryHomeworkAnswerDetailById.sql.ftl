SELECT
	id
FROM
	t_homework_detail_answer
WHERE
	id = #{data.homeworkDetailAnswerId}
AND homework_id = #{data.homeworkId}
AND homework_detail_id = #{data.homeworkDetailId}
AND homework_answer_id = #{data.homeworkAnswerId}
AND question_id = #{data.id}
AND student_id = #{data.userId}