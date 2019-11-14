select * from (
	select 
	h.id,
	h.homework_answer_id as homeworkAnswerId,
	h.question_id as questionId,
	h.objective_real_score as objectiveRealScore,
	case h.objective_real_score when 0 then 1
	else 0 end as correctFlag,
	q.content as content,
	o.title as correctTitle,
	ho.title as answerOption
	from t_homework_answer ha 
	left join  t_homework_detail_answer h
	on ha.id = h.homework_answer_id
	left join t_question q
	on h.question_id = q.id
	and q.valid_flag = 0
	left join (select q1.id as id, GROUP_CONCAT(a.title) as title 
						from t_question q1
						left join t_question_answer_options a 
						on q1.id = a.question_id
						and a.correct_flag = 0
						GROUP BY q1.id
						)o
	on q.id = o.id
	left join (select h1.id as hid,h1.create_by,
							a1.question_id,
							GROUP_CONCAT(a1.title) as title
							from t_homework_detail_objective_answer h1
							 join t_question_answer_options a1
							on h1.question_answer_options_id = a1.id 
							GROUP BY h1.homework_detail_answer_id 
						)ho 
	on h.question_id = ho.question_id and ho.create_by=ha.student_id
	where ha.id = #{data.homeworkAnswerId}
	ORDER BY h.question_id asc
 ) a 