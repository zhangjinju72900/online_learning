SELECT
	ha.id,
	hk.`name` as homeworkName,
	CASE
WHEN ha.`status` = 3 THEN
		CASE
	WHEN ha.homework_type = 0 THEN
		format(objective_real_score,0)
	ELSE
		d.`name`
	END
		ELSE
	'无'
END AS answerResult,
 remark,
 CONCAT(finish_speed, '%') as finishSpeed,
  (select count(1) from t_wrong_question_set s where s.homework_id = ha.homework_id and s.student_id=#{data.session.userInfo.userId}) as wrongCount
	from t_homework_answer ha
	LEFT JOIN t_dict d on ha.subjective_score_level = d.`code`
	LEFT JOIN t_homework hk on hk.id=ha.homework_id
WHERE 
	ha.id = #{data.id}
AND ha.`status` = 3 and d.cata_code = 't_homework_answer_subjective_score_level'