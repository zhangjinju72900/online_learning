select * from (
	select 
	h.question_id as questionId,
	Ifnull(h1.countCommit,0) as countCommit,
	IFNULL(h2.countWrong,0) as countWrong,
	CONCAT(CONVERT(IFNULL(h2.countWrong,0)/IFNULL(h1.countCommit,0),DECIMAL(12,2))*100,'%') as wrongRate,
	q.title as correctTitle,
	q.content as questionContent
	
	from (select question_id
	from t_homework_detail 
	where homework_id = #{data.homeworkId} 
	group by question_id)h
	
	LEFT JOIN (select question_id,count(question_id) as countCommit
	from t_homework_detail_answer 
	where homework_id = #{data.homeworkId} 
	group by question_id)h1
	on h.question_id = h1.question_id
	
	LEFT JOIN (select question_id,count(question_id) as countWrong
	from t_homework_detail_answer 
	where homework_id = #{data.homeworkId} and objective_real_score = 0
	group by question_id)h2
	on h.question_id= h2.question_id
	
	LEFT JOIN (select q1.id,
							q1.content,
							GROUP_CONCAT(a.title) as title 
							from t_question q1
							left join t_question_answer_options a 
							on q1.id = a.question_id
							and a.correct_flag = 0
							GROUP BY q1.id)q
	on h.question_id= q.id
	ORDER BY h.question_id ASC
 ) a 