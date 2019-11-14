SELECT
	hd.homework_id as homeworkId,
	q.question_type as questionType,
	hd.objective_score as objectiveScore,
	ha.teacher_id as teacherId,
	ha.status
FROM
	t_homework_detail hd
JOIN t_homework_answer ha ON hd.homework_id = ha.homework_id
left join t_question q on hd.question_id = q.id
WHERE
	hd.id = #{data.homeworkDetailId}
AND hd.question_id = #{data.id}
AND ha.id = #{data.homeworkAnswerId}
AND ha.student_id = #{data.userId}