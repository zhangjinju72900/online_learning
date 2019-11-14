	 	select 
		GROUP_CONCAT(a.`status`) as statusStr
	from t_homework_answer a
	left join t_customer_user cu 
	on a.student_id = cu.id 
	where a.homework_id = #{data.homeworkId} and a.valid_flag = 0 and a.homework_type = 1 and a.id  in (select h.id from t_homework_answer h LEFT JOIN t_homework k on h.homework_id=k.id where NOW()>k.end_time and h.`status`=2 and h.homework_id=a.homework_id)
	 GROUP BY a.homework_id
	 