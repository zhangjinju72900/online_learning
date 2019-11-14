select * from (
	select 
	a.id as id,
	a.student_id as studentId,
	a.objective_real_score as objectiveRealScore,
	a.status as status,
	a.finish_time as finishTime,
	cu.name as studentName,
	cu.file_id as fileId,
	d2.name as statusName,
	case when a.status in (2,3) then 0
	else 1 end as commitFlag,
	f.oss_url as ossUrl
	from t_homework_answer a
	left join t_customer_user cu 
	on a.student_id = cu.id 
	left join t_file_index f
	on cu.file_id = f.id
	left join t_dict d2
	on a.status = d2.code
	and d2.cata_code = 't_homework_answer_satus'
	where a.homework_id = #{data.homeworkId} and a.valid_flag = 0 and a.homework_type = 0
	order by a.status desc,a.objective_real_score desc,a.finish_time desc
 ) a 