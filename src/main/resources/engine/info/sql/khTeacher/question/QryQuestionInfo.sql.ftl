SELECT
	q.id,
	q.content,
	q.difficulty_level AS difficultyLevel,
	q.answer_thought AS answerThought,
	GROUP_CONCAT(qf.file_id) as fileIds,
	GROUP_CONCAT(CONCAT(f.filename,'.',f.file_type)) as fileNames
FROM
	t_question q
LEFT JOIN t_question_file qf ON q.id = qf.question_id
LEFT JOIN t_file_index f on qf.file_id = f.id
WHERE
	q.id = #{data.id}
GROUP BY
	q.id