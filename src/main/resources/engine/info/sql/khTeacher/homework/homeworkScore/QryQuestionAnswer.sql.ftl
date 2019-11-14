select * from (
	select 
	h.student_id as studentId,
	case h.objective_real_score when 0 then '错误'
	else '正确' end as correctFlagName,
	u.name as studentName
	from t_homework_detail_answer h
	left join t_customer_user u
	on h.student_id = u.id
	where h.homework_id = #{data.homeworkId} and h.question_id = #{data.questionId} and h.status = 3
 ) a 