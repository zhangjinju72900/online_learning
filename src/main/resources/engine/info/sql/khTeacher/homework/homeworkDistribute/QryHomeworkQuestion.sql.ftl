SELECT
	hd.id,
	q.id as questionId,
	q.base_id as baseId,
	hd.objective_score AS objectiveScore,
	case when q.id is null then '试题已删除'
		else q.content end as content,
	q.question_type as questionType,
	q.difficulty_level as difficultyLevel,
	q.answer_thought as answerThought
FROM
	t_homework_detail hd
LEFT JOIN t_question q ON hd.question_id = q.id where hd.homework_id = #{data.id}