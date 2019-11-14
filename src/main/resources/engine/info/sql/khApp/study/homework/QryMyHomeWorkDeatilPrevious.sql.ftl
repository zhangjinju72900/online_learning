select 
	tab1.homeworkId,
	tab1.homeworkDetailId,
	tab1.homeworkAnswerId,
	tab1.question_id as id, 
	tab1.status,
	h.`name` as homeworkName,
	q.content,
	q.question_type as questionType,
	q.difficulty_level as difficultyLevel,
	q.answer_thought as answerThought,
	(
	SELECT
		count(1) as nextCount
	FROM
		t_homework_detail hd
	JOIN t_homework_answer ha ON hd.homework_id = ha.homework_id
	WHERE
		ha.id = #{data.homeworkAnswerId} and hd.id > #{data.id}-1 ORDER BY hd.question_id asc limit 1
	)as nextFlag,
	(
	SELECT
		count(1) as nextCount
	FROM
		t_homework_detail hd
	JOIN t_homework_answer ha ON hd.homework_id = ha.homework_id
	WHERE
		ha.id = #{data.homeworkAnswerId} and hd.id < #{data.id}-1 ORDER BY hd.question_id asc limit 2
	)as previousFlag,
	ifnull(hda.id, 0) as homeworkDetailAnswerId,
	hda.content as answerContent,
	case when hda.objective_real_score > 0 then 0
	else 1 end as answerFlag
from (
	SELECT
		hd.homework_id as homeworkId,
		ha.id as homeworkAnswerId,
		hd.id as homeworkDetailId,
		hd.question_id,
		ha.status
	FROM
		t_homework_detail hd
	JOIN t_homework_answer ha ON hd.homework_id = ha.homework_id
	WHERE
		ha.id = #{data.homeworkAnswerId} and hd.id = #{data.id}-1 ORDER BY hd.question_id asc limit 1
)tab1 
LEFT JOIN t_question q on tab1.question_id = q.id 
left join t_homework h on tab1.homeworkId = h.id
left join t_homework_detail_answer hda on tab1.homeworkAnswerId = hda.homework_answer_id and tab1.homeworkDetailId = hda.homework_detail_id
and tab1.homeworkId = hda.homework_id and tab1.question_id = hda.question_id
where q.enable_status = 0 