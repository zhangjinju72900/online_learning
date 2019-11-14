select * from (
	SELECT
	q.id as id,
	q.content as content,
	q.answer_thought as answerThought,
	o.title as correctTitle
	from t_question q
	left join(select q1.id as id, GROUP_CONCAT(a.title) as title 
							from t_question q1
							left join t_question_answer_options a 
							on q1.id = a.question_id
							and a.correct_flag = 0
							and q1.valid_flag = 0
							GROUP BY q1.id
							)o
	on o.id = q.id
	where q.id = #{data.questionId} and q.valid_flag = 0
	
 ) a 